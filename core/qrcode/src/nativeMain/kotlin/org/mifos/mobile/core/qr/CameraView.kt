/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.qr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.useContents
import kotlinx.cinterop.value
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeLeft
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeRight
import platform.AVFoundation.AVCaptureVideoOrientationPortrait
import platform.AVFoundation.AVCaptureVideoOrientationPortraitUpsideDown
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectType
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSError
import platform.QuartzCore.CALayer
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

@Composable
fun UiScannerView(
    allowedMetadataTypes: List<AVMetadataObjectType>,
    modifier: Modifier = Modifier,
    onScanned: (String) -> Boolean,
) {
    val coordinator = remember {
        ScannerCameraCoordinator(
            onScanned = onScanned,
        )
    }

    DisposableEffect(Unit) {
        val listener = OrientationListener { orientation ->
            coordinator.setCurrentOrientation(orientation)
        }

        listener.register()

        onDispose {
            listener.unregister()
        }
    }

    UIKitView<UIView>(
        modifier = modifier.fillMaxSize(),
        factory = {
            val previewContainer = ScannerPreviewView(coordinator)
            println("Calling prepare")
            coordinator.prepare(previewContainer.layer, allowedMetadataTypes)
            previewContainer
        },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true,
        ),
    )

//    DisposableEffect(Unit) {
//        onDispose {
//            // stop capture
//            coordinator.
//        }
//    }
}

@OptIn(ExperimentalForeignApi::class)
class ScannerPreviewView(private val coordinator: ScannerCameraCoordinator) :
    UIView(frame = cValue { CGRectZero }) {
    @OptIn(ExperimentalForeignApi::class)
    override fun layoutSubviews() {
        super.layoutSubviews()
        CATransaction.begin()
        CATransaction.setValue(true, kCATransactionDisableActions)

        layer.setFrame(frame)
        coordinator.setFrame(frame)
        CATransaction.commit()
    }
}

@OptIn(ExperimentalForeignApi::class)
class ScannerCameraCoordinator(
    val onScanned: (String) -> Boolean,
) : AVCaptureMetadataOutputObjectsDelegateProtocol, NSObject() {

    private var previewLayer: AVCaptureVideoPreviewLayer? = null
    lateinit var captureSession: AVCaptureSession

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, DelicateCoroutinesApi::class)
    fun prepare(layer: CALayer, allowedMetadataTypes: List<AVMetadataObjectType>) {
        captureSession = AVCaptureSession()
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)

        if (device == null) {
            println("Device has no camera")
        } else {
            println("Initializing video input")
            val videoInput = createVideoInput(device)
            println("Adding video input")
            if (videoInput != null && captureSession.canAddInput(videoInput)) {
                captureSession.addInput(videoInput)

                val metadataOutput = AVCaptureMetadataOutput()
                println("Adding metadata output")
                if (captureSession.canAddOutput(metadataOutput)) {
                    captureSession.addOutput(metadataOutput)

                    metadataOutput.setMetadataObjectsDelegate(
                        this,
                        queue = dispatch_get_main_queue(),
                    )
                    metadataOutput.metadataObjectTypes = allowedMetadataTypes

                    println("Adding preview layer")
                    previewLayer = AVCaptureVideoPreviewLayer(session = captureSession).also {
                        it.frame = layer.bounds
                        it.videoGravity = AVLayerVideoGravityResizeAspectFill
                        println("Set orientation")
                        setCurrentOrientation(newOrientation = UIDevice.currentDevice.orientation)
                        println("Adding sublayer")
                        layer.bounds.useContents {
                            println("Bounds: ${this.size.width}x${this.size.height}")
                        }
                        layer.frame.useContents {
                            println("Frame: ${this.size.width}x${this.size.height}")
                        }
                        layer.addSublayer(it)
                    }

                    println("Launching capture session")
                    GlobalScope.launch(Dispatchers.Default) {
                        captureSession.startRunning()
                    }
                } else {
                    println("Could not add output")
                }
            } else {
                println("Could not add input")
            }
        }
    }

    fun setCurrentOrientation(newOrientation: UIDeviceOrientation) {
        when (newOrientation) {
            UIDeviceOrientation.UIDeviceOrientationLandscapeLeft ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationLandscapeRight

            UIDeviceOrientation.UIDeviceOrientationLandscapeRight ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationLandscapeLeft

            UIDeviceOrientation.UIDeviceOrientationPortrait ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationPortrait

            UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown ->
                previewLayer?.connection?.videoOrientation =
                    AVCaptureVideoOrientationPortraitUpsideDown

            else ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationPortrait
        }
    }

    override fun captureOutput(
        output: platform.AVFoundation.AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: platform.AVFoundation.AVCaptureConnection,
    ) {
        val metadataObject =
            didOutputMetadataObjects.firstOrNull() as? AVMetadataMachineReadableCodeObject
        metadataObject?.stringValue?.let { onFound(it) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun onFound(code: String) {
        captureSession.stopRunning()
        if (!onScanned(code)) {
            GlobalScope.launch(Dispatchers.Default) {
                captureSession.startRunning()
            }
        }
    }

    fun setFrame(rect: CValue<CGRect>) {
        previewLayer?.setFrame(rect)
    }
}

// Extracted function: Creates a video input
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun createVideoInput(device: AVCaptureDevice): AVCaptureDeviceInput? {
    return memScoped {
        val error: ObjCObjectVar<NSError?> = alloc()
        val videoInput = AVCaptureDeviceInput(device = device, error = error.ptr)
        if (error.value != null) {
            println(error.value)
            null
        } else {
            videoInput
        }
    }
}
