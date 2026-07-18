package com.jrprofessor.mindolist.domain.repository

import com.google.firebase.ai.FirebaseAI
import com.jrprofessor.mindolist.model.ParsedTask
import com.jrprofessor.mindolist.utils.Logger
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

class AiTaskRepositoryImpl : AiTaskRepository {

    private val model = FirebaseAI.instance
        .generativeModel("gemini-3.1-flash-lite") // gemini-1.5-flash is shut down (404) — use a current model

    override suspend fun parseReminderText(userInput: String): ParsedTask {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val today = "${now.day.toString().padStart(2, '0')}/${now.monthNumber.toString().padStart(2, '0')}/${now.year}"
        val currentTime = "${if (now.hour % 12 == 0) 12 else now.hour % 12}:${now.minute.toString().padStart(2, '0')} ${if (now.hour >= 12) "PM" else "AM"}"

        val prompt = """
            Today's date is $today and current time is $currentTime.
            You are a helpful assistant that extracts task details from natural language.
            The input may be in English, Hindi, or Romanized Hindi (like 'kal subah').

            Extract the following details and respond ONLY with a valid JSON object, no markdown, no code fences, no explanation.

            Rules:
            - title: short, clear action in English (max 6 words), strip filler like "remind me to"
            - description: extra context in English, empty string "" if none
            - date: resolve relative dates ("kal", "tomorrow", "next Monday") into "DD/MM/YYYY" relative to $today. Use null only if no date/time reference exists at all.
            - time: resolve into 12-hour format "hh:mm AM/PM" (e.g., 09:00 AM, 11:30 PM). 
              * Use the current time ($currentTime) to infer AM/PM if not specified. 
              * If the specified time has already passed today, assume the user means the next occurrence (e.g., if it's 10:00 PM and user says '11:30', they mean '11:30 PM').
              * If a date is mentioned but no time, use null. If time is mentioned but no date, assume today.
            - importance: infer from urgency words. "urgent"/"asap"/"important"/"zaroori" → "High". "whenever"/"low priority"/"fursat mein" → "Low". No urgency signal → "Medium".
            - category: choose exactly one: "Work", "Personal", or "Shopping"
              - "Work": meetings, deadlines, emails, colleagues, projects, client calls
              - "Shopping": buy, purchase, order, pick up, grocery, store, kharidna
              - "Personal": everything else (family, health, errands, self-care)

            JSON structure:
            {
              "title": "...",
              "description": "...",
              "date": "DD/MM/YYYY or null (Example: 17/07/2026)",
              "time": "hh:mm AM/PM or null (Example: 08:30 AM, 11:30 PM)",
              "importance": "High|Medium|Low",
              "category": "Work|Personal|Shopping"
            }

            Input: "$userInput"
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            val responseText = response.text ?: throw Exception("Empty response from AI")

            Logger.debug { "AI Response: $responseText" }

            // Extract JSON using Regex in case AI adds markdown or extra text
            val jsonRegex = """\{[\s\S]*\}""".toRegex()
            val jsonMatch = jsonRegex.find(responseText)?.value
                ?: throw Exception("Could not find JSON in response")

            json.decodeFromString<ParsedTask>(jsonMatch)
        } catch (e: Exception) {
            Logger.error { "AI Parsing Error: ${e.message}" }
            throw e
        }
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
}