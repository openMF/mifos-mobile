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

external class IntlNumberFormat {
    fun format(value: Double): String
}

@JsFun(
    """
    (currencyCode, digits) => new Intl.NumberFormat(undefined, {
        style: "currency",
        currency: currencyCode,
        currencyDisplay: "symbol",
        minimumFractionDigits: digits,
        maximumFractionDigits: digits
    })
""",
)
external fun createIntlFormatter(currencyCode: String, digits: Int): IntlNumberFormat

actual object CurrencyFormatter {
    actual fun format(
        balance: Double?,
        currencyCode: String?,
        maximumFractionDigits: Int?,
    ): String {
        if (balance == null || currencyCode.isNullOrBlank()) return ""

        return try {
            val digits = maximumFractionDigits ?: 2
            createIntlFormatter(currencyCode, digits).format(balance)
        } catch (_: Throwable) {
            balance.toString()
        }
    }
}
