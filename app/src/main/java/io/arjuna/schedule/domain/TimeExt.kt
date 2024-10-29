package io.arjuna.schedule.domain

import java.time.LocalTime

infix fun LocalTime.isWithin(fromTo: Pair<Time, Time>): Boolean {
    val (from, to) = fromTo
    val startTime = LocalTime.of(from.hour.value, from.minute.value)
    val endTime = LocalTime.of(to.hour.value, to.minute.value)

    // Check if currentTime falls within the range
    return if (startTime <= endTime) {
        // Normal range, start is before end (e.g., 08:00 to 18:00)
        this in startTime..endTime
    } else {
        // Overnight range, start is after end (e.g., 22:00 to 06:00)
        this >= startTime || this <= endTime
    }
}