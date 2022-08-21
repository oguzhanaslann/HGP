package com.oguzhanaslann.camera

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oguzhanaslann.commonui.HGPBottomAppBar
import com.oguzhanaslann.commonui.HGPDrawer
import com.oguzhanaslann.commonui.LoadingView
import com.oguzhanaslann.commonui.theme.white
import com.oguzhanaslann.commonui.toggle
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

@Composable
fun ScanView(
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel = viewModel(),
    onTextSearchClicked: () -> Unit = {},
    onVoiceSearchClicked: () -> Unit = {},
    onPrivacyPolicyClicked: () -> Unit = {},
    onContactUsClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {}
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        bottomBar = {
            HGPBottomAppBar(
                onDrawerClick = { scope.launch { scaffoldState.drawerState.toggle() } }
            )
        },
        drawerShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        drawerContent = {
            HGPDrawer(
                onVoiceSearchClicked = onVoiceSearchClicked,
                onTextSearchClicked = onTextSearchClicked,
                onPrivacyPolicyClicked = onPrivacyPolicyClicked,
                onContactUsClicked = onContactUsClicked,
                onShareClicked = onShareClicked
            )
        },
        floatingActionButton = {
            if (cameraViewModel.cameraSearchType.isImageSearch()) {
                val context = LocalContext.current
                IconButton(onClick = {
//                    cameraViewModel.onImageCapturing() todo fix this
                    val executor = ContextCompat.getMainExecutor(context)
                    val outputFileOptions = ImageCapture.OutputFileOptions
                        .Builder(
                            File(context.cacheDir, "capture_${UUID.randomUUID()}.jpg")
                        )
                        .build()
                    cameraViewModel.imageCapture?.takePicture(
                        outputFileOptions,
                        executor,
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                Log.e("TAG", "onImageSaved: ${outputFileResults.savedUri}")
                                cameraViewModel.onImageCapture(outputFileResults.savedUri)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("TAG", "onError: ${exception.message}")
                                cameraViewModel.onImageCaptureError(exception)
                            }
                        })
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_shutter),
                        contentDescription = "Shutter",
                        tint = white
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { values ->
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
    )
}
