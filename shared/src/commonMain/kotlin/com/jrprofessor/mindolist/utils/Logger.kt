package com.jrprofessor.mindolist.utils

import io.github.aakira.napier.Napier

object Logger {
    fun debug(tag: String = "MindoList", message: () -> String) {
        Napier.d(tag = tag, message = message)
    }

    fun error(throwable: Throwable? = null, tag: String = "MindoList", message: () -> String) {
        Napier.e(tag = tag, throwable = throwable, message = message)
    }
}