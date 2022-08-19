package com.oguzhanaslann.camera

import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.oguzhanaslann.common.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    var scanState by mutableStateOf<ScanState>(ScanState.Idle)

    var imageCapture by  mutableStateOf<ImageCapture?>(null)

    fun onImageCapturing() {
        scanState = ScanState.Scanning
    }

    fun onImageCapture(savedUri: Uri?) {
        scanState = ScanState.ImagePreview(savedUri)
    }

    fun onAnalyze(uri: Uri?) {
//        TODO("Not yet implemented")
    }

    fun onImageCaptureError(exception: ImageCaptureException) {
//        scanState = ScanState.Error(exception)
    }

    fun onIdle() {
        scanState = ScanState.Idle
    }

    fun onScan(imageProxy: ImageProxy) {
//        scanState = ScanState.Scanning
    }
}


sealed class ScanState {
    object Idle : ScanState()
    object Scanning : ScanState()
    data class Scanned(val productScanResult: ProductScanResult) : ScanState()
    data class ImagePreview(val uri: Uri?) : ScanState()
}
