package org.mifos.selfserviceapp.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.content.Context;

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
}
