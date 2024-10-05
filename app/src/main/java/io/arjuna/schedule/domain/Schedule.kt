package io.arjuna.schedule.domain

import io.arjuna.websites.Website
import java.util.UUID


class Schedule(
    var name: String = "Schedule lock",
    val identifier: Id = Id(),
    var websites: Set<Website>,
    var onDays: Set<Weekday>,
    var from: Time,
    var to: Time
) {

    data class Id(val uuid: UUID = UUID.randomUUID())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Schedule

        return identifier == other.identifier
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}