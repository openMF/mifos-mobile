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

import android.text.format.DateUtils
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

actual object LocalizedDateFormatter {

    actual fun formatFullDate(year: Int, month: Int, day: Int): String {
        val date = LocalDate.of(year, month, day)
        val locale = Locale.getDefault()

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", locale)
        val dateText = date.format(formatter)

        return localizeDateDigits(dateText, locale)
    }

    private fun localizeDateDigits(dateText: String, locale: Locale): String {
        val zeroDigit = DecimalFormatSymbols.getInstance(locale).zeroDigit
        if (zeroDigit == '0') return dateText

        return buildString {
            dateText.forEach { ch ->
                if (ch in '0'..'9') {
                    append((zeroDigit.code + (ch - '0')).toChar())
                } else {
                    append(ch)
                }
            }
        }
    }

    actual fun getRelativePrefix(year: Int, month: Int, day: Int): String? {
        return try {
            val targetDate = LocalDate.of(year, month, day)
            val today = LocalDate.now()
            val yesterday = today.minusDays(1)
            if (targetDate != today && targetDate != yesterday) return null
            val millis = targetDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            val locale = Locale.getDefault()
            val relative = DateUtils.getRelativeTimeSpanString(
                millis,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS,
            ).toString()
            localizeDateDigits(relative, locale)
        } catch (_: java.time.DateTimeException) {
            null
        }
    }
}
