package com.jrprofessor.mindolist.utils

import dev.gitlive.firebase.storage.Data
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.create
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun ByteArray.toStorageData(): Data {
    val nsData = this.usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }
    return Data(nsData)
}