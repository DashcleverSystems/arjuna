package io.arjuna.locktime

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import io.arjuna.proto.WebsiteLocks
import java.io.InputStream
import java.io.OutputStream


object WebsiteLockSerializer : Serializer<WebsiteLocks> {
    override val defaultValue: WebsiteLocks =
        WebsiteLocks.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): WebsiteLocks {
        try {
            return WebsiteLocks.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto. Blocked websites data", exception)
        }
    }

    override suspend fun writeTo(t: WebsiteLocks, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.websiteLocksStore: DataStore<WebsiteLocks> by dataStore(
    fileName = "websiteLocks.pb",
    serializer = WebsiteLockSerializer
)