package com.oguzhanaslann.voice

data class Progress(
    val isListening: Boolean,
    val isLoading: Boolean
) {

    fun isNone(): Boolean = !isListening && !isLoading

    init {
        require(!(isListening && isLoading)) { "Cannot have both listening and loading" }
    }

    companion object {
        fun none() = Progress(isListening = false, isLoading = false)
        fun loading() = Progress(isListening = false, isLoading = true)
        fun listening() = Progress(isListening = true, isLoading = false)
    }
}
