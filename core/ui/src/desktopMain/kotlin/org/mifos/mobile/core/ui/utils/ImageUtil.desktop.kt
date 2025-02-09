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

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

actual object ImageUtil {
    actual val DEFAULT_MAX_WIDTH: Float = 816f
    actual val DEFAULT_MAX_HEIGHT: Float = 612f

    actual fun compressImage(
        decodedBytes: ByteArray,
        maxWidth: Float,
        maxHeight: Float,
    ): ByteArray {
        val inputStream = ByteArrayInputStream(decodedBytes)
        val originalImage: BufferedImage = try {
            ImageIO.read(inputStream)
        } catch (e: Exception) {
//            e.printStackTrace()
            return ByteArray(0)
        }

        val (actualWidth, actualHeight) = calculateActualDimensions(
            originalImage.width.toFloat(),
            originalImage.height.toFloat(),
            maxWidth,
            maxHeight,
        )

        val scaledImage = createScaledImage(originalImage, actualWidth, actualHeight)

        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            ImageIO.write(scaledImage, "JPEG", byteArrayOutputStream)
        } catch (e: Exception) {
//            e.printStackTrace()
        }

        return byteArrayOutputStream.toByteArray()
    }

    private fun calculateActualDimensions(
        actualWidth: Float,
        actualHeight: Float,
        maxWidth: Float,
        maxHeight: Float,
    ): Pair<Int, Int> {
        val imgRatio = actualWidth / actualHeight
        val maxRatio = maxWidth / maxHeight

        var finalWidth = actualWidth
        var finalHeight = actualHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            when {
                imgRatio < maxRatio -> {
                    finalHeight = maxHeight
                    finalWidth = maxHeight * imgRatio
                }
                imgRatio > maxRatio -> {
                    finalWidth = maxWidth
                    finalHeight = maxWidth / imgRatio
                }
                else -> {
                    finalWidth = maxWidth
                    finalHeight = maxHeight
                }
            }
        }

        return Pair(finalWidth.toInt(), finalHeight.toInt())
    }

    private fun createScaledImage(image: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        val scaledImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
        val g2d: Graphics2D = scaledImage.createGraphics()
        g2d.drawImage(image, 0, 0, targetWidth, targetHeight, null)
        g2d.dispose()

        return scaledImage
    }
}
