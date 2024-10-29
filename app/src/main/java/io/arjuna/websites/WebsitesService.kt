package io.arjuna.websites

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

private data class WebsitesLock(
    val websites: Set<Website>,
    val duration: Pair<Time, Time>,
    val onDays: Set<Weekday>
)

class WebsitesService(
    private val scheduleRepository: ScheduleRepository,
    private val coroutineScope: CoroutineScope,
    private val systemTimeProvider: SystemTimeZoneLocalDateTimeProvider
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
                if (locks.block(urlChanged.url)) {
                    Log.d(ARJUNA_TAG, "Blocking: ${urlChanged.url}")
                    onBlockedWebsite.invoke()
                }
            }
        }
    }

    private fun Set<WebsitesLock>.block(websiteUrl: String): Boolean {
        return this.any { lock -> lock.block(websiteUrl) }
    }

    private fun WebsitesLock.block(websiteUrl: String): Boolean {
        val now = systemTimeProvider.provide()
        return this.websites contains websiteUrl
                && now.toLocalTime() isWithin this.duration
                && now.dayOfWeek isIn this.onDays
    }

    private infix fun Collection<Website>.contains(website: String): Boolean {
        return this.any { website.contains(it.mainDomain) }
    }

    data class UrlChanged(val url: String)
}