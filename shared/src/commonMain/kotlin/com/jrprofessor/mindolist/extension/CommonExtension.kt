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

    val hour24 = dateTime.hour
    val minute = dateTime.minute

    val amPm = if (hour24 < 12) "AM" else "PM"

    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    val hour = hour12.toString().padStart(2, '0')
    val min = minute.toString().padStart(2, '0')

    return "$hour:$min $amPm"
}

// ── Long → "dd/MM/yyyy" ───────────────────────────────────────────────────────
fun Long.toDisplayDate(): String {
    val dateTime = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dateTime.dayOfMonth.toString().padStart(2, '0')}/" +
            "${dateTime.monthNumber.toString().padStart(2, '0')}/" +
            "${dateTime.year}"
}

// ── Long → "hh:mm AM/PM" ─────────────────────────────────────────────────────
fun Long.toDisplayTime(): String {
    val dateTime = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = dateTime.hour
    val minute = dateTime.minute.toString().padStart(2, '0')
    val period = if (hour >= 12) "PM" else "AM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }.toString().padStart(2, '0')
    return "$displayHour:$minute $period"
}