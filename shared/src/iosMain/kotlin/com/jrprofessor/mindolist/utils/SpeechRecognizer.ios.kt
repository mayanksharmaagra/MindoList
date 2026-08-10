package com.jrprofessor.mindolist.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.AVFAudio.*
import platform.Foundation.*
import platform.Speech.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class IosSpeechToTextParser : SpeechToTextParser {
    private val _state = MutableStateFlow(SpeechToTextParserState())
    override val state: StateFlow<SpeechToTextParserState> = _state.asStateFlow()

    private var speechRecognizer: SFSpeechRecognizer? = null
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private val audioEngine = AVAudioEngine()

    override fun startListening(languageCode: String) {
        _state.update { it.copy(isSpeaking = true, spokenText = "", error = null) }

        SFSpeechRecognizer.requestAuthorization { status ->
            dispatch_async(dispatch_get_main_queue()) {
                when (status) {
                    SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized -> {
                        AVAudioSession.sharedInstance().requestRecordPermission { granted: Boolean ->
                            dispatch_async(dispatch_get_main_queue()) {
                                if (granted) {
                                    startRecognition(languageCode)
                                } else {
                                    handleError("Microphone permission denied")
                                }
                            }
                        }
                    }
                    else -> handleError("Speech recognition permission denied")
                }
            }
        }
    }

    private fun startRecognition(languageCode: String) {
        try {
            val locale = NSLocale(languageCode)
            speechRecognizer = SFSpeechRecognizer(locale)
            
            recognitionTask?.cancel()
            recognitionTask = null

            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryRecord, null)
            audioSession.setMode(AVAudioSessionModeMeasurement, null)
            audioSession.setActive(true, null)

            recognitionRequest = SFSpeechAudioBufferRecognitionRequest().apply {
                shouldReportPartialResults = true
            }

            val inputNode = audioEngine.inputNode
            val recordingFormat = inputNode.outputFormatForBus(0u)
            
            inputNode.removeTapOnBus(0u)
            inputNode.installTapOnBus(0u, 1024u, recordingFormat) { buffer: AVAudioPCMBuffer?, _: AVAudioTime? ->
                buffer?.let { recognitionRequest?.appendAudioPCMBuffer(it) }
            }

            audioEngine.prepare()
            audioEngine.startAndReturnError(null)

            recognitionTask = speechRecognizer?.recognitionTaskWithRequest(recognitionRequest!!, resultHandler = { result, error ->
                dispatch_async(dispatch_get_main_queue()) {
                    if (error != null) {
                        handleError(error.localizedDescription)
                        return@dispatch_async
                    }
                    
                    result?.let {
                        val text = it.bestTranscription.formattedString
                        _state.update { s -> s.copy(spokenText = text) }
                        
                        if (it.final) {
                            stopListening()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            handleError("Speech engine failed: ${e.message}")
        }
    }

    override fun stopListening() {
        _state.update { it.copy(isSpeaking = false) }
        
        if (audioEngine.running) {
            audioEngine.stop()
            audioEngine.inputNode.removeTapOnBus(0u)
        }
        
        recognitionRequest?.endAudio()
        recognitionRequest = null
        recognitionTask?.cancel()
        recognitionTask = null
    }

    private fun handleError(message: String) {
        _state.update { it.copy(isSpeaking = false, error = message) }
        stopListening()
    }
}
