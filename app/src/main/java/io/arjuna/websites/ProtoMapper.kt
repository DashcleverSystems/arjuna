package io.arjuna.websites

import io.arjuna.proto.Websites
import io.arjuna.proto.toProto
import java.util.UUID

fun Websites.contains(website: Website): Boolean =
    this.websitesList.any { it.domain == website.mainDomain }

fun io.arjuna.proto.UUID.toUUID(): UUID = UUID.fromString(this.value)

fun Website.toProto(): io.arjuna.proto.Website =
    io.arjuna.proto.Website.newBuilder().apply {
        this.domain = this@toProto.mainDomain
        this.identifier = this@toProto.identifier.uuid.toProto()
    }.build()