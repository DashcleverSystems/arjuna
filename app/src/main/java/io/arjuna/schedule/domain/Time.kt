package io.arjuna.schedule.domain

data class Hour(val value: Int) {

    init {
        require(value in 0..24) {
            "$value. Hour must be between 0 and 24. Only 24 hour clock format is used!"
        }
    }
}

data class Minute(val value: Int) {

    init {
        require(value in 0..59) {
            "$value. Minute must be between 0 and 59!"
        }
    }
}

data class Time(val hour: Hour, val minute: Minute)

fun Int.Hour() = Hour(this)

fun Int.Minute() = Minute(this)

infix fun Hour.with(minute: Minute): Time = Time(this, minute)
