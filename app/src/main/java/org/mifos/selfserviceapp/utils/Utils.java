package org.mifos.selfserviceapp.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;

import org.mifos.selfserviceapp.R;

import java.text.DateFormatSymbols;

/**
 * Created by michaelsosnick on 12/12/16.
 */

public class Utils {
    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public static LayerDrawable setCircularBackground(int colorId, Context context) {
        Drawable color = new ColorDrawable(ContextCompat.getColor(context, colorId));
        Drawable image = ContextCompat.getDrawable(context, R.drawable.circular_background);
        LayerDrawable ld = new LayerDrawable(new Drawable[]{image, color});
        return ld;
    }

    public static String generateFormString(String[][] data) {
        StringBuilder formString = new StringBuilder();
        formString.setLength(0);
        for (String[] aData : data) {
            formString.append(aData[0]).append(" : ").append(aData[1]).append('\n');
        }
        return formString.toString();
    }
}
