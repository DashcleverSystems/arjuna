package io.arjuna.websites

import android.util.Log
import io.arjuna.logging.ARJUNA_TAG
import io.arjuna.schedule.domain.ScheduleRepository
import io.arjuna.schedule.domain.Time
import io.arjuna.schedule.domain.Weekday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private data class WebsitesLock(
    val websites: Set<Website>,
    val duration: Pair<Time, Time>,
    val onDays: Set<Weekday>
)

class WebsitesService(
    private val scheduleRepository: ScheduleRepository,
    private val coroutineScope: CoroutineScope
) {

    fun onUrlChange(
        urlChanged: UrlChanged,
        onBlockedWebsite: () -> Unit
    ) {
        coroutineScope.launch {
            scheduleRepository.findAll().collect { schedules ->
                val locks: Set<WebsitesLock> =
                    schedules.map { WebsitesLock(it.websites, it.from to it.to, it.onDays) }
                        .toSet()
                if (locks.isEmpty()) {
                    Log.d(ARJUNA_TAG, "Blocked websites is empty. Not blocking $urlChanged")
                    return@collect
                }
                if (locks.blocks(urlChanged.url)) {
                    Log.d(ARJUNA_TAG, "Blocking: ${urlChanged.url}")
                    onBlockedWebsite.invoke()
                }
            }
        }
    }

    private fun Set<WebsitesLock>.blocks(websiteUrl: String): Boolean {
        return this.any { lock ->
            lock.websites.any { website ->
                websiteUrl.contains(website.mainDomain)
            }
        }
    }

    data class UrlChanged(val url: String)
}