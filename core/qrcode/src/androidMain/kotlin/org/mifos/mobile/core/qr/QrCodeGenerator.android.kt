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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.google.zxing.BinaryBitmap
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader

actual fun decodeQrCode(bitmap: ImageBitmap): String? {
    val androidBitmap: Bitmap = bitmap.asAndroidBitmap()
    val intArray = IntArray(androidBitmap.width * androidBitmap.height)
    androidBitmap.getPixels(intArray, 0, androidBitmap.width, 0, 0, androidBitmap.width, androidBitmap.height)

    val source = RGBLuminanceSource(androidBitmap.width, androidBitmap.height, intArray)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        val reader = QRCodeMultiReader()
        val result = reader.decode(binaryBitmap)
        result.text
    } catch (e: NotFoundException) {
        null
    }
}
