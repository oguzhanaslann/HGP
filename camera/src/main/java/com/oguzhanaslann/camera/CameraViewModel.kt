package com.oguzhanaslann.camera

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(

) : ViewModel() {
    var scanState by mutableStateOf<ScanState>(ScanState.Idle)

    fun onImageCapturing() {
        scanState = ScanState.Scanning
    }

    fun onImageCapture(savedUri: Uri?) {
        scanState = ScanState.ImagePreview(savedUri)
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
