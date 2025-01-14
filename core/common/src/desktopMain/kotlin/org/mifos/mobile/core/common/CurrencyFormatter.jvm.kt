/*
 * Copyright 2024 Mifos Initiative
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

actual object CurrencyFormatter {
    actual fun format(
        balance: Double?,
        currencyCode: String?,
        maximumFractionDigits: Int?,
    ): String {
        val numberFormat = NumberFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = maximumFractionDigits ?: 0
        numberFormat.currency = Currency.getInstance(currencyCode)
        return numberFormat.format(balance)
    }
}
