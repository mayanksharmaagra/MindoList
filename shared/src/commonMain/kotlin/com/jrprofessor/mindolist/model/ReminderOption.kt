package com.jrprofessor.mindolist.model

enum class ReminderOption(val label: String, val minutes: Int) {
    FIVE_MINUTES("5 minutes before", 5),
    TEN_MINUTES("10 minutes before", 10),
    FIFTEEN_MINUTES("15 minutes before", 15),
    THIRTY_MINUTES("30 minutes before", 30),
    ONE_HOUR("1 hour before", 60),
    TWO_HOURS("2 hours before", 120),
    ONE_DAY("1 day before", 1440),
}
