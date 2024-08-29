/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosOutlinedTextField(
    label: Int,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    maxLines: Int = 1,
    singleLine: Boolean = true,
    icon: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: Boolean = false,
    supportingText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
    ),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = label)) },
        modifier = modifier,
        leadingIcon = if (icon != null) {
            {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    colorFilter = if (isSystemInDarkTheme()) {
                        ColorFilter.tint(Color.White)
                    } else {
                        ColorFilter.tint(Color.Black)
                    },
                )
            }
        } else {
            null
        },
        trailingIcon = trailingIcon,
        maxLines = maxLines,
        singleLine = singleLine,
        colors = colors,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = LocalDensity.current.run {
            TextStyle(fontSize = 18.sp)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType,
        ),
        visualTransformation = visualTransformation,
        isError = error,
        supportingText = {
            if (error) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = supportingText ?: "",
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                null
            }
        },
    )
}
