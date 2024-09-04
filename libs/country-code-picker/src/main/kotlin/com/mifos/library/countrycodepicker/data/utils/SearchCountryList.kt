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
import com.mifos.library.countrycodepicker.R
import com.mifos.library.countrycodepicker.data.CountryData

internal fun List<CountryData>.searchCountry(key: String, context: Context): List<CountryData> =
    this.mapNotNull { countryData ->
        countryNames[countryData.countryIso]?.let { countryName ->
            val localizedCountryName = context.resources.getString(countryName).lowercase()
            if (localizedCountryName.contains(key.lowercase())) {
                countryData to localizedCountryName
            } else {
                null
            }
        }
    }
        .partition { it.second.startsWith(key.lowercase()) }
        .let { (startWith, contains) ->
            startWith.map { it.first } + contains.map { it.first }
        }

internal fun List<CountryData>.sortedByLocalizedName(context: Context): List<CountryData> =
    this.sortedBy {
        context.resources.getString(
            countryNames.getOrDefault(it.countryIso, R.string.unknown),
        )
    }
