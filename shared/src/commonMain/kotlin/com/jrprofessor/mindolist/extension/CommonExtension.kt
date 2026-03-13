package com.jrprofessor.mindolist.extension

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Instant

fun Duration.toDateFormat(): String {

    val instant = Instant.fromEpochMilliseconds(this.inWholeMilliseconds)

    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val day = dateTime.day.toString().padStart(2, '0')
    val month = dateTime.month.number.toString().padStart(2, '0')
    val year = dateTime.year

    return "$day/$month/$year"
}
fun Long.toTimeFormat(): String {

    val instant = Instant.fromEpochMilliseconds(this)

    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')

    return "$hour:$minute"
}