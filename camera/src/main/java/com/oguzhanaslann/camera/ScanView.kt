package com.oguzhanaslann.camera

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            ScanState.Scanned -> CameraResultView(
                ProductScanResult(
                    productName = "Lorem Ipsum",
                    productImage = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                    statistics = listOf(
                        BinaryStatistic("Certificated", true),
                        BinaryStatistic("Recyclable", true),
                        BinaryStatistic("GreenHouse gases", false),
                        BinaryStatistic("Renewable Energy", true),
                        LevelStatistic("Toxicity", Level.Medium)
                    ),
                    greennessRatio = Ratio(3, 5)
                ),
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
                    cameraViewModel.onIdle()
                }
            )
        }

    }
}

sealed class ScanState {
    object Scanning : ScanState()
    object Scanned : ScanState()
    data class ImagePreview(val uri: Uri?) : ScanState()
    object Idle : ScanState()
}
