package io.arjuna.websites

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import io.arjuna.proto.Websites
import java.io.InputStream
import java.io.OutputStream

object WebsitesSerializer : Serializer<Websites> {

    override val defaultValue: Websites = Websites.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Websites {
        try {
            return Websites.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto. Blocked websites data", exception)
        }
    }

    override suspend fun writeTo(t: Websites, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.blockedWebsitesStore: DataStore<Websites> by dataStore(
    fileName = "blockedWebsites.pb",
    serializer = WebsitesSerializer
)