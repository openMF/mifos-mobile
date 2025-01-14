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

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual object CurrencyFormatter {
    actual fun format(
        balance: Double?,
        currencyCode: String?,
        maximumFractionDigits: Int?,
    ): String {
        val numberFormatter = NSNumberFormatter()
        numberFormatter.numberStyle = NSNumberFormatterCurrencyStyle
        numberFormatter.currencyCode = currencyCode ?: "$"
        numberFormatter.maximumFractionDigits = (maximumFractionDigits ?: 0).toULong()
        return numberFormatter.stringFromNumber(NSNumber(balance ?: 0.0)) ?: ""
    }
}
