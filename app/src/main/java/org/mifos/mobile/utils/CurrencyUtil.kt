package org.mifos.mobile.utils

import android.content.Context
import android.telephony.TelephonyManager
import java.text.DecimalFormat
import java.util.Locale

/**
 * Created by dilpreet on 21/8/17.
 */
object CurrencyUtil {

    private val defaultLocale = Locale.US
    fun formatCurrency(context: Context, amt: String?): String {
        return getDecimalFormatter(context).format(amt ?: "0.0")
    }

    fun formatCurrency(context: Context, amt: Long): String {
        return getDecimalFormatter(context).format(amt)
    }

    @JvmStatic
    fun formatCurrency(context: Context?, amt: Double?): String {
        return getDecimalFormatter(context).format(amt ?: 0.0)
    }

    private fun getDecimalFormatter(context: Context?): DecimalFormat {
        val currencyFormatter: DecimalFormat
        val locale: Locale?
        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        locale = try {
            Locale("en", tm.networkCountryIso.uppercase(Locale.ROOT))
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

    fun formatCurrency(amt: Double?): String {
        return getDecimalFormatter().format(amt ?: 0.0)
    }

    private fun getDecimalFormatter(): DecimalFormat {
        val currencyFormatter: DecimalFormat = try {
            DecimalFormat.getCurrencyInstance(defaultLocale) as DecimalFormat
        } catch (e: Exception) {
            DecimalFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat
        }
        val decimalFormatSymbols = currencyFormatter.decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = ""
        currencyFormatter.decimalFormatSymbols = decimalFormatSymbols
        return currencyFormatter
    }
}
