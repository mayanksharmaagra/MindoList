package com.jrprofessor.mindolist.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.domain.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RebootBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val taskRepository: TaskRepository by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskRepository.getTasks().first()
                if (result is Result.Success<*>) {
                    val tasks = result.data as? List<*> ?: return@launch
                    tasks.forEach { task ->
                        if (task is com.jrprofessor.mindolist.model.TaskModel) {
                            if (task.reminderEnabled && !task.isCompleted) {
                                notificationScheduler.scheduleNotification(task)
                            }
                        }
                    }
                }
            }
        }
    }
}
