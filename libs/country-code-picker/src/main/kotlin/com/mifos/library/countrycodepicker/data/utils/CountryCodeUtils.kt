/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package com.mifos.library.countrycodepicker.data.utils

import android.content.Context
import android.telephony.TelephonyManager
import com.mifos.library.countrycodepicker.data.CountryData
import com.mifos.library.countrycodepicker.data.Iso31661alpha2
import com.mifos.library.countrycodepicker.data.PhoneCode
import kotlin.math.min

private const val EMOJI_UNICODE = 0x1F1A5
private const val MAX_LENGTH_COUNTRY_CODE = 3

internal fun getCountryFromPhoneCode(code: PhoneCode, context: Context): CountryData? {
    val countries = CountryData.entries.filter { it.countryPhoneCode == code }
    return when (countries.size) {
        0 -> null
        1 -> countries.firstOrNull()
        else -> {
            val userIso = getUserIsoCode(context)
            countries.firstOrNull { it.countryIso == userIso }
                ?: if (code == "+1") CountryData.UnitedStates else countries.firstOrNull()
        }
    }
}

@Suppress("SwallowedException", "TooGenericExceptionCaught", "Deprecation")
internal fun getUserIsoCode(context: Context): Iso31661alpha2 = try {
    val telephonyManager = context.telephonyManager
    telephonyManager?.networkCountryIso ?: telephonyManager?.simCountryIso
} catch (ex: Exception) {
    null
}.takeIf { !it.isNullOrBlank() } ?: context.resources.configuration.locale.country

val CountryData.emojiFlag: String get() = countryCodeToEmojiFlag(countryIso)

fun countryCodeToEmojiFlag(countryCode: Iso31661alpha2): String =
    countryCode
        .uppercase()
        .map { char -> Character.codePointAt("$char", 0) + EMOJI_UNICODE }
        .joinToString("") { String(Character.toChars(it)) }

private val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

internal fun extractCountryCode(fullNumber: String): String? {
    val numberLength = fullNumber.length
    val minLength = min(a = numberLength, b = MAX_LENGTH_COUNTRY_CODE)
    for (i in 1..minLength) {
        val potentialCountryCode = fullNumber.substring(0, i)
        if (CountryData.phoneCodeMap.containsKey(potentialCountryCode)) {
            return potentialCountryCode
        }
    }
    return null
}
