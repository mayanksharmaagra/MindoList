package com.jrprofessor.mindolist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform