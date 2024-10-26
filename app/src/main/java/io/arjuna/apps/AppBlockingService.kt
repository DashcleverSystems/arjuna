package io.arjuna.apps

import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.util.Log
import io.arjuna.logging.ARJUNA_TAG
import io.arjuna.schedule.domain.ScheduleRepository
import io.arjuna.schedule.domain.Time
import io.arjuna.schedule.domain.Weekday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private data class AppBlock(
    val apps: Set<InstalledApp>,
    val duration: Pair<Time, Time>,
    val onDays: Set<Weekday>
)

class AppBlockingService(
    private val scheduleRepository: ScheduleRepository,
    private val coroutineScope: CoroutineScope,
    private val packageManager: PackageManager
) {

    fun onAppOpen(appPackageName: String, onBlockedApp: () -> Unit) {
        coroutineScope.launch {
            scheduleRepository.findAll().collect { schedules ->
                val locks: Set<AppBlock> =
                    schedules.map { AppBlock(it.apps, it.from to it.to, it.onDays) }
                        .toSet()
                if (locks.isEmpty()) {
                    Log.d(ARJUNA_TAG, "Blocked apps is empty. Not blocking $appPackageName")
                    return@collect
                }
                if (locks.blocks(appPackageName)) {
                    Log.d(ARJUNA_TAG, "Blocking: $appPackageName")
                    onBlockedApp()
                }
            }
        }
    }

    private fun Set<AppBlock>.blocks(appPackageName: String): Boolean {
        return this.any { lock ->
            lock.apps.any { app ->
                val info = packageManager.getPackageInfo(appPackageName, GET_META_DATA)
                info.applicationInfo.loadLabel(packageManager) == app.name
            }
        }
    }
}