/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model

object CountryFlagUtils {

    /**
     * Get country by phone number (auto-detect)
     */
    fun detectCountryFromPhoneNumber(phoneNumber: String): Country? {
        val cleanedNumber = phoneNumber.filter { it.isDigit() || it == '+' }

        // Sort by phone code length (longest first) for better matching
        val sortedCountries = worldCountries.sortedByDescending { it.phoneCode.length }

        for (country in sortedCountries) {
            val mobileRegex = Regex(country.mobilePattern)
            val landlineRegex = country.landlinePattern?.let { Regex(it) }

            if (mobileRegex.matches(cleanedNumber) ||
                landlineRegex?.matches(cleanedNumber) == true
            ) {
                return country
            }
        }
        return null
    }

    /**
     * Get all countries for dropdown/picker
     */
    fun getAllCountriesForSelection(): List<Country> {
        return worldCountries.sortedBy { it.name }
    }

    /**
     * Get popular countries first, then alphabetical
     */
    fun getCountriesWithPopularFirst(): List<Country> {
        val popularCodes = listOf("US", "GB", "ES", "FR", "DE", "IT", "CA", "AU")
        val popular = worldCountries.filter { it.code in popularCodes }
            .sortedBy { popularCodes.indexOf(it.code) }
        val others = worldCountries.filter { it.code !in popularCodes }
            .sortedBy { it.name }

        return popular + others
    }

    /**
     * Search countries by name or code
     */
    fun searchCountries(query: String): List<Country> {
        val lowercaseQuery = query.lowercase()
        return worldCountries.filter { country ->
            country.name.lowercase().contains(lowercaseQuery) ||
                country.code.lowercase().contains(lowercaseQuery) ||
                country.phoneCode.contains(query)
        }.sortedBy { it.name }
    }

    /**
     * Get flag emoji by country code
     */
    fun getFlagEmoji(countryCode: String): String? {
        return worldCountries.find {
            it.code.equals(countryCode, ignoreCase = true)
        }?.flagEmoji
    }

    /**
     * Get flag resource name by country code
     */
    fun getFlagResourceName(countryCode: String): String? {
        return worldCountries.find {
            it.code.equals(countryCode, ignoreCase = true)
        }?.flagResourceName
    }
}

