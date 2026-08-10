package com.jrprofessor.mindolist.utils

import com.jrprofessor.mindolist.model.ReminderOption

object ReminderUtils {
    /**
     * Calculates the trigger time for a reminder.
     * @param dueDateMillis The due date of the task in epoch milliseconds.
     * @param reminderOption The selected reminder option.
     * @return The trigger time in epoch milliseconds.
     */
    fun calculateTriggerTime(dueDateMillis: Long, reminderOption: ReminderOption): Long {
        if (dueDateMillis == 0L) return 0L
        return dueDateMillis - (reminderOption.minutes * 60000L)
    }

    /**
     * Finds the ReminderOption matching the given label.
     */
    fun getOptionFromLabel(label: String): ReminderOption {
        return ReminderOption.entries.find { it.label == label } ?: ReminderOption.FIFTEEN_MINUTES
    }
}
