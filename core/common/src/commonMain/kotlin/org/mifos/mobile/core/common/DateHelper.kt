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

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Suppress("TooManyFunctions")
@OptIn(FormatStringsInDatetimeFormats::class)
object DateHelper {
    /*
     * This is the full month format for the date picker.
     * "dd MM yyyy" is the format of the date picker.
     */
    const val FULL_MONTH = "dd MM yyyy"

    /*
     * This is the short month format for the date picker.
     * "dd-MM-yyyy" is the format of the date picker.
     */
    const val SHORT_MONTH = "yyyy-MM-dd"

    private val fullMonthFormat = LocalDateTime.Format {
        byUnicodePattern(FULL_MONTH)
    }

    private val shortMonthFormat = LocalDateTime.Format {
        byUnicodePattern(SHORT_MONTH)
    }

    /**
     * the result string uses the list given in a reverse order ([x, y, z] results in "z y x")
     *
     * @param integersOfDate [year-month-day] (ex [2016, 4, 14])
     * @return date in the format day month year (ex 14 Apr 2016)
     */
    fun getDateAsString(integersOfDate: List<Int>): String {
        require(integersOfDate.size == 3)
        val (year, month, day) = integersOfDate
        return LocalizedDateFormatter.formatFullDate(year, month, day)
    }

    @OptIn(ExperimentalTime::class)
    fun getFormattedDateWithPrefix(date: List<Int>): String {
        require(date.size == 3)
        val (year, month, day) = date

        val prefix = LocalizedDateFormatter.getRelativePrefix(day, month, year)
        val formattedDate = LocalizedDateFormatter.formatFullDate(year, month, day)

        return prefix?.let { "$it, $formattedDate" } ?: formattedDate
    }

    fun getDateAsString(integersOfDate: List<Long>, pattern: String): String {
        return getFormatConverter(
            requiredFormat = pattern,
            dateString = getDateAsString(integersOfDate.map { it.toInt() }),
        )
    }

    @OptIn(ExperimentalTime::class)
    fun isDarkModeBasedOnTime(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ): Boolean {
        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time

        val currentMinutes = now.hour * 60 + now.minute
        val startMinutes = startHour * 60 + startMinute
        val endMinutes = endHour * 60 + endMinute

        return if (startMinutes < endMinutes) {
            currentMinutes in startMinutes until endMinutes
        } else {
            currentMinutes !in endMinutes..<startMinutes
        }
    }

    fun formatTimeRange(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ): String {
        fun format(hour: Int, minute: Int): String {
            val period = if (hour < 12) "AM" else "PM"
            val hour12 = when (hour % 12) {
                0 -> 12
                else -> hour % 12
            }
            return "$hour12:${minute.toString().padStart(2, '0')} $period"
        }

        return "${format(startHour, startMinute)} - ${format(endHour, endMinute)}"
    }

    fun getDateAsList(date: String): List<Int> {
        val formatter = LocalDateTime.Format {
            byUnicodePattern("d MMMM yyyy")
        }

        val parsed = formatter.parse(date)
        return listOf(parsed.year, parsed.month.number, parsed.day)
    }

    @OptIn(ExperimentalTime::class)
    fun getDateAsLongFromList(integersOfDate: List<Int>?): Long? {
        if (integersOfDate == null) return null
        val dateStr = getDateAsString(integersOfDate)
        return try {
            val dateList = getDateAsList(dateStr)
            val localDate = LocalDate(dateList[0], dateList[1], dateList[2])
            localDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        } catch (_: Exception) {
            null
        }
    }

    private fun getFormatConverter(
        requiredFormat: String,
        dateString: String,
    ): String {
        val pickerFormat = LocalDateTime.Format { byUnicodePattern(FULL_MONTH) }
        val finalFormat = LocalDateTime.Format { byUnicodePattern(requiredFormat) }

        return pickerFormat.parse(dateString).format(finalFormat)
    }

    fun LocalDate.format(pattern: String): String {
        val year = this.year.toString().padStart(4, '0')
        val month = this.month.toString().padStart(2, '0')
        val day = this.day.toString().padStart(2, '0')

        return pattern
            .replace("yyyy", year)
            .replace("MM", month)
            .replace("dd", day)
    }

    @OptIn(ExperimentalTime::class)
    fun getDateAsStringFromLong(timeInMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(timeInMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        return instant.format(shortMonthFormat)
    }

    @OptIn(ExperimentalTime::class)
    fun getDateMonthYearString(timeInMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(timeInMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val day = instant.day
        val month = instant.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        val year = instant.year

        return "$day $month $year"
    }

    /**
     * Gets the date string in the format "dd MMMM yyyy" from a list of integers representing [day, month, year].
     *
     * @param integersOfDate A list of three integers representing [day, month, year], e.g. [8, 3, 2025]
     * @return The date string in the format "08 March 2025"
     */
    fun getDateMonthYearString(integersOfDate: List<Int>): String {
        val (year, month, day) = integersOfDate
        return LocalizedDateFormatter.formatFullDate(year, month, day)
    }

    @OptIn(ExperimentalTime::class)
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    /**
     * This is the full date format for the date picker.
     * "dd MM yyyy" is the format of the date picker.
     */
    val formattedFullDate = currentDate.format(fullMonthFormat)

    /**
     * This is the short date format for the date picker.
     * "dd-MM-yyyy" is the format of the date picker.
     */
    val formattedShortDate = currentDate.format(shortMonthFormat)
}
