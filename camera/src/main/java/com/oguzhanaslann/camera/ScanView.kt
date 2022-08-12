package com.oguzhanaslann.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.oguzhanaslann.commonui.LoadingView

@Composable
fun ScanView(
    isLoading: Boolean = false
) {

    var scanState by remember { mutableStateOf(ScanState.Scanned ) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraView(
            modifier = Modifier.align(Alignment.Center),
            onScan = {
                // google Ml kit barcode scanner
            },
            onImageCapture = {
                // google Ml kit image scan
            },

            onImageCaptureError = {

            }
        )

        when (scanState) {
            ScanState.Idle -> Unit
            ScanState.Scanning -> LoadingView(
                modifier = Modifier.align(Alignment.Center)
            )
            ScanState.Scanned -> CameraResultView(
                ProductScanResult(
                    productName = "Lorem Ipsum",
                    productImage = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                    statistics = listOf(
                        BinaryStatistic( "Certificated", true),
                        BinaryStatistic( "Recyclable", true),
                        BinaryStatistic( "GreenHouse gases", false),
                        BinaryStatistic( "Renewable Energy", true),
                        LevelStatistic( "Toxicity",  Level.Medium)
                    ),
                    greennessRatio = Ratio(3,5)
                ),
                onGoBackClicked = {
                    scanState = ScanState.Idle
                }
            )
            ScanState.ImageScanned -> TODO()
        }

    }
}

enum class ScanState {
    Scanning,
    Scanned,
    ImageScanned,
    Idle
}
