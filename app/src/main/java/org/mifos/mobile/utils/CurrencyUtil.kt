package org.mifos.mobile.utils

import android.content.Context
import android.telephony.TelephonyManager
import java.text.DecimalFormat
import java.util.*

/**
 * Created by dilpreet on 21/8/17.
 */
object CurrencyUtil {

    private val defaultLocale = Locale.US
    fun formatCurrency(context: Context, amt: String?): String {
        return getDecimalFormatter(context).format(amt)
    }

    fun formatCurrency(context: Context, amt: Long): String {
        return getDecimalFormatter(context).format(amt)
    }

    @kotlin.jvm.JvmStatic
    fun formatCurrency(context: Context?, amt: Double?): String {
        return getDecimalFormatter(context).format(amt)
    }

    private fun getDecimalFormatter(context: Context?): DecimalFormat {
        val currencyFormatter: DecimalFormat
        val locale: Locale?
        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        locale = try {
            Locale("en", tm.networkCountryIso.toUpperCase())
        } catch (e: Exception) {
            defaultLocale
        }
        currencyFormatter = try {
            DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        } catch (e: Exception) {
            DecimalFormat.getCurrencyInstance(defaultLocale) as DecimalFormat
        }
        val decimalFormatSymbols = currencyFormatter.decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = ""
        currencyFormatter.decimalFormatSymbols = decimalFormatSymbols
        return currencyFormatter
    }
}