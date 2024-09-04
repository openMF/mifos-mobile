/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package com.mifos.library.countrycodepicker.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mifos.library.countrycodepicker.data.CountryData
import com.mifos.library.countrycodepicker.data.utils.emojiFlag
import com.mifos.library.countrycodepicker.data.utils.sortedByLocalizedName
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList

internal val DEFAULT_PADDING = 10.dp

@Composable
internal fun CountryCodeDialog(
    selectedCountry: CountryData,
    includeOnly: ImmutableSet<String>?,
    onCountryChange: (CountryData) -> Unit,
    showCountryCode: Boolean,
    showFlag: Boolean,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    val context = LocalContext.current

    var country by remember { mutableStateOf(selectedCountry) }
    var isOpenDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val countryList by remember(context, includeOnly) {
        derivedStateOf {
            val allCountries = CountryData.entries.sortedByLocalizedName(context)
            includeOnly?.run {
                val includeUppercase = map { it.uppercase() }
                allCountries.filter { it.countryIso in includeUppercase }
            } ?: allCountries
        }
    }

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                isOpenDialog = true
            },
    ) {
        CountryRow(
            modifier = Modifier.padding(DEFAULT_PADDING),
            showCountryCode = showCountryCode,
            showFlag = showFlag,
            country = country,
            textStyle = textStyle,
        )

        if (isOpenDialog) {
            CountryDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(DEFAULT_ROUNDING)),
                onDismissRequest = { isOpenDialog = false },
                onSelect = { countryItem ->
                    onCountryChange(countryItem)
                    country = countryItem
                    isOpenDialog = false
                },
                countryList = countryList.toImmutableList(),
                textStyle = if (textStyle.color != Color.Unspecified) {
                    textStyle
                } else {
                    textStyle.copy(color = MaterialTheme.colorScheme.onSurface)
                },
                backgroundColor = backgroundColor,
            )
        }
    }
}

@Composable
private fun CountryRow(
    showCountryCode: Boolean,
    showFlag: Boolean,
    country: CountryData,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(
        text = emojiCodeText(
            showFlag = showFlag,
            isPickCountry = country,
            showCountryCode = showCountryCode,
        ),
        modifier = Modifier.padding(start = DEFAULT_PADDING),
        style = textStyle.copy(color = textStyle.color.copy(alpha = 1f)),
    )
    Icon(
        imageVector = Icons.Default.ArrowDropDown,
        contentDescription = null,
        tint = textStyle.color,
    )
}

@Composable
private fun emojiCodeText(
    showFlag: Boolean,
    isPickCountry: CountryData,
    showCountryCode: Boolean,
) = (if (showFlag) isPickCountry.emojiFlag else "") +
    (if (showCountryCode && showFlag) "  " else "") +
    (if (showCountryCode) isPickCountry.countryPhoneCode else "")

@Preview
@Composable
private fun CountryCodeDialogPreview() {
    CountryCodeDialog(
        selectedCountry = CountryData.UnitedStates,
        includeOnly = null,
        onCountryChange = {},
        showCountryCode = true,
        showFlag = true,
        textStyle = TextStyle(),
        backgroundColor = Color.White,
    )
}
