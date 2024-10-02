package io.arjuna.schedule.domain

import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    fun findAll(): Flow<Set<Schedule>>

    fun remove(schedule: Schedule)

    fun save(schedule: Schedule)
}