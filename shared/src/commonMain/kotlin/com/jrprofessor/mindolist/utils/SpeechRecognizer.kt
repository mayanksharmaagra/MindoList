package com.jrprofessor.mindolist.utils

import kotlinx.coroutines.flow.StateFlow

interface SpeechToTextParser {
    val state: StateFlow<SpeechToTextParserState>
    fun startListening(languageCode: String = "en-US")
    fun stopListening()
}

data class SpeechToTextParserState(
    val isSpeaking: Boolean = false,
    val spokenText: String = "",
    val error: String? = null
)
