package org.mifos.mobile.ui.qr

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import androidx.camera.core.Camera
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import org.mifos.mobile.utils.QrCodeAnalyzer

@ExperimentalGetImage
class BarcodeCamera {

    private var camera: Camera? = null

    @Composable
    fun BarcodeReaderCamera(
        onBarcodeScanned: (String?) -> Unit,
        isFlashOn: Boolean,
    ) {
        LaunchedEffect(isFlashOn) {
            toggleFlash(isOn = isFlashOn)
        }

        val lifecycleOwner = LocalLifecycleOwner.current

        val imageCapture = remember {
            ImageCapture.Builder().build()
        }

        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    scaleType = PreviewView.ScaleType.FILL_START

                    startCamera(
                        context = context,
                        previewView = this,
                        imageCapture = imageCapture,
                        lifecycleOwner = lifecycleOwner,
                        onBarcodeScanned = onBarcodeScanned
                    )
                }
            }
        )
    }

    private fun startCamera(
        context: Context,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        imageCapture: ImageCapture,
        onBarcodeScanned: (String) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer(
                        onBarcodeScanned = onBarcodeScanned
                    )
                )

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis)
                } catch (exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    private fun toggleFlash(
        isOn: Boolean,
    ) {
        camera?.cameraControl?.enableTorch(isOn)
    }
}