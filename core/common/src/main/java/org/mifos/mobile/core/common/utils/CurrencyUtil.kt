/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common.utils

import android.content.Context
import android.telephony.TelephonyManager
import java.text.DecimalFormat
import java.util.Locale

object CurrencyUtil {

    private val defaultLocale = Locale.US
    fun formatCurrency(context: Context, amt: String?): String {
        return getDecimalFormatter(context).format(amt ?: "0.0")
    }

    fun formatCurrency(context: Context, amt: Long): String {
        return getDecimalFormatter(context).format(amt)
    }

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
