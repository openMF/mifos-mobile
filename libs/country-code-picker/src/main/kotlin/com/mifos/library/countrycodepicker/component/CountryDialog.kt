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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mifos.library.countrycodepicker.R
import com.mifos.library.countrycodepicker.data.CountryData
import com.mifos.library.countrycodepicker.data.utils.countryNames
import com.mifos.library.countrycodepicker.data.utils.emojiFlag
import com.mifos.library.countrycodepicker.data.utils.searchCountry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

internal val DEFAULT_ROUNDING = 10.dp
private val DEFAULT_ROW_PADDING = 16.dp
private const val ROW_PADDING_VERTICAL_SCALING = 1.1f
private val SEARCH_ICON_PADDING = 5.dp
private const val HEADER_TEXT_SIZE_MULTIPLE = 1.5
private val MIN_TAP_DIMENSION = 48.dp
private const val DIVIDER_ALPHA = 0.12f

/**
 * @param onDismissRequest Executes when the user tries to dismiss the dialog.
 * @param onSelect Executes when the user selects a country from the list.
 * @param textStyle A [TextStyle] for customizing text style of search input field and country rows.
 * @param modifier The modifier to be applied to the dialog surface.
 * @param countryList The list of countries to display in the dialog.
 * @param rowPadding The padding to be applied to each row.
 * @param backgroundColor The [Color] of the dialog background.
 * @param dividerColor The [Color] of the country row dividers.
 */
@Composable
fun CountryDialog(
    onDismissRequest: () -> Unit,
    onSelect: (item: CountryData) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    countryList: ImmutableList<CountryData> = persistentListOf(),
    rowPadding: Dp = DEFAULT_ROW_PADDING,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    dividerColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = DIVIDER_ALPHA),
) {
    val context = LocalContext.current
    var searchValue by rememberSaveable { mutableStateOf("") }
    val filteredCountries by remember(context, searchValue) {
        derivedStateOf {
            if (searchValue.isEmpty()) {
                countryList
            } else {
                countryList.searchCountry(
                    searchValue,
                    context,
                )
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            @Suppress("ReusedModifierInstance")
            Surface(
                color = backgroundColor,
                modifier = modifier,
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    HeaderRow(textStyle, onDismissRequest)

                    SearchTextField(
                        value = searchValue,
                        onValueChange = { searchValue = it },
                        textStyle = textStyle,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(id = R.string.search),
                                tint = textStyle.color,
                                modifier = Modifier.padding(horizontal = SEARCH_ICON_PADDING),
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(DEFAULT_ROW_PADDING))

                    LazyColumn {
                        items(
                            filteredCountries,
                            key = { it.countryIso },
                        ) { countryItem ->
                            HorizontalDivider(color = dividerColor)
                            CountryRowItem(
                                rowPadding = rowPadding,
                                onSelect = { onSelect(countryItem) },
                                countryItem = countryItem,
                                textStyle = textStyle,
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun HeaderRow(
    textStyle: TextStyle,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(DEFAULT_ROW_PADDING))
        Text(
            text = stringResource(id = R.string.select_country),
            style = textStyle.copy(
                fontSize = textStyle
                    .fontSize
                    .takeIf { it != TextUnit.Unspecified }
                    ?.let { it * HEADER_TEXT_SIZE_MULTIPLE }
                    ?: MaterialTheme.typography.labelSmall.fontSize,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onDismissRequest() },
        ) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Close",
                tint = textStyle.color,
            )
        }
    }
}

@Composable
private fun CountryRowItem(
    rowPadding: Dp,
    onSelect: () -> Unit,
    countryItem: CountryData,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onSelect() })
            .padding(
                horizontal = rowPadding,
                vertical = rowPadding * ROW_PADDING_VERTICAL_SCALING,
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = countryItem.emojiFlag + "  " +
                stringResource(
                    id = countryNames.getOrDefault(
                        countryItem.countryIso,
                        R.string.unknown,
                    ),
                ),
            style = textStyle,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    hint: String = stringResource(id = R.string.search),
) {
    val requester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        requester.requestFocus()
    }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(MIN_TAP_DIMENSION)
            .padding(horizontal = DEFAULT_ROW_PADDING)
            .focusRequester(requester),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(textStyle.color),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.invoke()
                Box(
                    modifier = Modifier
                        .padding(start = DEFAULT_ROUNDING)
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            maxLines = 1,
                            style = textStyle.copy(color = textStyle.color.copy(alpha = 0.5f)),
                        )
                    }
                    innerTextField()
                }
            }
        },
    )
}

@Preview
@Composable
private fun CountryDialogPreview() {
    CountryDialog(
        onDismissRequest = {},
        onSelect = {},
        countryList = CountryData.entries.toImmutableList(),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(DEFAULT_ROUNDING)),
        textStyle = TextStyle(),
    )
}
