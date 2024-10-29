package io.arjuna.schedule.domain

import java.time.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY

infix fun DayOfWeek.isIn(days: Collection<Weekday>): Boolean {
    val day = this.toDomain()
    return days.contains(day)
}

private fun DayOfWeek.toDomain(): Weekday {
    return when (this) {
        MONDAY -> Weekday.MONDAY
        TUESDAY -> Weekday.TUESDAY
        WEDNESDAY -> Weekday.WEDNESDAY
        THURSDAY -> Weekday.THURSDAY
        FRIDAY -> Weekday.FRIDAY
        SATURDAY -> Weekday.SATURDAY
        SUNDAY -> Weekday.SUNDAY
    }
}