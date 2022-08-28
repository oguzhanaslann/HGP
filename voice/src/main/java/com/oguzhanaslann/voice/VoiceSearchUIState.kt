package com.oguzhanaslann.voice

data class VoiceSearchUIState(
    val progress: Progress = Progress.none(),
    val historyResults: List<SearchResult>? = null,
    val searchResults: List<SearchResult>? = null
) {

    fun isIdle(
        skipCurrentProgress: Boolean = false
    ) = (progress == Progress.none() && !skipCurrentProgress) && historyResults == null && searchResults == null
    fun isHistory() = historyResults != null && searchResults == null
    fun isSearch() =  searchResults != null //historyResults == null &&

    fun isListening() = progress.isListening
    fun isSearching() = progress.isLoading
    fun isNoProgress() = progress.isNone()

    companion object {
        val Idle = VoiceSearchUIState()
    }
}
