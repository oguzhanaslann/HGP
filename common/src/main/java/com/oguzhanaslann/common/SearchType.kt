package com.oguzhanaslann.common

sealed class SearchType {
    object TextSearch
    object VoiceSearch
    sealed class CameraSearch: SearchType() {
        object ImageSearch : CameraSearch()
        object QRScanSearch : CameraSearch()
    }
}
