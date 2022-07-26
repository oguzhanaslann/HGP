package com.oguzhanaslann.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.oguzhanaslann.commonui.theme.contentPadding

@Composable
fun CameraView() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val cameraSelection = remember {
        mutableStateOf(CameraSelector.LENS_FACING_BACK)
    }

//    val isScanningState = remember {
//        mutableStateOf(true)
//    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                bindCameraUseCases(
                    ctx,
                    cameraProviderFuture,
                    previewView,
                    cameraSelection.value,
                    lifecycleOwner
                )
                previewView
            },
            update = { previewView ->
                bindCameraUseCases(
                    ctx = previewView.context,
                    cameraProviderFuture = cameraProviderFuture,
                    previewView = previewView,
                    cameraSelection = cameraSelection.value,
                    lifecycleOwner = lifecycleOwner
                )

            }
        )

        CameraControlUIView(
            modifier = Modifier
                .align(Alignment.TopEnd),
            onFlipCamera = {
                cameraSelection.value =
                    if (cameraSelection.value == CameraSelector.LENS_FACING_BACK) {
                        CameraSelector.LENS_FACING_FRONT
                    } else {
                        CameraSelector.LENS_FACING_BACK
                    }
            }
        )

    }
}

@Composable
fun CameraControlUIView(
    modifier: Modifier = Modifier,
    onFlipCamera: () -> Unit = {},
) {

    val isScanningState = remember {
        mutableStateOf(true)
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(contentPadding)
        ) {
            FlipCameraView(
                onFlipClicked = onFlipCamera
            )

            ScanningEnableSwitchView(
                modifier = Modifier.padding(top = 8.dp),
                isScanning = isScanningState.value,
                onScanChanged = {
                    isScanningState.value = it
                }
            )
        }


    }
}

private fun bindCameraUseCases(
    ctx: Context,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    previewView: PreviewView,
    cameraSelection: Int,
    lifecycleOwner: LifecycleOwner
) {
    val executor = ContextCompat.getMainExecutor(ctx)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(cameraSelection)
            .build()

        runCatching {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview
            )
        }
    }, executor)
}

@Composable
private fun FlipCameraView(
    modifier: Modifier = Modifier,
    onFlipClicked: () -> Unit = {}
) {
    val animationState = remember { mutableStateOf(true) }
    val animation = animateFloatAsState(
        targetValue = if (animationState.value) 0f else 360f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    IconButton(
        modifier = modifier
            .graphicsLayer(
                rotationZ = animation.value,
            ),
        onClick = {
            animationState.value = !animationState.value
            onFlipClicked()
        },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_flip_camera),
            contentDescription = "Flip Camera"
        )
    }
}

@Composable
fun ScanningEnableSwitchView(
    modifier: Modifier = Modifier,
    isScanning: Boolean,
    onScanChanged: (Boolean) -> Unit = {}
) {


    IconToggleButton(
        modifier = modifier,
        checked = isScanning,
        onCheckedChange = onScanChanged
    ) {
        Icon(
            painter = if (isScanning) {
                painterResource(id = R.drawable.ic_scanning_enabled)
            } else {
                painterResource(id = R.drawable.ic_scanning_disabled)
            },
            contentDescription = "Scanning Enable"
        )
    }
}
