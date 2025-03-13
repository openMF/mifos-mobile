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

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatWriter
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader

private const val QR_CODE_SIZE = 200

/**
 * Generate a QR Code and return platform-specific representation.
 * @param str Data to be stored in QR Code.
 * @return Platform-specific QR Code representation.
 */

actual fun generateQrCode(str: String): ImageBitmap? {
    return try {
        val bitMatrix = MultiFormatWriter().encode(
            str,
            BarcodeFormat.QR_CODE,
            QR_CODE_SIZE,
            QR_CODE_SIZE,
        )
        val bitmap = createBitmapFromBitMatrix(bitMatrix)
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

private fun createBitmapFromBitMatrix(matrix: BitMatrix): Bitmap {
    val width = matrix.width
    val height = matrix.height
    val pixels = IntArray(width * height) { index ->
        if (matrix[index % width, index / width]) Color.Black.toArgb() else Color.White.toArgb()
    }

    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        setPixels(pixels, 0, width, 0, 0, width, height)
    }
}

actual fun decodeQrCode(bitmap: ImageBitmap): String? {
    val androidBitmap: Bitmap = bitmap.asAndroidBitmap()
    val intArray = IntArray(androidBitmap.width * androidBitmap.height)
    androidBitmap.getPixels(intArray, 0, androidBitmap.width, 0, 0, androidBitmap.width, androidBitmap.height)

    val source = RGBLuminanceSource(androidBitmap.width, androidBitmap.height, intArray)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        val reader = QRCodeMultiReader()
        val result = reader.decode(binaryBitmap)
//        QrCodeResult(text = result.text)
        result.text
    } catch (e: NotFoundException) {
        null
    }
}
