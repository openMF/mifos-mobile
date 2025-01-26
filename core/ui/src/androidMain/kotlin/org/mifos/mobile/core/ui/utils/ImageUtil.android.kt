/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import java.io.ByteArrayOutputStream

actual object ImageUtil {
    actual val DEFAULT_MAX_WIDTH: Float = 816f
    actual val DEFAULT_MAX_HEIGHT: Float = 612f

    actual fun compressImage(
        decodedBytes: ByteArray,
        maxWidth: Float,
        maxHeight: Float,
    ): ByteArray {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, options)

        val (actualWidth, actualHeight) = calculateActualDimensions(options, maxWidth, maxHeight)

        options.apply {
            inJustDecodeBounds = false
            inSampleSize = calculateInSampleSize(this, actualWidth, actualHeight)
            inTempStorage = ByteArray(16 * 1024)
        }

        val bmp = try {
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, options)
        } catch (e: OutOfMemoryError) {
            Log.e(this::class.java.simpleName, "OutOfMemoryError while decoding bitmap", e)
            return ByteArray(0)
        }

        val scaledBitmap = try {
            createScaledBitmap(bmp, actualWidth, actualHeight, options)
        } catch (e: OutOfMemoryError) {
            Log.e(this::class.java.simpleName, "OutOfMemoryError while scaling bitmap", e)
            bmp
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun calculateActualDimensions(
        options: BitmapFactory.Options,
        maxWidth: Float,
        maxHeight: Float,
    ): Pair<Int, Int> {
        var actualWidth = options.outWidth
        var actualHeight = options.outHeight
        val imgRatio = actualWidth.toFloat() / actualHeight
        val maxRatio = maxWidth / maxHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            when {
                imgRatio < maxRatio -> {
                    actualHeight = maxHeight.toInt()
                    actualWidth = (maxHeight * imgRatio).toInt()
                }

                imgRatio > maxRatio -> {
                    actualWidth = maxWidth.toInt()
                    actualHeight = (maxWidth / imgRatio).toInt()
                }

                else -> {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }
        }

        return Pair(actualWidth, actualHeight)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val (height, width) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun createScaledBitmap(
        bmp: Bitmap,
        actualWidth: Int,
        actualHeight: Int,
        options: BitmapFactory.Options,
    ): Bitmap {
        val scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2f
        val middleY = actualHeight / 2f

        val scaleMatrix = Matrix().apply {
            setScale(ratioX, ratioY, middleX, middleY)
        }

        Canvas(scaledBitmap).apply {
            setMatrix(scaleMatrix)
            drawBitmap(
                bmp,
                middleX - bmp.width / 2,
                middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG),
            )
        }

        return scaledBitmap
    }
}
