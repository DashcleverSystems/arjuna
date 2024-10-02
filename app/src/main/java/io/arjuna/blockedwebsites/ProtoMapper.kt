package io.arjuna.blockedwebsites

import io.arjuna.proto.BlockedWebsites
import io.arjuna.proto.toProto
import java.util.UUID

fun BlockedWebsites.contains(website: BlockedWebsite): Boolean =
    this.websitesList.any { it.domain == website.mainDomain }

fun io.arjuna.proto.UUID.toUUID(): UUID = UUID.fromString(this.value)

fun BlockedWebsite.toProto(): io.arjuna.proto.BlockedWebsite =
    io.arjuna.proto.BlockedWebsite.newBuilder().apply {
        this.domain = this@toProto.mainDomain
        this.identifier = this@toProto.identifier.uuid.toProto()
    }.build()