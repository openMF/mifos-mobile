package org.mifos.selfserviceapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


public class Base64toBitmap {
    //This class converts any base64 to its image.
    private Exception exception;

    public Bitmap toBitmap(String base64input) {
        String formattedInput;
        try {
            formattedInput = base64input.replaceFirst("^data:image/[^;]*;base64,?", "");
        } catch (Exception e) {
            exception = e;
            return null;
        }
        //Replaces blank spaces and new line
        formattedInput = formattedInput.replaceAll(" ", "").replaceAll("\n", "");
        try {
            byte[] decodedString = Base64.decode(formattedInput, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    public Exception getError() {
        return exception;
    }
}