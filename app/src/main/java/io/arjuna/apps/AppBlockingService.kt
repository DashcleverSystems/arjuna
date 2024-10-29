package io.arjuna.apps

import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.util.Log
import io.arjuna.logging.ARJUNA_TAG
import io.arjuna.schedule.domain.ScheduleRepository
import io.arjuna.schedule.domain.SystemTimeZoneLocalDateTimeProvider
import io.arjuna.schedule.domain.Time
import io.arjuna.schedule.domain.Weekday
import io.arjuna.schedule.domain.isIn
import io.arjuna.schedule.domain.isWithin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private data class AppLock(
    val apps: Set<InstalledApp>,
    val duration: Pair<Time, Time>,
    val onDays: Set<Weekday>
)

class AppBlockingService(
    private val scheduleRepository: ScheduleRepository,
    private val coroutineScope: CoroutineScope,
    private val packageManager: PackageManager,
    private val systemTimeProvider: SystemTimeZoneLocalDateTimeProvider
) {

    fun onAppOpen(appPackageName: String, onBlockedApp: () -> Unit) {
        coroutineScope.launch {
            scheduleRepository.findAll().collect { schedules ->
                val locks: Set<AppLock> =
                    schedules.map { AppLock(it.apps, it.from to it.to, it.onDays) }
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

    private fun Set<AppLock>.blocks(appPackageName: String): Boolean {
        return this.any { lock -> lock.blocks(appPackageName) }
    }

    private fun AppLock.blocks(appPackageName: String): Boolean {
        val now = systemTimeProvider.provide()
        return this.apps containsAppOfPackage appPackageName
                && now.toLocalTime() isWithin this.duration
                && now.dayOfWeek isIn this.onDays
    }

    private infix fun Collection<InstalledApp>.containsAppOfPackage(appPackageName: String): Boolean {
        return this.any { app ->
            val info = packageManager.getPackageInfo(appPackageName, GET_META_DATA)
            info.applicationInfo.loadLabel(packageManager) == app.name
        }
    }
}