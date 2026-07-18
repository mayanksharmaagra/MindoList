package com.jrprofessor.mindolist.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IosSpeechToTextParser : SpeechToTextParser {
    private val _state = MutableStateFlow(SpeechToTextParserState())
    override val state: StateFlow<SpeechToTextParserState> = _state.asStateFlow()

    override fun startListening(languageCode: String) {
        // Not implemented for iOS in this iteration
    }

    override fun stopListening() {
        // Not implemented for iOS in this iteration
    }
}
