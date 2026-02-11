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

import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

actual object LocalizedDateFormatter {

    actual fun formatFullDate(year: Int, month: Int, day: Int): String {
        val date = LocalDate.of(year, month, day)
        val locale = Locale.getDefault()

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", locale)
        val formatted = date.format(formatter)

        return localizeDateDigits(formatted, locale)
    }

    actual fun getRelativePrefix(day: Int, month: Int, year: Int): String? {
        return null
    }

    private fun localizeDateDigits(text: String, locale: Locale): String {
        val zeroDigit = DecimalFormatSymbols.getInstance(locale).zeroDigit
        if (zeroDigit == '0') return text

        return buildString {
            text.forEach { ch ->
                if (ch in '0'..'9') {
                    append((zeroDigit.code + (ch - '0')).toChar())
                } else {
                    append(ch)
                }
            }
        }
    }
}
