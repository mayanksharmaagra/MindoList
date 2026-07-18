package com.jrprofessor.mindolist

interface Platform {
    val name: String
    val isAndroid: Boolean
}

expect fun getPlatform(): Platform