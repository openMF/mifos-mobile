package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by dilpreet on 21/8/17.
 */

public class CurrencyUtil {

    private static Locale defaultLocale = Locale.US;


    public static String formatCurrency(Context context, String amt) {
        return getDecimalFormatter(context).format(amt);
    }

    public static String formatCurrency(Context context, long amt) {
        return getDecimalFormatter(context).format(amt);
    }

    public static String formatCurrency(Context context, double amt) {
        return getDecimalFormatter(context).format(amt);
    }


    private static DecimalFormat getDecimalFormatter(Context context) {
        DecimalFormat currencyFormatter;
        Locale locale;
        TelephonyManager tm = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        try {
            locale = new Locale("en", tm.getNetworkCountryIso().toUpperCase());
        } catch (Exception e) {
            locale = defaultLocale;
        }

        try {
            currencyFormatter = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        } catch (Exception e) {
            currencyFormatter = (DecimalFormat) DecimalFormat.getCurrencyInstance(defaultLocale);
        }
        DecimalFormatSymbols decimalFormatSymbols = currencyFormatter.getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        currencyFormatter.setDecimalFormatSymbols(decimalFormatSymbols);
        return currencyFormatter;
    }
}
