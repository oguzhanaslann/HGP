package com.oguzhanaslann.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oguzhanaslann.common.SearchType
import com.oguzhanaslann.commonui.LoadingView

@Composable
fun ScanView(
    cameraViewModel: CameraViewModel = viewModel(),
    cameraSearchType: SearchType.CameraSearch = SearchType.CameraSearch.QRScanSearch,
    onScanModeChanged: (SearchType.CameraSearch) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraView(
            modifier = Modifier.align(Alignment.Center),
            cameraViewModel = cameraViewModel,
            cameraSearchType = cameraSearchType,
            onScanModeChanged = onScanModeChanged
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
