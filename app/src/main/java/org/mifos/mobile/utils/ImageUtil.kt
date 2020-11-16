package org.mifos.mobile.utils

import android.graphics.*
import android.util.Log

/**
 * Created by dilpreet on 10/8/17.
 */
class ImageUtil {
    //Default width and height
    fun compressImage(decodedBytes: ByteArray): Bitmap? {
        return compress(decodedBytes, 816.0f, 612.0f)
    }

    fun compressImage(decodedBytes: ByteArray, maxHeight: Float, maxWidth: Float): Bitmap? {
        return compress(decodedBytes, maxHeight, maxWidth)
    }

    private fun compress(decodedBytes: ByteArray, maxHeight: Float, maxWidth: Float): Bitmap? {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory.
//      Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        var imgRatio = actualWidth / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, options)
        } catch (exception: OutOfMemoryError) {
            Log.e(ImageUtil::class.java.name, exception.toString())
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            Log.e(ImageUtil::class.java.name, exception.toString())
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas: Canvas? = Canvas(scaledBitmap)
        canvas?.matrix = scaleMatrix
        canvas?.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG))
        if (scaledBitmap != null)
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, null,
                    true)
        return scaledBitmap
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = width * height.toFloat()
        val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    companion object {
        /**
         * Reference : https://developer.android.com/topic/performance/graphics/load-bitmap.html
         * And for scaling :
         * https://stackoverflow.com/questions/8722359/scale-rotate-bitmap-using-matrix-in-android/8722592#8722592
         */
        @kotlin.jvm.JvmStatic
        var instance: ImageUtil? = null
            get() {
                if (field == null) {
                    field = ImageUtil()
                }
                return field
            }
            private set
    }
}