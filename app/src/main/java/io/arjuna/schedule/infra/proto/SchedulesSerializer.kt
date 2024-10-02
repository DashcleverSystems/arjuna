package io.arjuna.schedule.infra.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import io.arjuna.proto.Schedules
import java.io.InputStream
import java.io.OutputStream

object SchedulesSerializer : Serializer<Schedules> {

    override val defaultValue: Schedules = Schedules.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Schedules {
        try {
            return Schedules.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto. Schedules data", exception)
        }
    }

    override suspend fun writeTo(t: Schedules, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.schedulesStore: DataStore<Schedules> by dataStore(
    fileName = "schedules.pb",
    serializer = SchedulesSerializer
)