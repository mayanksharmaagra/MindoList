package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.domain.model.UnauthorizedException
import com.jrprofessor.mindolist.model.GoogleItem
import com.jrprofessor.mindolist.model.GoogleItemType
import com.jrprofessor.mindolist.utils.Logger
import com.jrprofessor.mindolist.utils.NetworkConnectivityManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
internal data class GoogleCalendarResponse(val items: List<CalendarItem>? = null)

@Serializable
internal data class CalendarItem(
    val id: String,
    val summary: String? = null,
    val description: String? = null,
    val start: CalendarTime? = null,
    val reminders: CalendarReminders? = null
)

@Serializable
internal data class CalendarTime(val dateTime: String? = null, val date: String? = null)

@Serializable
internal data class CalendarReminders(val overrides: List<ReminderOverride>? = null)

@Serializable
internal data class ReminderOverride(val method: String, val minutes: Int)

@Serializable
internal data class GoogleTasksResponse(val items: List<TaskItem>? = null)

@Serializable
internal data class TaskItem(
    val id: String,
    val title: String? = null,
    val notes: String? = null,
    val due: String? = null,
    val status: String? = null
)

@Serializable
internal data class TaskListResponse(val items: List<TaskListItem>? = null)

@Serializable
internal data class TaskListItem(val id: String, val title: String? = null)

class GoogleCalendarRepositoryImpl(
    private val networkConnectivityManager: NetworkConnectivityManager
) : GoogleCalendarRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                isLenient = true
            })
        }
    }

    override suspend fun fetchAll(accessToken: String): List<GoogleItem> = withContext(Dispatchers.IO) {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return@withContext emptyList()
        }
        val events = fetchCalendarEvents(accessToken)
        val tasks = fetchTasks(accessToken)
        (events + tasks).sortedBy { it.dateTime }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun fetchCalendarEvents(accessToken: String): List<GoogleItem> {
        return try {
            val now = Clock.System.now()
            val timeZone = TimeZone.currentSystemDefault()
            val todayStart = now.toLocalDateTime(timeZone).date.atStartOfDayIn(timeZone)
            val monthLater = todayStart.plus(30, DateTimeUnit.DAY, timeZone)

            val response: HttpResponse =
                client.get("https://www.googleapis.com/calendar/v3/calendars/primary/events") {
                    header("Authorization", "Bearer $accessToken")
                    parameter("timeMin", todayStart.toString())
                    parameter("timeMax", monthLater.toString())
                    parameter("singleEvents", true)
                    parameter("orderBy", "startTime")
                }

            if (response.status.value !in 200..299) {
                val errorBody = response.bodyAsText()
                Logger.error { "Google Calendar API Error: ${response.status.value} - $errorBody" }
                if (response.status.value == 401) {
                    throw UnauthorizedException("Google Calendar API Unauthorized")
                }
                return emptyList()
            }

            val bodyResponse = response.body<GoogleCalendarResponse>()
            Logger.debug { "Google Calendar Response Status: ${response.status}" }

            bodyResponse.items?.map { item ->
                Logger.error {
                    "Google Calendar Events: " + item
                }
                val dateTime = item.start?.dateTime?.let {
                    Instant.parse(it).toLocalDateTime(TimeZone.currentSystemDefault())
                } ?: item.start?.date?.let {
                    // All-day event: parse "YYYY-MM-DD" and set to start of day
                    LocalDate.parse(it).atStartOfDay()
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
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: Exception) {
            Logger.error { "Exception fetching calendar events: ${e.message}" }
            emptyList()
        }
    }

    private suspend fun fetchTasks(accessToken: String): List<GoogleItem> {
        return try {
            // 1. Get all task lists
            val listsResponse: HttpResponse =
                client.get("https://www.googleapis.com/tasks/v1/users/@me/lists") {
                    header("Authorization", "Bearer $accessToken")
                }

            if (listsResponse.status.value !in 200..299) {
                val errorBody = listsResponse.bodyAsText()
                Logger.error { "Google Tasks API Error (List): ${listsResponse.status.value} - $errorBody" }
                if (listsResponse.status.value == 401) {
                    throw UnauthorizedException("Google Tasks API Unauthorized")
                }
                return emptyList()
            }

            val bodyResponse = listsResponse.body<TaskListResponse>()
            Logger.debug { "Google Tasks List Response Status: ${listsResponse.status}" }

            val allItems = mutableListOf<GoogleItem>()

            // 2. Fetch tasks for each list
            bodyResponse.items?.forEach { taskList ->
                val response: HttpResponse =
                    client.get("https://www.googleapis.com/tasks/v1/lists/${taskList.id}/tasks") {
                        header("Authorization", "Bearer $accessToken")
                        parameter("showCompleted", false)
                        parameter("showHidden", false)
                    }

                if (response.status.value in 200..299) {
                    val taskBody = response.body<GoogleTasksResponse>()
                    taskBody.items?.map { item ->
                        Logger.error {
                            "Google Calendar tasks: " + item
                        }
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
                    }?.let { allItems.addAll(it) }
                } else {
                    val errorBody = response.bodyAsText()
                    Logger.error { "Google Tasks API Error (Tasks): ${response.status.value} for list ${taskList.id} - $errorBody" }
                }
            }

            allItems
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: Exception) {
            Logger.error { "Exception fetching tasks: ${e.message}" }
            emptyList()
        }
    }

    private fun LocalDate.atStartOfDay(): LocalDateTime {
        return LocalDateTime(this.year, this.month, this.dayOfMonth, 0, 0)
    }
}
