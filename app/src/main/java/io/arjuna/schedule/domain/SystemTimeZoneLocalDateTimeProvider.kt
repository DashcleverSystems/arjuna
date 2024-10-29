package io.arjuna.schedule.domain

import java.time.LocalDateTime

fun interface SystemTimeZoneLocalDateTimeProvider {

    fun provide(): LocalDateTime
}