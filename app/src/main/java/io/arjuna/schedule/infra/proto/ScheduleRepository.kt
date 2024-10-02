package io.arjuna.schedule.infra.proto

import androidx.datastore.core.DataStore
import io.arjuna.proto.Schedules
import io.arjuna.proto.UUID
import io.arjuna.proto.toJavaUUID
import io.arjuna.proto.toProto
import io.arjuna.schedule.domain.Schedule
import io.arjuna.schedule.domain.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import io.arjuna.proto.Schedules as SchedulesProto

class ScheduleRepository(
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore<SchedulesProto>
) : ScheduleRepository {

    override fun findAll(): Flow<Set<Schedule>> {
        return dataStore.data.map { proto: SchedulesProto ->
            proto.schedulesList.mapTo(mutableSetOf()) { it.toDomain() }
        }
    }

    override fun remove(schedule: Schedule) {
        coroutineScope.launch {
            dataStore.updateData { current: SchedulesProto ->
                val schedules = current.schedulesList.filter {
                    it.identifier.toJavaUUID() == schedule.identifier.uuid
                }
                val updatedSchedules = current.toBuilder()
                    .clearSchedules()
                    .addAllSchedules(schedules)
                    .build()
                updatedSchedules
            }
        }
    }

    override fun save(schedule: Schedule) {
        coroutineScope.launch {
            dataStore.updateData { schedules: SchedulesProto ->
                val scheduleToUpdate = if (schedules.contains(schedule))
                    schedules.getById(schedule.identifier.uuid.toProto())
                else
                    null
                val currentSchedules = schedules.schedulesList
                val newSchedules = scheduleToUpdate?.let {
                    currentSchedules - it + schedule.toProto()
                } ?: (currentSchedules + schedule.toProto())
                val updatedSchedules = SchedulesProto.newBuilder()
                    .addAllSchedules(newSchedules)
                updatedSchedules.build()
            }
        }
    }

    private fun Schedules.contains(schedule: Schedule): Boolean {
        return this.schedulesList.any { it.identifier.toJavaUUID() == schedule.identifier.uuid }
    }

    private fun Schedules.getById(id: UUID): io.arjuna.proto.Schedule {
        return this.schedulesList.find { it.identifier == id } ?: error("No schedule of $id")
    }
}
