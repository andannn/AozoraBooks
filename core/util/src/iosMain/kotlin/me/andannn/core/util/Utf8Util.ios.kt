package me.andannn.core.util

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import platform.Foundation.NSData
import platform.Foundation.NSShiftJISStringEncoding
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataWithBytes

/**
 * read file as string from path.
 */
actual fun Path.readString(charset: String): String {
    val decoder = charsetToDecoderMap[charset] ?: error("no available decoder")
    val source = SystemFileSystem.source(this).buffered().readByteArray()
    return source.toNSData().string(decoder) ?: error("failed to read string")
}

private val charsetToDecoderMap = mapOf(
    "UTF_8" to NSUTF8StringEncoding,
    "Shift_JIS" to NSShiftJISStringEncoding,
)

@OptIn(BetaInteropApi::class)
private fun NSData.string(charset: ULong): String? {
    return NSString.create(this, charset) as String?
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = usePinned {
    NSData.dataWithBytes(it.addressOf(0), size.toULong())
}

