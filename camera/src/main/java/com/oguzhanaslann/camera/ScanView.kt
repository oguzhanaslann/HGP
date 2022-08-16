package com.oguzhanaslann.camera

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oguzhanaslann.commonui.LoadingView

@Composable
fun ScanView(
    cameraViewModel: CameraViewModel = viewModel()
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraView(
            modifier = Modifier.align(Alignment.Center),
            cameraViewModel = cameraViewModel
        )

        when (cameraViewModel.scanState) {
            ScanState.Idle -> Unit
            ScanState.Scanning -> LoadingView(
                modifier = Modifier.align(Alignment.Center)
            )
            is ScanState.Scanned -> CameraResultView(
                productScanResult = (cameraViewModel.scanState as ScanState.Scanned).productScanResult,
                onGoBackClicked = {
                    cameraViewModel.scanState = ScanState.Idle
                }
            )
            is ScanState.ImagePreview -> ImagePreviewView(
                previewUri = (cameraViewModel.scanState as ScanState.ImagePreview).uri.toString(),
                onCancel = {
                    cameraViewModel.onIdle()
                },
                onAnalyzeClick = {
                    cameraViewModel.onAnalyze((cameraViewModel.scanState as ScanState.ImagePreview).uri)
                }
            )
        }

    }
}

sealed class ScanState {
    object Idle : ScanState()
    object Scanning : ScanState()
    data class Scanned(val productScanResult: ProductScanResult) : ScanState()
    data class ImagePreview(val uri: Uri?) : ScanState()
}