val worldCountries: List<Country> = listOf(
    Country(
        code = "ES",
        name = "Spain",
        phoneCode = "+34",
        mobilePattern = "^(\\+34|0034|34)?[6789]\\d{8}$",
        landlinePattern = "^(\\+34|0034|34)?[89]\\d{8}$",
        specialPattern = "^(\\+34|0034|34)?[12345]\\d{8}$",
        formatExample = "+34 XXX XXX XXX",
        flagEmoji = "🇪🇸",
        flagResourceName = "flag_es",
    ),
    Country(
        code = "US",
        name = "United States",
        phoneCode = "+1",
        mobilePattern = "^(\\+1|001|1)?[2-9]\\d{9}$",
        landlinePattern = "^(\\+1|001|1)?[2-9]\\d{9}$",
        specialPattern = null,
        formatExample = "+1 XXX XXX XXXX",
        flagEmoji = "🇺🇸",
        flagResourceName = "flag_us",
    ),
    Country(
        code = "GB",
        name = "United Kingdom",
        phoneCode = "+44",
        mobilePattern = "^(\\+44|0044|44|0)?7[0-9]\\d{8}$",
        landlinePattern = "^(\\+44|0044|44|0)?[1-6,8-9]\\d{8,9}$",
        specialPattern = null,
        formatExample = "+44 XXXX XXXXXX",
        flagEmoji = "🇬🇧",
        flagResourceName = "flag_gb",
    ),

    // Additional European countries
    Country(
        code = "NL",
        name = "Netherlands",
        phoneCode = "+31",
        mobilePattern = "^(\\+31|0031|31|0)?6\\d{8}$",
        landlinePattern = "^(\\+31|0031|31|0)?[1-5,8-9]\\d{8}$",
        specialPattern = null,
        formatExample = "+31 X XXXX XXXX",
        flagEmoji = "🇳🇱",
        flagResourceName = "flag_nl",
    ),
    Country(
        code = "BE",
        name = "Belgium",
        phoneCode = "+32",
        mobilePattern = "^(\\+32|0032|32|0)?4[5-9]\\d{7}$",
        landlinePattern = "^(\\+32|0032|32|0)?[1-9]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+32 XXX XX XX XX",
        flagEmoji = "🇧🇪",
        flagResourceName = "flag_be",
    ),
    Country(
        code = "AT",
        name = "Austria",
        phoneCode = "+43",
        mobilePattern = "^(\\+43|0043|43|0)?6[5-9]\\d{7}$",
        landlinePattern = "^(\\+43|0043|43|0)?[1-9]\\d{6,12}$",
        specialPattern = null,
        formatExample = "+43 XXX XXXXXXX",
        flagEmoji = "🇦🇹",
        flagResourceName = "flag_at",
    ),
    Country(
        code = "CH",
        name = "Switzerland",
        phoneCode = "+41",
        mobilePattern = "^(\\+41|0041|41|0)?7[5-9]\\d{7}$",
        landlinePattern = "^(\\+41|0041|41|0)?[1-6,8-9]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+41 XX XXX XX XX",
        flagEmoji = "🇨🇭",
        flagResourceName = "flag_ch",
    ),
    Country(
        code = "SE",
        name = "Sweden",
        phoneCode = "+46",
        mobilePattern = "^(\\+46|0046|46|0)?7[0-9]\\d{7}$",
        landlinePattern = "^(\\+46|0046|46|0)?[1-6,8-9]\\d{6,9}$",
        specialPattern = null,
        formatExample = "+46 XX XXX XX XX",
        flagEmoji = "🇸🇪",
        flagResourceName = "flag_se",
    ),
    Country(
        code = "NO",
        name = "Norway",
        phoneCode = "+47",
        mobilePattern = "^(\\+47|0047|47)?[49]\\d{7}$",
        landlinePattern = "^(\\+47|0047|47)?[2-8]\\d{7}$",
        specialPattern = null,
        formatExample = "+47 XXX XX XXX",
        flagEmoji = "🇳🇴",
        flagResourceName = "flag_no",
    ),
    Country(
        code = "DK",
        name = "Denmark",
        phoneCode = "+45",
        mobilePattern = "^(\\+45|0045|45)?[2-9]\\d{7}$",
        landlinePattern = "^(\\+45|0045|45)?[3-9]\\d{7}$",
        specialPattern = null,
        formatExample = "+45 XX XX XX XX",
        flagEmoji = "🇩🇰",
        flagResourceName = "flag_dk",
    ),
    Country(
        code = "FI",
        name = "Finland",
        phoneCode = "+358",
        mobilePattern = "^(\\+358|00358|358|0)?4[0-9]\\d{7}$",
        landlinePattern = "^(\\+358|00358|358|0)?[1-3,5-9]\\d{6,11}$",
        specialPattern = null,
        formatExample = "+358 XX XXX XXXX",
        flagEmoji = "🇫🇮",
        flagResourceName = "flag_fi",
    ),
    Country(
        code = "PL",
        name = "Poland",
        phoneCode = "+48",
        mobilePattern = "^(\\+48|0048|48)?[4-8]\\d{8}$",
        landlinePattern = "^(\\+48|0048|48)?[1-3,5-9]\\d{8}$",
        specialPattern = null,
        formatExample = "+48 XXX XXX XXX",
        flagEmoji = "🇵🇱",
        flagResourceName = "flag_pl",
    ),
    Country(
        code = "CZ",
        name = "Czech Republic",
        phoneCode = "+420",
        mobilePattern = "^(\\+420|00420|420)?[6-7]\\d{8}$",
        landlinePattern = "^(\\+420|00420|420)?[2-5,8-9]\\d{8}$",
        specialPattern = null,
        formatExample = "+420 XXX XXX XXX",
        flagEmoji = "🇨🇿",
        flagResourceName = "flag_cz",
    ),
    Country(
        code = "HU",
        name = "Hungary",
        phoneCode = "+36",
        mobilePattern = "^(\\+36|0036|36)?[2-3,7]0\\d{7}$",
        landlinePattern = "^(\\+36|0036|36|0)?[1,2-9]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+36 XX XXX XXXX",
        flagEmoji = "🇭🇺",
        flagResourceName = "flag_hu",
    ),
    Country(
        code = "GR",
        name = "Greece",
        phoneCode = "+30",
        mobilePattern = "^(\\+30|0030|30)?69\\d{8}$",
        landlinePattern = "^(\\+30|0030|30)?[2]\\d{9}$",
        specialPattern = null,
        formatExample = "+30 XXX XXX XXXX",
        flagEmoji = "🇬🇷",
        flagResourceName = "flag_gr",
    ),
    Country(
        code = "PT",
        name = "Portugal",
        phoneCode = "+351",
        mobilePattern = "^(\\+351|00351|351)?9[1236]\\d{7}$",
        landlinePattern = "^(\\+351|00351|351)?[2]\\d{8}$",
        specialPattern = null,
        formatExample = "+351 XXX XXX XXX",
        flagEmoji = "🇵🇹",
        flagResourceName = "flag_pt",
    ),

    // Asian countries
    Country(
        code = "TH",
        name = "Thailand",
        phoneCode = "+66",
        mobilePattern = "^(\\+66|0066|66|0)?[6-9]\\d{8}$",
        landlinePattern = "^(\\+66|0066|66|0)?[2-7]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+66 XX XXX XXXX",
        flagEmoji = "🇹🇭",
        flagResourceName = "flag_th",
    ),
    Country(
        code = "VN",
        name = "Vietnam",
        phoneCode = "+84",
        mobilePattern = "^(\\+84|0084|84|0)?[3-9]\\d{8}$",
        landlinePattern = "^(\\+84|0084|84|0)?[2]\\d{9}$",
        specialPattern = null,
        formatExample = "+84 XXX XXX XXXX",
        flagEmoji = "🇻🇳",
        flagResourceName = "flag_vn",
    ),
    Country(
        code = "MY",
        name = "Malaysia",
        phoneCode = "+60",
        mobilePattern = "^(\\+60|0060|60|0)?1[0-9]\\d{7,8}$",
        landlinePattern = "^(\\+60|0060|60|0)?[3-9]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+60 XX XXXX XXXX",
        flagEmoji = "🇲🇾",
        flagResourceName = "flag_my",
    ),
    Country(
        code = "SG",
        name = "Singapore",
        phoneCode = "+65",
        mobilePattern = "^(\\+65|0065|65)?[8-9]\\d{7}$",
        landlinePattern = "^(\\+65|0065|65)?6\\d{7}$",
        specialPattern = null,
        formatExample = "+65 XXXX XXXX",
        flagEmoji = "🇸🇬",
        flagResourceName = "flag_sg",
    ),
    Country(
        code = "ID",
        name = "Indonesia",
        phoneCode = "+62",
        mobilePattern = "^(\\+62|0062|62|0)?8\\d{8,11}$",
        landlinePattern = "^(\\+62|0062|62|0)?[2-7]\\d{7,11}$",
        specialPattern = null,
        formatExample = "+62 XXX XXXX XXXX",
        flagEmoji = "🇮🇩",
        flagResourceName = "flag_id",
    ),
    Country(
        code = "PH",
        name = "Philippines",
        phoneCode = "+63",
        mobilePattern = "^(\\+63|0063|63|0)?9\\d{9}$",
        landlinePattern = "^(\\+63|0063|63|0)?[2-8]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+63 XXX XXX XXXX",
        flagEmoji = "🇵🇭",
        flagResourceName = "flag_ph",
    ),
    Country(
        code = "HK",
        name = "Hong Kong",
        phoneCode = "+852",
        mobilePattern = "^(\\+852|00852|852)?[5-9]\\d{7}$",
        landlinePattern = "^(\\+852|00852|852)?[2-3]\\d{7}$",
        specialPattern = null,
        formatExample = "+852 XXXX XXXX",
        flagEmoji = "🇭🇰",
        flagResourceName = "flag_hk",
    ),
    Country(
        code = "TW",
        name = "Taiwan",
        phoneCode = "+886",
        mobilePattern = "^(\\+886|00886|886|0)?9\\d{8}$",
        landlinePattern = "^(\\+886|00886|886|0)?[2-8]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+886 XXX XXX XXX",
        flagEmoji = "🇹🇼",
        flagResourceName = "flag_tw",
    ),

    // Middle Eastern countries
    Country(
        code = "AE",
        name = "UAE",
        phoneCode = "+971",
        mobilePattern = "^(\\+971|00971|971|0)?5[0-9]\\d{7}$",
        landlinePattern = "^(\\+971|00971|971|0)?[2-4,6-9]\\d{6,7}$",
        specialPattern = null,
        formatExample = "+971 XX XXX XXXX",
        flagEmoji = "🇦🇪",
        flagResourceName = "flag_ae",
    ),
    Country(
        code = "SA",
        name = "Saudi Arabia",
        phoneCode = "+966",
        mobilePattern = "^(\\+966|00966|966|0)?5\\d{8}$",
        landlinePattern = "^(\\+966|00966|966|0)?[1-4]\\d{7}$",
        specialPattern = null,
        formatExample = "+966 XX XXX XXXX",
        flagEmoji = "🇸🇦",
        flagResourceName = "flag_sa",
    ),
    Country(
        code = "IL",
        name = "Israel",
        phoneCode = "+972",
        mobilePattern = "^(\\+972|00972|972|0)?5[0-9]\\d{7}$",
        landlinePattern = "^(\\+972|00972|972|0)?[2-4,8-9]\\d{7}$",
        specialPattern = null,
        formatExample = "+972 XX XXX XXXX",
        flagEmoji = "🇮🇱",
        flagResourceName = "flag_il",
    ),
    Country(
        code = "TR",
        name = "Turkey",
        phoneCode = "+90",
        mobilePattern = "^(\\+90|0090|90|0)?5\\d{9}$",
        landlinePattern = "^(\\+90|0090|90|0)?[2-4]\\d{8}$",
        specialPattern = null,
        formatExample = "+90 XXX XXX XX XX",
        flagEmoji = "🇹🇷",
        flagResourceName = "flag_tr",
    ),

    // African countries
    Country(
        code = "ZA",
        name = "South Africa",
        phoneCode = "+27",
        mobilePattern = "^(\\+27|0027|27|0)?[6-8]\\d{8}$",
        landlinePattern = "^(\\+27|0027|27|0)?[1-5]\\d{8}$",
        specialPattern = null,
        formatExample = "+27 XX XXX XXXX",
        flagEmoji = "🇿🇦",
        flagResourceName = "flag_za",
    ),
    Country(
        code = "EG",
        name = "Egypt",
        phoneCode = "+20",
        mobilePattern = "^(\\+20|0020|20|0)?1[0-9]\\d{8}$",
        landlinePattern = "^(\\+20|0020|20|0)?[2-3]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+20 XXX XXX XXXX",
        flagEmoji = "🇪🇬",
        flagResourceName = "flag_eg",
    ),
    Country(
        code = "NG",
        name = "Nigeria",
        phoneCode = "+234",
        mobilePattern = "^(\\+234|00234|234|0)?[7-9]\\d{9}$",
        landlinePattern = "^(\\+234|00234|234|0)?[1-6]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+234 XXX XXX XXXX",
        flagEmoji = "🇳🇬",
        flagResourceName = "flag_ng",
    ),

    // South American countries
    Country(
        code = "AR",
        name = "Argentina",
        phoneCode = "+54",
        mobilePattern = "^(\\+54|0054|54)?9\\d{10}$",
        landlinePattern = "^(\\+54|0054|54|0)?11\\d{8}$",
        specialPattern = null,
        formatExample = "+54 XXX XXX XXXX",
        flagEmoji = "🇦🇷",
        flagResourceName = "flag_ar",
    ),
    Country(
        code = "CL",
        name = "Chile",
        phoneCode = "+56",
        mobilePattern = "^(\\+56|0056|56)?9\\d{8}$",
        landlinePattern = "^(\\+56|0056|56|0)?[2-5]\\d{7,8}$",
        specialPattern = null,
        formatExample = "+56 X XXXX XXXX",
        flagEmoji = "🇨🇱",
        flagResourceName = "flag_cl",
    ),
    Country(
        code = "CO",
        name = "Colombia",
        phoneCode = "+57",
        mobilePattern = "^(\\+57|0057|57)?3\\d{9}$",
        landlinePattern = "^(\\+57|0057|57|0)?[1-8]\\d{7}$",
        specialPattern = null,
        formatExample = "+57 XXX XXX XXXX",
        flagEmoji = "🇨🇴",
        flagResourceName = "flag_co",
    ),
    Country(
        code = "PE",
        name = "Peru",
        phoneCode = "+51",
        mobilePattern = "^(\\+51|0051|51)?9\\d{8}$",
        landlinePattern = "^(\\+51|0051|51|0)?[1-7]\\d{6,7}$",
        specialPattern = null,
        formatExample = "+51 XXX XXX XXX",
        flagEmoji = "🇵🇪",
        flagResourceName = "flag_pe",
    ),

    // Other notable countries
    Country(
        code = "NZ",
        name = "New Zealand",
        phoneCode = "+64",
        mobilePattern = "^(\\+64|0064|64|0)?[2-4]\\d{7,8}$",
        landlinePattern = "^(\\+64|0064|64|0)?[3-9]\\d{7}$",
        specialPattern = null,
        formatExample = "+64 XX XXX XXXX",
        flagEmoji = "🇳🇿",
        flagResourceName = "flag_nz",
    ),
    Country(
        code = "IE",
        name = "Ireland",
        phoneCode = "+353",
        mobilePattern = "^(\\+353|00353|353|0)?8[5-9]\\d{7}$",
        landlinePattern = "^(\\+353|00353|353|0)?[1-9]\\d{6,9}$",
        specialPattern = null,
        formatExample = "+353 XX XXX XXXX",
        flagEmoji = "🇮🇪",
        flagResourceName = "flag_ie",
    ),
)
