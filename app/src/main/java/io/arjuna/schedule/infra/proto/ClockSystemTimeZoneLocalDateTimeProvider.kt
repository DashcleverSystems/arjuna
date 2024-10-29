package io.arjuna.schedule.infra.proto

import io.arjuna.schedule.domain.SystemTimeZoneLocalDateTimeProvider
import java.time.Clock
import java.time.LocalDateTime

class ClockSystemTimeZoneLocalDateTimeProvider(
    private val clock: Clock
) : SystemTimeZoneLocalDateTimeProvider {

    override fun provide(): LocalDateTime {
        return clock.instant().atZone(clock.zone).toLocalDateTime()
    }
}