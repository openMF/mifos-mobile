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

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

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
    const val SHORT_MONTH = "dd-MM-yyyy"

    const val MONTH_FORMAT = "dd MMMM"

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
        val stringBuilder = StringBuilder()
        stringBuilder.append(integersOfDate[2])
            .append(' ')
            .append(getMonthName(integersOfDate[1]))
            .append(' ')
            .append(integersOfDate[0])
        return stringBuilder.toString()
    }

    fun getDateAsString(integersOfDate: List<Long>, pattern: String): String {
        return getFormatConverter(
            currentFormat = FULL_MONTH,
            requiredFormat = pattern,
            dateString = getDateAsString(integersOfDate.map { it.toInt() }),
        )
    }

    /**
     * This Method converting the dd-MM-yyyy format type date string into dd MMMM yyyy
     *
     * @param format     Final Format of date string
     * @param dateString date string
     * @return dd MMMM yyyy format date string.
     */
    fun getSpecificFormat(format: String, dateString: String): String {
        val pickerFormat = shortMonthFormat
        val finalFormat = LocalDateTime.Format { byUnicodePattern(format) }

        return finalFormat.format(pickerFormat.parse(dateString))
    }

    private fun getFormatConverter(
        currentFormat: String,
        requiredFormat: String,
        dateString: String,
    ): String {
        val pickerFormat = LocalDateTime.Format { byUnicodePattern(currentFormat) }
        val finalFormat = LocalDateTime.Format { byUnicodePattern(requiredFormat) }

        return pickerFormat.parse(dateString).format(finalFormat)
    }

    /**
     * Gets the date string in the format "dd-MM-yyyy" from an array of integers representing the year, month, and day.
     *
     * @param dateComponents An array of three integers representing the year, month, and day, e.g. [2024, 11, 10]
     * @return The date string in the format "dd-MM-yyyy", e.g. "10-11-2024"
     */
    fun formatTransferDate(dateComponents: List<Int>, pattern: String = SHORT_MONTH): String {
        require(dateComponents.size == 3) { "dateComponents must have exactly 3 elements" }
        val (year, month, day) = dateComponents

        val localDate = LocalDate(year, Month(month), day)
        return localDate.format(pattern)
    }

    // Extension function to format LocalDate
    fun LocalDate.format(pattern: String): String {
        val year = this.year.toString().padStart(4, '0')
        val month = this.monthNumber.toString().padStart(2, '0')
        val day = this.dayOfMonth.toString().padStart(2, '0')

        return pattern
            .replace("yyyy", year)
            .replace("MM", month)
            .replace("dd", day)
    }

    /**
     * @param month an integer from 1 to 12
     * @return string representation of the month like Jan or Feb..etc
     */
    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> throw IllegalArgumentException("Month should be between 1 and 12")
        }
    }

    /**
     * Input timestamp string in milliseconds
     * Example timestamp "1698278400000"
     * Output examples: "dd-MM-yyyy" - "14-04-2016"
     */
    fun getDateAsStringFromLong(timeInMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(timeInMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        return instant.format(shortMonthFormat)
    }

    /**
     * Input timestamp string in milliseconds
     * Example timestamp "1698278400000"
     * Output examples: "14 April"
     */
    fun getMonthAsStringFromLong(timeInMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(timeInMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val monthName = instant.month.name.lowercase().capitalize()

        return "${instant.dayOfMonth} $monthName"
    }

    /**
     * Gets the date string in the format "day month year" from an array of integers representing the day and month.
     *
     * @param integersOfDate An array of two integers representing the day and month, e.g. [11, 10]
     * @return The date string in the format "day month year", e.g. "11 October"
     */
    fun getDateMonthString(integersOfDate: List<Int>): String {
        require(integersOfDate.size == 2) { "integersOfDate must have exactly 2 elements" }
        val (day, month) = integersOfDate

        val monthName = when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> throw IllegalArgumentException("Invalid month value: $month")
        }

        return "$day $monthName"
    }

    /**
     * Handles the specific format "yyyy-MM-dd HH:mm:ss.SSSSSS"
     * For example "2024-09-19 05:41:18.558995"
     * Possible outputs depending on current date:
     * "Today at 05:41"
     * "Tomorrow at 05:41"
     */
    fun String.toFormattedDateTime(): String {
        // Parse the datetime string
        val dateTime = try {
            // Split into date and time parts
            val (datePart, timePart) = this.split(" ")
            // Remove microseconds from time part
            val simplifiedTime = timePart.split(".")[0]
            // Combine date and simplified time
            val isoString = "${datePart}T$simplifiedTime"
            // Parse to LocalDateTime
            LocalDateTime.parse(isoString)
        } catch (e: Exception) {
            return this // Return original string if parsing fails
        }

        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val nowDateTime = now.toLocalDateTime(timeZone)

        return when {
            // Same year
            nowDateTime.year == dateTime.year -> {
                when {
                    // Same month
                    nowDateTime.monthNumber == dateTime.monthNumber -> {
                        when {
                            // Tomorrow
                            dateTime.dayOfMonth - nowDateTime.dayOfMonth == 1 -> {
                                "Tomorrow at ${dateTime.format()}"
                            }
                            // Today
                            dateTime.dayOfMonth == nowDateTime.dayOfMonth -> {
                                "Today at ${dateTime.format()}"
                            }
                            // Yesterday
                            nowDateTime.dayOfMonth - dateTime.dayOfMonth == 1 -> {
                                "Yesterday at ${dateTime.format()}"
                            }
                            // Same month but different day
                            else -> {
                                "${
                                    dateTime.month.name.lowercase().capitalize()
                                } ${dateTime.dayOfMonth}, ${dateTime.format()}"
                            }
                        }
                    }
                    // Different month, same year
                    else -> {
                        "${
                            dateTime.month.name.lowercase().capitalize()
                        } ${dateTime.dayOfMonth}, ${dateTime.format()}"
                    }
                }
            }
            // Different year
            else -> {
                "${
                    dateTime.month.name.lowercase().capitalize()
                } ${dateTime.dayOfMonth} ${dateTime.year}, ${dateTime.format()}"
            }
        }
    }

    /**
     * Input timestamp string in milliseconds
     * Example timestamp "1698278400000"
     * Output examples:
     * "Today at 12:00"
     * "Tomorrow at 15:30"
     */
    fun String.toPrettyDate(): String {
        val timestamp = this.toLong()
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val timeZone = TimeZone.currentSystemDefault()
        val nowDateTime = Clock.System.now().toLocalDateTime(timeZone)
        val neededDateTime = instant.toLocalDateTime(timeZone)

        return when {
            // Same year
            nowDateTime.year == neededDateTime.year -> {
                when {
                    // Same month
                    nowDateTime.monthNumber == neededDateTime.monthNumber -> {
                        when {
                            // Tomorrow
                            neededDateTime.dayOfMonth - nowDateTime.dayOfMonth == 1 -> {
                                val time = neededDateTime.format()
                                "Tomorrow at $time"
                            }
                            // Today
                            neededDateTime.dayOfMonth == nowDateTime.dayOfMonth -> {
                                val time = neededDateTime.format()
                                "Today at $time"
                            }
                            // Yesterday
                            nowDateTime.dayOfMonth - neededDateTime.dayOfMonth == 1 -> {
                                val time = neededDateTime.format()
                                "Yesterday at $time"
                            }
                            // Same month but different day
                            else -> {
                                "${
                                    neededDateTime.month.name.lowercase().capitalize()
                                } ${neededDateTime.dayOfMonth}, ${neededDateTime.format()}"
                            }
                        }
                    }
                    // Different month, same year
                    else -> {
                        "${
                            neededDateTime.month.name.lowercase().capitalize()
                        } ${neededDateTime.dayOfMonth}, ${neededDateTime.format()}"
                    }
                }
            }
            // Different year
            else -> {
                "${
                    neededDateTime.month.name.lowercase().capitalize()
                } ${neededDateTime.dayOfMonth} ${neededDateTime.year}, ${neededDateTime.format()}"
            }
        }
    }

    // Helper function to format time
    private fun LocalDateTime.format(): String {
        return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
    }

    // Extension to capitalize first letter
    private fun String.capitalize() = replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }

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

    fun getMonth(month: Int): String {
        require(month in 1..12) { "Month should be between 1 and 12" }
        return Month.entries[month - 1].name.lowercase().replaceFirstChar { it.uppercase() }
    }
}
