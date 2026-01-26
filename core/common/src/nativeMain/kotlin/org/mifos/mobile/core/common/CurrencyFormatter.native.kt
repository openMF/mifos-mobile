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

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.currentLocale

actual object CurrencyFormatter {
    actual fun format(
        balance: Double?,
        currencyCode: String?,
        maximumFractionDigits: Int?,
    ): String {
        if (balance == null || currencyCode.isNullOrBlank()) return ""

        return try {
            val digits = maximumFractionDigits ?: 2

            val formatter = NSNumberFormatter().apply {
                numberStyle = NSNumberFormatterCurrencyStyle
                locale = NSLocale.currentLocale

                setMinimumFractionDigits(digits.toULong())
                setMaximumFractionDigits(digits.toULong())

                setInternationalCurrencySymbol(currencyCode)
            }

            formatter.stringFromNumber(NSNumber(balance)) ?: balance.toString()
        } catch (_: Throwable) {
            balance.toString()
        }
    }
}
