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

actual object CurrencyFormatter {
    actual fun format(
        balance: Double?,
        currencyCode: String?,
        maximumFractionDigits: Int?,
    ): String {
        if (balance == null || currencyCode.isNullOrBlank()) return ""

        val options = js("{}").unsafeCast<dynamic>()
        options.style = "currency"
        options.currency = currencyCode
        options.currencyDisplay = "symbol"

        val digits = maximumFractionDigits ?: 2
        options.minimumFractionDigits = digits
        options.maximumFractionDigits = digits

        return try {
            js("new Intl.NumberFormat(undefined, options).format(balance)").toString()
        } catch (e: dynamic) {
            console.error("Error formatting currency: ${e.message}")
            balance.toString()
        }
    }
}
