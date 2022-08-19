package com.oguzhanaslann.common

sealed class SearchType {
    object TextSearch : SearchType()
    object VoiceSearch : SearchType()
    sealed class CameraSearch: SearchType() {
        object ImageSearch : CameraSearch()
        object QRScanSearch : CameraSearch()

        fun isQRScanSearch(): Boolean = this is CameraSearch.QRScanSearch
    }
}
