package io.arjuna.proto

fun UUID.toJavaUUID(): java.util.UUID = java.util.UUID.fromString(this.value)

fun java.util.UUID.toProto(): UUID = with(UUID.newBuilder()) {
    this.value = this@toProto.toString()
    build()
}
