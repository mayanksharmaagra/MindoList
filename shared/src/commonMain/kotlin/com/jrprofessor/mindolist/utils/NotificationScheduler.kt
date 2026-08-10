package com.jrprofessor.mindolist.utils

import com.jrprofessor.mindolist.model.TaskModel

interface NotificationScheduler {
    fun scheduleNotification(task: TaskModel)
    fun cancelNotification(taskId: String)
}
