package com.oguzhanaslann.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VoiceViewModel : ViewModel() {
    private val _voiceState = MutableStateFlow(VoiceSearchUIState.Idle)
    val voiceState: StateFlow<VoiceSearchUIState>
        get() = _voiceState

    val isListening = _voiceState.map { it.progress.isListening }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun loadHistory() {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.loading())
            delay(1000)
            _voiceState.value = _voiceState.value.copy(
                progress = Progress.none(),
                searchResults = null,
                historyResults = listOf(
                    SearchResult(
                        id = "1",
                        productName = "Product 1",
                        producerName = "Producer 1",
                        imageUrl = "https://via.placeholder.com/150",
                    ),

                    SearchResult(
                        id = "2",
                        productName = "Product 2",
                        producerName = "Producer 2",
                        imageUrl = "https://via.placeholder.com/150",
                    ),

                    SearchResult(
                        id = "3",
                        productName = "Product 3",
                        producerName = "Producer 3",
                        imageUrl = "https://via.placeholder.com/150",
                    ),
                )
            )


        }
    }

    fun startListening() {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.listening())
        }
    }

    fun cancelListening() {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.none())
        }
    }

    fun stopListening() {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.none())
        }
    }

    fun searchForText(voiceInput: String?) {
        viewModelScope.launch {
            _voiceState.value = _voiceState.value.copy(progress = Progress.loading())
            delay(1000)
            _voiceState.value = _voiceState.value.copy(
                progress = Progress.none(),
                searchResults = listOf(
                    SearchResult(
                        id = "1",
                        productName = "Product 1",
                        producerName = "Producer 1",
                        imageUrl = "https://via.placeholder.com/150",
                    ),

                    SearchResult(
                        id = "2",
                        productName = "Product 2",
                        producerName = "Producer 2",
                        imageUrl = "https://via.placeholder.com/150",
                    ),

                    SearchResult(
                        id = "3",
                        productName = "Product 3",
                        producerName = "Producer 3",
                        imageUrl = "https://via.placeholder.com/150",
                    ),
                )
            )
        }
    }


}
