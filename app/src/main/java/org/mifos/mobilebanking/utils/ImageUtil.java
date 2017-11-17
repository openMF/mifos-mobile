package org.mifos.mobilebanking.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by dilpreet on 10/8/17.
 */

public class ImageUtil {
    /**
     * Reference : https://developer.android.com/topic/performance/graphics/load-bitmap.html
     * And for scaling :
     * https://stackoverflow.com/questions/8722359/scale-rotate-bitmap-using-matrix-in-android/8722592#8722592
     */

    private static ImageUtil instance;

    public static ImageUtil getInstance() {
        if (instance == null) {
            instance = new ImageUtil();
        }
        return instance;
    }

    //Default width and height
    public Bitmap compressImage(byte[] decodedBytes) {
        return compress(decodedBytes, 816.0f, 612.0f);
    }

    public Bitmap compressImage(byte[] decodedBytes, float maxHeight, float maxWidth) {
        return compress(decodedBytes, maxHeight, maxWidth);
    }

    private Bitmap compress(byte[] decodedBytes, float maxHeight, float maxWidth) {
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory.
//      Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize =
                calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
        } catch (OutOfMemoryError exception) {
            Log.e(ImageUtil.class.getName(), exception.toString());
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            Log.e(ImageUtil.class.getName(), exception.toString());
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));


        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                scaledBitmap.getWidth(), scaledBitmap.getHeight(), null,
                true);

        return scaledBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        float totalPixels = width * height;
        float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}