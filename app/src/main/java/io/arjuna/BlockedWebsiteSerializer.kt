package io.arjuna

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import io.arjuna.proto.BlockedWebsites
import java.io.InputStream
import java.io.OutputStream

object BlockedWebsitesSerializer : Serializer<BlockedWebsites> {

    override val defaultValue = BlockedWebsites.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): BlockedWebsites {
        try {
            return BlockedWebsites.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: BlockedWebsites, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.blockedWebsitesStore: DataStore<BlockedWebsites> by dataStore(
    fileName = "blockedWebsites.pb",
    serializer = BlockedWebsitesSerializer
)