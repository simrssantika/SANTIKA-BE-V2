package com.santika.simrs.global.utils

import java.nio.ByteBuffer
import java.util.UUID

fun ByteArray.toUUID(): UUID {
    val bb = ByteBuffer.wrap(this)
    return UUID(bb.long, bb.long)
}
