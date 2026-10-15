/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.retry
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview
import template.core.base.designsystem.theme.KptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosDropDownTextField(
    onClick: (Int, String) -> Unit,
    labelResId: StringResource,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    supportingText: String? = null,
    error: Boolean = false,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    optionsList: List<String> = listOf(),
    selectedOption: String? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && isEnabled,
        onExpandedChange = {
            if (isEnabled) {
                expanded = !expanded
            }
        },
        modifier = modifier.alpha(if (!isEnabled) 0.4f else 1f).fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selectedOption ?: "",
            shape = shape,
            onValueChange = { },
            label = { Text(stringResource(labelResId)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            enabled = isEnabled,
            textStyle = KptTheme.typography.labelMedium,
            supportingText = { if (error) Text(text = supportingText ?: "") },
            isError = error,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) {
                        MifosIcons.ArrowDropUp
                    } else {
                        MifosIcons.ArrowDropDown
                    },
                    contentDescription = if (expanded) {
                        "Arrow Up Icon"
                    } else {
                        "Arrow Down Icon"
                    },
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KptTheme.colorScheme.secondaryContainer,
                unfocusedBorderColor = KptTheme.colorScheme.secondaryContainer,
                errorBorderColor = KptTheme.colorScheme.error,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded && isEnabled,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .heightIn(max = DesignToken.sizes.dropDownMenuHeightInDp200),
        ) {
            optionsList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onClick(index, item)
                    },
                    text = { Text(text = item) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosDropDownDoubleTextField(
    onClick: (Int, Pair<String, String>) -> Unit,
    labelResId: StringResource,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    supportingText: String? = null,
    error: Boolean = false,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    optionsList: List<Pair<String, String>> = listOf(),
    selectedOption: String? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && isEnabled,
        onExpandedChange = {
            if (isEnabled) {
                expanded = !expanded
            }
        },
        modifier = modifier.alpha(if (!isEnabled) 0.4f else 1f).fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = { },
            label = { Text(stringResource(labelResId)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            shape = shape,
            enabled = isEnabled,
            textStyle = KptTheme.typography.labelSmall,
            supportingText = { if (error) Text(text = supportingText ?: "") },
            isError = error,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) {
                        MifosIcons.ArrowDropUp
                    } else {
                        MifosIcons.ArrowDropDown
                    },
                    contentDescription = if (expanded) {
                        "Arrow Up Icon"
                    } else {
                        "Arrow Down Icon"
                    },
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KptTheme.colorScheme.secondaryContainer,
                unfocusedBorderColor = KptTheme.colorScheme.secondaryContainer,
                errorBorderColor = KptTheme.colorScheme.error,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded && isEnabled,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .heightIn(max = DesignToken.sizes.dropDownMenuHeightInDp200),
        ) {
            optionsList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onClick(index, item)
                    },
                    text = {
                        Column {
                            Text(text = item.first)
                            Text(text = item.second)
                        }
                    },
                )
            }
        }
    }
}

@DevicePreview
@Composable
fun MifosDropDownTextFieldPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosDropDownTextField(
            onClick = { _, _ -> },
            modifier = modifier,
            labelResId = Res.string.retry,
            isEnabled = true,
            supportingText = null,
            error = false,
            optionsList = listOf("Option 1", "Option 2", "Option 3"),
            selectedOption = null,
        )
    }
}

@DevicePreview
@Composable
fun MifosDropDownDoubleTextFieldPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosDropDownDoubleTextField(
            onClick = { _, _ -> },
            modifier = modifier,
            labelResId = Res.string.retry,
            isEnabled = true,
            supportingText = null,
            error = false,
            optionsList = listOf(
                Pair("Option 1", "Option 1 Description"),
                Pair("Option 2", "Option 2 Description"),
                Pair("Option 3", "Option 3 Description"),
            ),
            selectedOption = null,
        )
    }
}
