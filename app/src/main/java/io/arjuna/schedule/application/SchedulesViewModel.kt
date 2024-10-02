package io.arjuna.schedule.application

import androidx.lifecycle.ViewModel
import io.arjuna.blockedwebsites.BlockedWebsite
import io.arjuna.blockedwebsites.BlockedWebsiteRepository
import io.arjuna.schedule.domain.Schedule
import io.arjuna.schedule.infra.proto.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SchedulesViewModel(
    private val scheduleRepository: ScheduleRepository,
    websiteRepository: BlockedWebsiteRepository
) : ViewModel() {

    fun save(schedule: Schedule) = scheduleRepository.save(schedule)


    val schedules: Flow<Set<Schedule>> =
        scheduleRepository.findAll()

    fun findScheduleById(scheduleId: Schedule.Id): Flow<Schedule?> =
        scheduleRepository.findAll().map { schedules -> schedules.find { it.identifier == scheduleId } }

    val websites: Flow<Set<BlockedWebsite>> = websiteRepository.blockedWebsites
}