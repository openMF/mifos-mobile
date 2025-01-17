/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

@Composable
fun MifosSearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSearchDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        value = value,
        placeholder = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.DarkGray,
            )
        },
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge,
        trailingIcon = {
            AnimatedVisibility(visible = value.text.isNotEmpty()) {
                IconButton(onClick = onSearchDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
            unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
            },
        ),
        singleLine = true,
    )
}

@Composable
private fun MifosSearchTextFieldPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        Surface {
            MifosSearchTextField(
                value = TextFieldValue("Search"),
                onValueChange = {},
                onSearchDismiss = {},
                modifier = modifier,
            )
        }
    }
}
