@file:Suppress(" FunctionNaming")

package com.oguzhanaslann.camera

import android.content.Context
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.common.util.concurrent.ListenableFuture
import com.oguzhanaslann.common.SearchType
import com.oguzhanaslann.commonui.theme.contentPadding
import java.io.File
import java.util.*
import java.util.concurrent.Executor
import kotlin.math.abs

@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val cameraSelection = remember {
        mutableStateOf(CameraSelector.LENS_FACING_BACK)
    }

    var zoom by remember { mutableStateOf(1f) }
    val camera = remember {
        mutableStateOf<Camera?>(null)
    }

    val meteringFactory = remember {
        mutableStateOf<MeteringPointFactory?>(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                previewView.meteringPointFactory
                bindCameraUseCases(
                    ctx,
                    cameraProviderFuture,
                    previewView,
                    cameraSelection.value,
                    lifecycleOwner,
                    onCameraReady = { camera.value = it },
                    isScanning = cameraViewModel.cameraSearchType.isQRScanSearch(),
                    onImageProxy = cameraViewModel::onScan,
                    onImageCapture = { cameraViewModel.imageCapture = it },
                )

                meteringFactory.value = previewView.meteringPointFactory
                previewView
            },
            update = { previewView ->
                bindCameraUseCases(
                    ctx = previewView.context,
                    cameraProviderFuture = cameraProviderFuture,
                    previewView = previewView,
                    cameraSelection = cameraSelection.value,
                    lifecycleOwner = lifecycleOwner,
                    onCameraReady = { camera.value = it },
                    isScanning = cameraViewModel.cameraSearchType.isQRScanSearch(),
                    onImageProxy = cameraViewModel::onScan,
                    onImageCapture = { cameraViewModel.imageCapture = it },
                )

                meteringFactory.value = previewView.meteringPointFactory
            }
        )

        CameraControlUIView(
            modifier = Modifier
                .align(Alignment.TopEnd),
            isScanMode = cameraViewModel.cameraSearchType.isQRScanSearch(),
            onScanModeChanged = {
                if (it) {
                    cameraViewModel.cameraSearchType = (SearchType.CameraSearch.QRScanSearch)
                } else {
                    cameraViewModel.cameraSearchType = (SearchType.CameraSearch.ImageSearch)
                }
            },
            onFlipCamera = {
                zoom = 1f
                flipCameraLens(cameraSelection)
            },
            onZoom = {
                zoom *= abs(it)
                updateCameraZoom(zoom, camera)
            },
            onFocusTap = { offset ->
                focusCameraAt(offset, camera.value, meteringFactory.value)
            }
        )
    }

}

private fun flipCameraLens(cameraSelection: MutableState<Int>) {
    cameraSelection.value =
        if (cameraSelection.value == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
}

private fun updateCameraZoom(
    zoom: Float,
    camera: MutableState<Camera?>
) {
    camera.value?.cameraControl?.setZoomRatio(zoom)
}

fun focusCameraAt(
    offset: Offset,
    camera: Camera?,
    meteringFactory: MeteringPointFactory?
) {
    meteringFactory?.let {
        val point = it.createPoint(offset.x, offset.y)
        val action = FocusMeteringAction.Builder(point).build()
        camera?.cameraControl?.startFocusAndMetering(action)
    }
}

@Composable
fun CameraControlUIView(
    modifier: Modifier = Modifier,
    isScanMode: Boolean,
    onScanModeChanged: (Boolean) -> Unit = {},
    onFlipCamera: () -> Unit = {},
    onZoom: (Float) -> Unit = {},
    onFocusTap: (Offset) -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(
                key1 = "camera_control_ui_view_zoom",
            ) {
                detectTransformGestures(
                    onGesture = { centroid: Offset, pan: Offset, zoom: Float, rotation: Float ->
                        onZoom(zoom)
                    }
                )
            }
            .pointerInput(
                key1 = "camera_control_ui_view_double_tap",
            ) {
                detectTapGestures(
                    onDoubleTap = {
                        onFlipCamera()
                    },
                    onTap = {
                        onFocusTap(it)
                    }
                )
            }
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
                isScanning = isScanMode,
                onScanChanged = {
                    onScanModeChanged(it)
                }
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center),
            visible = isScanMode,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_barcode),
                contentDescription = "Qr, Barcode Scan Area"
            )
        }
    }
}

@Suppress("LongParameterList")
private fun bindCameraUseCases(
    ctx: Context,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    previewView: PreviewView,
    cameraSelection: Int,
    lifecycleOwner: LifecycleOwner,
    onCameraReady: (Camera) -> Unit = {},
    isScanning: Boolean = false,
    onImageProxy: (ImageProxy) -> Unit = {},
    onImageCapture: (ImageCapture) -> Unit = {}
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


        cameraProvider.unbindAll()

        runCatching {

            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(preview)
                .apply {
                    if (isScanning) {
                        val imageAnalysis = getImageAnalysis(executor, onImageProxy)
                        addUseCase(imageAnalysis)
                    }
                }
                .apply {
                    if (!isScanning) {
                        val imageCapture = ImageCapture.Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                            .setTargetRotation(previewView.display.rotation)
                            .build()

                        onImageCapture(imageCapture)
                        addUseCase(imageCapture)
                    }
                }
                .build()

            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                useCaseGroup
            )

            onCameraReady(camera)
        }
    }, executor)

}

fun getImageAnalysis(
    executor: Executor,
    onImageProxy: (ImageProxy) -> Unit = {},
): ImageAnalysis = with(ImageAnalysis.Builder()) {
    setTargetResolution(Size(640, 480))
    setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
    build()
}.apply {
    setAnalyzer(executor) { proxy -> onImageProxy(proxy) }
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
            .graphicsLayer(rotationZ = animation.value),
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
