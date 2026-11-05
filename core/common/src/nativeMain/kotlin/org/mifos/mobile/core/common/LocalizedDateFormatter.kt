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

import platform.Foundation.NSArray
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.firstObject

actual object LocalizedDateFormatter {

    private fun getCurrentLocale(): NSLocale {
        val languages = NSUserDefaults.standardUserDefaults
            .objectForKey("AppleLanguages") as? NSArray

        val currentLanguage = languages
            ?.firstObject as? String

        if (currentLanguage != null) {
            val updatedIdentifier = when (currentLanguage) {
                "ar" -> "ar@numbers=arab"
                "hi" -> "hi@numbers=deva"
                "bn" -> "bn@numbers=beng"
                "fa" -> "fa@numbers=arabext"
                "kn" -> "kn@numbers=knda"
                "my" -> "my@numbers=mymr"
                "km" -> "km@numbers=khmr"
                "ml" -> "ml@numbers=mlym"
                "te" -> "te@numbers=telu"
                "ur" -> "ur@numbers=arabext"
                else -> currentLanguage
            }
            return NSLocale(updatedIdentifier)
        }

        return NSLocale.currentLocale()
    }

    actual fun formatFullDate(year: Int, month: Int, day: Int): String {
        val date = createNSDate(day, month, year) ?: return ""

        val formatter = NSDateFormatter().apply {
            locale = getCurrentLocale()
            dateStyle = NSDateFormatterLongStyle
            timeStyle = NSDateFormatterNoStyle
        }

        val formatted = formatter.stringFromDate(date)
        return localizeDateDigits(formatted)
    }

    actual fun getRelativePrefix(year: Int, month: Int, day: Int): String? {
        val date = createNSDate(day, month, year) ?: return null
        val calendar = NSCalendar.currentCalendar

        val isToday = calendar.isDateInToday(date)
        val isYesterday = calendar.isDateInYesterday(date)

        return if (isToday || isYesterday) {
            val formatter = NSDateFormatter().apply {
                locale = getCurrentLocale()
                doesRelativeDateFormatting = true
                dateStyle = NSDateFormatterMediumStyle
                timeStyle = NSDateFormatterNoStyle
            }

            val fullString = formatter.stringFromDate(date)
            val prefix = fullString.split(",").firstOrNull()?.trim()

            prefix?.let { localizeDateDigits(it) }
        } else {
            null
        }
    }

    private fun localizeDateDigits(text: String): String {
        val numberFormatter = NSNumberFormatter().apply {
            locale = getCurrentLocale()
            numberStyle = NSNumberFormatterDecimalStyle
        }

        val zeroSymbol = numberFormatter.zeroSymbol ?: "0"

        // Latin-digit locales → return unchanged (Malayalam, Khmer, etc.)
        if (zeroSymbol == "0") return text

        val zeroCode = zeroSymbol.first().code

        return text.map { ch ->
            if (ch in '0'..'9') {
                (zeroCode + (ch - '0')).toChar()
            } else {
                ch
            }
        }.joinToString("")
    }

    private fun createNSDate(day: Int, month: Int, year: Int): NSDate? {
        val components = NSDateComponents().apply {
            this.day = day.toLong()
            this.month = month.toLong()
            this.year = year.toLong()
        }

        return NSCalendar.currentCalendar.dateFromComponents(components)
    }
}
