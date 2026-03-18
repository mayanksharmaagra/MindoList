package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.model.TaskModel
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import io.github.oshai.kotlinlogging.KotlinLogging
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


private val logger = KotlinLogging.logger {

}

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
) : TaskRepository {

    private val userRef: DatabaseReference = firebaseDatabase.reference("users")
    private val uid get() = firebaseAuth.currentUser?.uid ?: error("User not logged in")

    // tasks/{uid}/{taskId}
    private fun tasksRef() = firebaseDatabase.reference("tasks").child(uid)

    override fun getTasks(): Flow<Result<List<TaskModel>>> = callbackFlow {
        println(">>>    $uid")
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
        logger.debug {
            "Error fetching tasks: ${e.message}"
        }
        emit(Result.Error(e as Exception, e.message ?: "Failed to fetch tasks"))
    }

    override fun getTasksByDate(date: LocalDate): Flow<Result<List<TaskModel>>> = callbackFlow {
        println(">>>    $uid")
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

                println(">>> tasks: ${tasks.size}")
                trySend(Result.Success(tasks))
            }
        }

        awaitClose { job.cancel() }
    }.catch { e ->
        logger.debug {
            "Error fetching tasks: ${e.message}"
        }
        emit(Result.Error(e as Exception, e.message ?: "Failed to fetch tasks"))
    }

    override suspend fun addTask(task: TaskModel): Result<Unit> {
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
                createdAt = now,
                updatedAt = now,
            )
            ref.setValue(taskWithId.toMap())
            logger.debug { "Task saved successfully: ${taskWithId.id}" }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(e) { "Error saving task to database" }
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
        "isCompleted" to isCompleted,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt,
    )

    override suspend fun updateTask(task: TaskModel): Result<Unit> {
        return try {
            val updatedMap = task.toMap().toMutableMap().apply {
                put("isCompleted", true)
                put("updatedAt", Clock.System.now().toEpochMilliseconds())
            }
            tasksRef().child(task.id).updateChildren(updatedMap)
            logger.debug { "Task Updated successfully" }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(e) { "Error updating task to database" }
            Result.Error(e, "Failed to update task")
        }
    }

    override suspend fun markComplete(
        taskId: String,
        isCompleted: Boolean
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        TODO("Not yet implemented")
    }


}