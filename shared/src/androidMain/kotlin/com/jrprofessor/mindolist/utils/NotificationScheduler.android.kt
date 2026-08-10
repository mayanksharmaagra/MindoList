package com.jrprofessor.mindolist.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jrprofessor.mindolist.model.ReminderOption
import com.jrprofessor.mindolist.model.TaskModel

class AndroidNotificationScheduler(
    private val context: Context
) : NotificationScheduler {

    override fun scheduleNotification(task: TaskModel) {
        // Cancel existing first to be safe
        cancelNotification(task.id)

        if (!task.reminderEnabled || task.dueDate == 0L || task.isCompleted) return

        val reminderOption = ReminderUtils.getOptionFromLabel(task.reminderValue)
        val triggerTime = ReminderUtils.calculateTriggerTime(task.dueDate, reminderOption)
        
        val now = System.currentTimeMillis()
        if (triggerTime <= now) {
            // If the reminder time has already passed but the task is still in the future,
            // we could potentially fire it now, but usually it's better to just skip.
            // However, if the task is "High" priority, maybe we fire a "Late" reminder?
            // For now, let's just stick to future only.
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "com.jrprofessor.mindolist.ACTION_TASK_REMINDER"
            putExtra("TASK_ID", task.id)
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_DESCRIPTION", task.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            // Fallback for security exceptions or other issues
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    override fun cancelNotification(taskId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "com.jrprofessor.mindolist.ACTION_TASK_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
