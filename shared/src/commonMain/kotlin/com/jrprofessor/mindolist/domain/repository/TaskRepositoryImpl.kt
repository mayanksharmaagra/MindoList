package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.extension.toDayMonthYearLabel
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.utils.Logger
import com.jrprofessor.mindolist.utils.NetworkConnectivityManager
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


// dueDate range calculate karo
fun LocalDate.toStartOfDayMillis(): Long =
    this.atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

fun LocalDate.toEndOfDayMillis(): Long =
    this.plus(1, DateTimeUnit.DAY)
        .atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds() - 1

@OptIn(ExperimentalTime::class)
private fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()

open class TaskRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val networkConnectivityManager: NetworkConnectivityManager,
) : TaskRepository {

    private val userRef: DatabaseReference by lazy { firebaseDatabase.reference("users") }
    private val uid get() = firebaseAuth.currentUser?.uid ?: error("User not logged in")

    // tasks/{uid}/{taskId}
    private fun tasksRef() = firebaseDatabase.reference("tasks").child(uid)

    override fun getTasks(): Flow<Result<List<TaskModel>>> =
        callbackFlow {
            Logger.error { ">>> get data with date" }
        trySend(Result.Loading)

        val listener = tasksRef().valueEvents

        val job = launch {
            listener.collect { snapshot ->
                val tasks = snapshot.children.mapNotNull { child ->
                    runCatching {
                        child.value<TaskModel>()
                    }.onFailure { e ->
                        println(">>> parse error: ${e.message}")
                    }.getOrNull()
                }.sortedByDescending { it.createdAt }

                println(">>> tasks: ${tasks.size}")
                trySend(Result.Success(tasks))
            }
        }

        awaitClose { job.cancel() }
        }.catch { e ->
            Logger.debug {
            "Error fetching tasks: ${e.message}"
        }
            emit(Result.Error(e as Exception, e.message ?: "Failed to fetch tasks"))
    }

    override fun getTasksByDate(date: LocalDate): Flow<Result<List<TaskModel>>> = callbackFlow {

        trySend(Result.Loading)

        val startMillis = date.toStartOfDayMillis()
        val endMillis = date.toEndOfDayMillis()
        val listener = tasksRef()
            .orderByChild("dueDate")
            .startAt(startMillis.toDouble())
            .endAt(endMillis.toDouble()).valueEvents

        val job = launch {
            listener.collect { snapshot ->
                val tasks = snapshot.children.mapNotNull { child ->
                    runCatching {
                        child.value<TaskModel>()
                    }.onFailure { e ->
                        println(">>> parse error: ${e.message}")
                    }.getOrNull()
                }.sortedBy { it.dueDate }

                Logger.error {
                    ">>> selected date    ${date.toDayMonthYearLabel()} tasks: ${tasks.size}"
                }
                println(">>> selected date    ${date.toDayMonthYearLabel()} tasks: ${tasks.size}")
                trySend(Result.Success(tasks))
            }
        }

        awaitClose { job.cancel() }
    }.catch { e ->
        Logger.debug {
            "Error fetching tasks: ${e.message}"
        }
        emit(Result.Error(e as Exception, e.message ?: "Failed to fetch tasks"))
    }

    override fun getTasksInRange(startMillis: Long, endMillis: Long): Flow<Result<List<TaskModel>>> = callbackFlow {
        trySend(Result.Loading)

        val listener = tasksRef()
            .orderByChild("dueDate")
            .startAt(startMillis.toDouble())
            .endAt(endMillis.toDouble()).valueEvents

        val job = launch {
            listener.collect { snapshot ->
                val tasks = snapshot.children.mapNotNull { child ->
                    runCatching {
                        child.value<TaskModel>()
                    }.getOrNull()
                }.sortedBy { it.dueDate }

                trySend(Result.Success(tasks))
            }
        }

        awaitClose { job.cancel() }
    }.catch { e ->
        Logger.debug { "Error fetching tasks in range: ${e.message}" }
        emit(Result.Error(e as Exception, e.message ?: "Failed to fetch tasks"))
    }

    override suspend fun addTask(task: TaskModel): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val ref = tasksRef().push()
            val taskId = ref.key ?: return Result.Error(
                Exception("Failed to generate task ID"),
                "Failed to generate task ID",
            )
            val now = currentTimeMillis()
            val taskWithId = task.copy(
                id = taskId,
                userId = uid,
                createdAt = task.createdAt.takeIf { it > 0 } ?: now,
                updatedAt = now,
            )
            ref.setValue(taskWithId.toMap())
            Logger.debug { "Task saved successfully: ${taskWithId.id}" }
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error saving task to database" }
            Result.Error(e, "Failed to save task")
        }
    }

    private fun TaskModel.toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "userId" to userId,
        "title" to title,
        "description" to description,
        "dueDate" to dueDate,
        "priority" to priority,
        "category" to category,
        "reminderEnabled" to reminderEnabled,
        "reminderValue" to reminderValue,
        "duration" to duration,
        "isCompleted" to isCompleted,
        "isPinned" to isPinned,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt,
    )

    override suspend fun updateTask(task: TaskModel): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val updateMap = task.toMap().toMutableMap()
            updateMap["updatedAt"] = currentTimeMillis()
            
            tasksRef().child(task.id).updateChildren(updateMap)
            Logger.debug { "Task updated successfully: ${task.id}" }
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error updating task: ${e.message}" }
            Result.Error(e, "Failed to update task")
        }
    }

    override suspend fun markComplete(
        taskId: String,
        isCompleted: Boolean
    ): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val updateMap = mapOf(
                "isCompleted" to isCompleted,               // ← hardcoded nahi
                "updatedAt" to Clock.System.now().toEpochMilliseconds()
            )
            tasksRef().child(taskId).updateChildren(updateMap)
            Logger.debug { "Task marked $isCompleted: $taskId" }
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error marking task: ${e.message}" }
            Result.Error(e, "Failed to update task status")
        }
    }

    override suspend fun togglePin(taskId: String, isPinned: Boolean): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val updateMap = mapOf(
                "isPinned" to isPinned,
                "updatedAt" to Clock.System.now().toEpochMilliseconds()
            )
            tasksRef().child(taskId).updateChildren(updateMap)
            Logger.debug { "Task pin toggled $isPinned: $taskId" }
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error toggling pin: ${e.message}" }
            Result.Error(e, "Failed to update pin status")
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            tasksRef().child(taskId).removeValue()
            Logger.debug { "Task deleted successfully: $taskId" }
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error deleting task: ${e.message}" }
            Result.Error(e, "Failed to delete task")
        }
    }


}