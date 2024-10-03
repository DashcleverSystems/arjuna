package io.arjuna.schedule.application

import androidx.lifecycle.ViewModel
import io.arjuna.websites.Website
import io.arjuna.websites.WebsiteRepository
import io.arjuna.schedule.domain.Schedule
import io.arjuna.schedule.infra.proto.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SchedulesViewModel(
    private val scheduleRepository: ScheduleRepository,
    websiteRepository: WebsiteRepository
) : ViewModel() {

    fun save(schedule: Schedule) = scheduleRepository.save(schedule)


    val schedules: Flow<Set<Schedule>> =
        scheduleRepository.findAll()

    fun findScheduleById(scheduleId: Schedule.Id): Flow<Schedule?> =
        scheduleRepository.findAll().map { schedules -> schedules.find { it.identifier == scheduleId } }

    val websites: Flow<Set<Website>> = websiteRepository.websites
}