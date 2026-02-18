/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

actual object CurrencyFormatter {

    actual fun format(
        balance: Double?,
        currencyCode: String?,
        maximumFractionDigits: Int?,
    ): String {
        if (balance == null || currencyCode.isNullOrBlank()) return ""

        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

        return try {
            formatter.currency = Currency.getInstance(currencyCode)

            val digits = maximumFractionDigits ?: 2
            formatter.minimumFractionDigits = digits
            formatter.maximumFractionDigits = digits

            formatter.format(balance)
        } catch (_: IllegalArgumentException) {
            balance.toString()
        }
    }
}
