package com.jrprofessor.mindolist.domain

import com.jrprofessor.mindolist.model.GoogleItem
import com.jrprofessor.mindolist.model.GoogleItemType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GoogleCalendarResponse(val items: List<CalendarItem>? = null)

@Serializable
data class CalendarItem(
    val id: String,
    val summary: String? = null,
    val description: String? = null,
    val start: CalendarTime? = null,
    val reminders: CalendarReminders? = null
)

@Serializable
data class CalendarTime(val dateTime: String? = null, val date: String? = null)

@Serializable
data class CalendarReminders(val overrides: List<ReminderOverride>? = null)

@Serializable
data class ReminderOverride(val method: String, val minutes: Int)

@Serializable
data class GoogleTasksResponse(val items: List<TaskItem>? = null)

@Serializable
data class TaskItem(
    val id: String,
    val title: String? = null,
    val notes: String? = null,
    val due: String? = null,
    val status: String? = null
)

class GoogleCalendarRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    suspend fun fetchAll(accessToken: String): List<GoogleItem> {
        val events = fetchCalendarEvents(accessToken)
        val tasks = fetchTasks(accessToken)
        return (events + tasks).sortedBy { it.dateTime }
    }

    private suspend fun fetchCalendarEvents(accessToken: String): List<GoogleItem> {
        return try {
            val response: GoogleCalendarResponse = client.get("https://www.googleapis.com/calendar/v3/calendars/primary/events") {
                header("Authorization", "Bearer $accessToken")
            }.body()

            response.items?.map { item ->
                val dateTime = item.start?.dateTime?.let {
                    Instant.parse(it).toLocalDateTime(TimeZone.currentSystemDefault())
                }
                val isReminder = item.reminders?.overrides?.isNotEmpty() == true

                GoogleItem(
                    id = item.id,
                    title = item.summary ?: "No Title",
                    description = item.description,
                    dateTime = dateTime,
                    type = if (isReminder) GoogleItemType.CALENDAR_REMINDER else GoogleItemType.CALENDAR_EVENT
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchTasks(accessToken: String): List<GoogleItem> {
        return try {
            val response: GoogleTasksResponse = client.get("https://www.googleapis.com/tasks/v1/lists/@default/tasks") {
                header("Authorization", "Bearer $accessToken")
                parameter("showCompleted", false)
                parameter("showHidden", false)
            }.body()

            response.items?.map { item ->
                val dateTime = item.due?.let {
                    Instant.parse(it).toLocalDateTime(TimeZone.currentSystemDefault())
                }
                GoogleItem(
                    id = item.id,
                    title = item.title ?: "Untitled Task",
                    description = item.notes,
                    dateTime = dateTime,
                    type = GoogleItemType.TASK,
                    isCompleted = item.status == "completed"
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
