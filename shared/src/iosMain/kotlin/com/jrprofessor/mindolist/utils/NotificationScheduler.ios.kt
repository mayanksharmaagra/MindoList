package com.jrprofessor.mindolist.utils

import com.jrprofessor.mindolist.model.TaskModel
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNNotificationSound
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

class IosNotificationScheduler : NotificationScheduler {

    override fun scheduleNotification(task: TaskModel) {
        if (!task.reminderEnabled || task.dueDate == 0L || task.isCompleted) return

        val reminderOption = ReminderUtils.getOptionFromLabel(task.reminderValue)
        val triggerTimeMillis = ReminderUtils.calculateTriggerTime(task.dueDate, reminderOption)
        
        val nowMillis = (NSDate().timeIntervalSince1970 * 1000).toLong()
        val delaySeconds = (triggerTimeMillis - nowMillis) / 1000.0
        
        if (delaySeconds <= 0) return

        val content = UNMutableNotificationContent().apply {
            setTitle(task.title)
            setBody(task.description)
            setSound(UNNotificationSound.defaultSound())
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(delaySeconds, false)
        val request = UNNotificationRequest.requestWithIdentifier(task.id, content, trigger)

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error scheduling notification: ${error.localizedDescription}")
            }
        }
    }

    override fun cancelNotification(taskId: String) {
        UNUserNotificationCenter.currentNotificationCenter().removePendingNotificationRequestsWithIdentifiers(listOf(taskId))
    }
}
