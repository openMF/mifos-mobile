/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import template.core.base.designsystem.theme.KptTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun MifosOutlineDropdown(
    selectedText: String,
    items: Map<Long, String>,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onItemSelected: (Long, String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier,
    ) {
        MifosOutlinedTextField(
            value = selectedText,
            onValueChange = {},
            label = label,
            config = MifosTextFieldConfig(
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(DesignToken.sizes.iconSmall),
                        imageVector = if (expanded) MifosIcons.CaretUp else MifosIcons.CaretDown,
                        contentDescription = null,
                        tint = if (enabled) {
                            KptTheme.colorScheme.onSurface
                        } else {
                            KptTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        },
                    )
                },
                showClearIcon = false,
                readOnly = true,
                enabled = enabled,
            ),

            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .onGloballyPositioned {
                    textFieldSize = it.size.toSize()
                }
                .then(if (enabled) Modifier.clickable { expanded = true } else Modifier),
            shape = KptTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KptTheme.colorScheme.secondaryContainer,
                unfocusedBorderColor = KptTheme.colorScheme.secondaryContainer,
                errorBorderColor = KptTheme.colorScheme.error,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .border(
                    width = DesignToken.strokes.thin,
                    color = KptTheme.colorScheme.secondaryContainer,
                    shape = KptTheme.shapes.medium,
                ),
            tonalElevation = DesignToken.elevation.dp2,
            shadowElevation = DesignToken.elevation.dp2,
            containerColor = KptTheme.colorScheme.inverseOnSurface,
            shape = KptTheme.shapes.large,
        ) {
            items.forEach { (productID, product) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = product,
                            style = KptTheme.typography.bodyLarge,
                            color = KptTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(productID, product)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun MifosOutlinedDropdownPreview() {
    var selected by remember { mutableStateOf("") }
    var selectedProduct by remember { mutableStateOf(0L) }

    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(KptTheme.spacing.md),
        ) {
            MifosOutlineDropdown(
                selectedText = selected,
                items = mapOf(
                    1L to "New Product",
                    2L to "Savings Account",
                    3L to "Fixed Deposit",
                ),
                onItemSelected = { id, product ->
                    selectedProduct = id
                    selected = product
                },
                label = "New Product",
            )
        }
    }
}
