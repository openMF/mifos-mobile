/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateHelper {

    private val LOG_TAG: String? = DateHelper::class.java.simpleName
    const val FORMAT_MMMM = "dd MMMM yyyy"
    const val FORMAT_MM = "dd-MM-yyyy"
    fun getCurrentDate(dateFormat: String?, separator: String): List<Int> {
        val date: MutableList<Int> = ArrayList()
        val s = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date())
        for (str in s.split(separator.toRegex()).toTypedArray()) {
            date.add(str.toInt())
        }
        return date
    }

    /**
     * the result string uses the list given in a reverse order ([x, y, z] results in "z y x")
     *
     * @param integersOfDate [year-month-day] (ex [2016, 4, 14])
     * @return date in the format day month year (ex 14 Apr 2016)
     */
    fun getDateAsString(integersOfDate: List<Int?>?): String {
        val stringBuilder = StringBuilder()
        if (integersOfDate != null && integersOfDate.size >= 3) {
            stringBuilder.append(integersOfDate[2])
                .append(' ')
                .append(getMonthName(integersOfDate[1]))
                .append(' ')
                .append(integersOfDate[0])
        }
        return stringBuilder.toString()
    }

    fun getDateAsString(integersOfDate: List<Int>?, pattern: String?): String {
        return getFormatConverter(
            "dd MMM yyyy",
            pattern,
            getDateAsString(integersOfDate),
        )
    }

    /**
     * This Method converting the dd-MM-yyyy format type date string into dd MMMM yyyy
     *
     * @param format     Final Format of date string
     * @param dateString date string
     * @return dd MMMM yyyy format date string.
     */
    fun getSpecificFormat(format: String?, dateString: String?): String {
        val pickerFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val finalFormat = SimpleDateFormat(format, Locale.ENGLISH)
        var date: Date? = null
        try {
            date = pickerFormat.parse(dateString)
        } catch (e: ParseException) {
            Log.d(LOG_TAG, e.localizedMessage)
        }
        return finalFormat.format(date)
    }

    fun getSpecificFormat(format: String?, dateLong: Long?): String? {
        try {
            return SimpleDateFormat(format, Locale.getDefault()).format(dateLong)
        } catch (e: ParseException) {
            Log.d(LOG_TAG, e.localizedMessage)
            return null
        }
    }

    fun getFormatConverter(
        currentFormat: String?,
        requiredFormat: String?,
        dateString: String?,
    ): String {
        val pickerFormat = SimpleDateFormat(currentFormat, Locale.ENGLISH)
        val finalFormat = SimpleDateFormat(requiredFormat, Locale.ENGLISH)
        var date: Date? = null
        try {
            date = pickerFormat.parse(dateString)
        } catch (e: ParseException) {
            Log.d(LOG_TAG, e.localizedMessage)
        }
        return finalFormat.format(date)
    }

    /**
     * @param month an integer from 1 to 12
     * @return string representation of the month like Jan or Feb..etc
     */
    private fun getMonthName(month: Int?): String {
        var monthName = ""
        when (month) {
            1 -> monthName = "Jan"
            2 -> monthName = "Feb"
            3 -> monthName = "Mar"
            4 -> monthName = "Apr"
            5 -> monthName = "May"
            6 -> monthName = "Jun"
            7 -> monthName = "Jul"
            8 -> monthName = "Aug"
            9 -> monthName = "Sep"
            10 -> monthName = "Oct"
            11 -> monthName = "Nov"
            12 -> monthName = "Dec"
        }
        return monthName
    }

    fun getDateAsLongFromString(dateStr: String?, pattern: String?): Long? {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(dateStr)
        } catch (e: ParseException) {
            Log.d("TAG", e.message ?: "")
        }
        return date?.time
    }

    @JvmStatic
    fun getDateAsLongFromList(integersOfDate: List<Int>?): Long? {
        val dateStr = getDateAsString(integersOfDate)
        return getDateAsLongFromString(dateStr, "dd MMM yyyy")
    }

    fun subtractWeeks(number: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -number)
        return calendar.timeInMillis
    }

    fun subtractMonths(number: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -number)
        return calendar.timeInMillis
    }

    fun getDateAsStringFromLong(timeInMillis: Long?): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(timeInMillis?.let { Date(it) })
    }

    @JvmStatic
    fun getDateAndTimeAsStringFromLong(timeInMillis: Long?): String {
        val sdf = SimpleDateFormat("HH:mm a dd MMM yyyy", Locale.getDefault())
        return sdf.format(timeInMillis?.let { Date(it) })
    }
}

fun getTodayFormatted(): String =
    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
