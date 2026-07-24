package com.jrprofessor.mindolist.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AndroidSpeechToTextParser(
    private val context: Context
) : SpeechToTextParser, RecognitionListener {

    private val _state = MutableStateFlow(SpeechToTextParserState())
    override val state: StateFlow<SpeechToTextParserState> = _state.asStateFlow()

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    override fun startListening(languageCode: String) {
        _state.update { SpeechToTextParserState(isSpeaking = true) }

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.update {
                it.copy(
                    error = "Speech recognition is not available",
                    isSpeaking = false
                )
            }
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            // Add calling package to avoid ERROR_INSUFFICIENT_PERMISSIONS on some devices
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
    }

    override fun stopListening() {
        _state.update { it.copy(isSpeaking = false) }
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update { it.copy(error = null) }
    }

    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit
    override fun onEndOfSpeech() {
        _state.update { it.copy(isSpeaking = false) }
    }

    override fun onError(error: Int) {
        val message = when (error) {
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission denied. Check microphone settings."
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client-side error"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Speech recognition error: $error"
        }
        _state.update {
            it.copy(
                error = message,
                isSpeaking = false
            )
        }
    }

    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.getOrNull(0)?.let { text ->
            _state.update { it.copy(spokenText = text) }
        }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}
