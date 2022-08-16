package com.oguzhanaslann.camera

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(

) : ViewModel() {
    var scanState by mutableStateOf<ScanState>(ScanState.Idle)

    var imageCapture by  mutableStateOf<ImageCapture?>(null)

    private  val scanType =  mutableStateOf(ScanType.QR)

    fun setScanType(scanType: ScanType) {
        imageCapture = null // reset image capture
        this.scanType.value = scanType
    }

    fun getScanType(): ScanType {
        return scanType.value
    }

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
enum class ScanType {
    QR, Image
}
