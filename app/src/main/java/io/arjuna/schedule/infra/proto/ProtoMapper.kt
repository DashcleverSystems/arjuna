package io.arjuna.schedule.infra.proto

import io.arjuna.proto.toJavaUUID
import io.arjuna.proto.toProto
import io.arjuna.schedule.domain.Hour
import io.arjuna.schedule.domain.Minute
import io.arjuna.schedule.domain.Schedule
import io.arjuna.schedule.domain.Weekday
import io.arjuna.schedule.domain.with
import io.arjuna.websites.Website
import io.arjuna.proto.Schedule as ScheduleProto
import io.arjuna.proto.Website as WebsiteProto
import io.arjuna.proto.Weekday as ProtoWeekday

fun ProtoWeekday.toDomain(): Weekday = Weekday.valueOf(this.name)

fun Weekday.toProto(): ProtoWeekday = ProtoWeekday.valueOf(this.name)

private fun WebsiteProto.toDomain(): Website = Website(Website.Id(identifier.toJavaUUID()), domain)

private fun Website.toProto(): WebsiteProto = WebsiteProto.newBuilder()
    .apply {
        identifier = this@toProto.identifier.uuid.toProto()
        domain = this@toProto.mainDomain
    }
    .build()

fun ScheduleProto.toDomain(): Schedule {
    return Schedule(
        identifier = Schedule.Id(this.identifier.toJavaUUID()),
        name = this.name,
        websites = this.websitesList.map { it.toDomain() }.toSet(),
        onDays = this.onDaysList.map { it.toDomain() }.toSet(),
        from = this.fromHour.Hour() with this.fromMinute.Minute(),
        to = this.toHour.Hour() with this.toMinute.Minute(),
    )
}

fun Schedule.toProto(): ScheduleProto {
    return ScheduleProto.newBuilder().apply {
        identifier = this@toProto.identifier.uuid.toProto()
        name = this@toProto.name
        fromHour = this@toProto.from.hour.value
        fromMinute = this@toProto.from.minute.value
        toHour = this@toProto.to.hour.value
        toMinute = this@toProto.to.minute.value
        addAllWebsites(this@toProto.websites.map { it.toProto() })
        addAllOnDays(this@toProto.onDays.map { it.toProto() })
    }.build()
}