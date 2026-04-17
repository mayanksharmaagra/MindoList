package com.jrprofessor.mindolist.utils

// androidMain/kotlin/utils/StorageUtils.kt
import dev.gitlive.firebase.storage.Data

actual fun ByteArray.toStorageData(): Data = Data(this)