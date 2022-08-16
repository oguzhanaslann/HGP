package com.oguzhanaslann.camera

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.oguzhanaslann.commonui.base.SearchContent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.*
import javax.inject.Inject

class CameraSearchContent @Inject constructor(
    @ApplicationContext private val context: Context
) : SearchContent<CameraViewModel> {

    private lateinit var cameraViewModel: CameraViewModel

    override fun setViewModel(viewModel: CameraViewModel) {
        cameraViewModel = viewModel
    }

    override fun getSearchContent(): @Composable () -> Unit {
        return {
            ScanView(cameraViewModel)
        }
    }

    override fun getSearchAction(): @Composable () -> Unit {
        return {
            IconButton(onClick = {
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
                            cameraViewModel.onImageCapture(outputFileResults.savedUri)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            cameraViewModel.onImageCaptureError(exception)
                        }
                    })
            }) {
                Icon(
                    painterResource(id = R.drawable.ic_shutter),
                    contentDescription = "Shutter"
                )
            }
        }
    }
}
