/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.update.password

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.icons.MifosIcons

@Composable
internal fun UpdatePasswordContent(
    updatePasswordButtonClicked: () -> Unit,
    validateAndUpdatePassword: (PasswordValidationParams) -> Unit,
    modifier: Modifier = Modifier,
) {
    var newPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var confirmPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var newPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var newPasswordError by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordError by rememberSaveable { mutableStateOf(false) }

    var newPasswordErrorContent by rememberSaveable { mutableStateOf("") }
    var confirmPasswordErrorContent by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PasswordTextField(
            value = newPassword,
            onValueChange = {
                newPassword = it
                newPasswordError = false
            },
            label = R.string.new_password,
            errorContent = newPasswordErrorContent,
            isError = newPasswordError,
            isVisible = newPasswordVisible,
            onVisibilityChange = { newPasswordVisible = it },
        )

        PasswordTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = false
            },
            label = R.string.confirm_password,
            errorContent = confirmPasswordErrorContent,
            isError = confirmPasswordError,
            isVisible = confirmPasswordVisible,
            onVisibilityChange = { confirmPasswordVisible = it },
        )

        UpdatePasswordButton(
            onClick = {
                keyboardController?.hide()
                updatePasswordButtonClicked()
                validateAndUpdatePassword(
                    PasswordValidationParams(
                        newPassword = newPassword.text,
                        confirmPassword = confirmPassword.text,
                        setNewPasswordError = { newPasswordError = it },
                        setConfirmPasswordError = { confirmPasswordError = it },
                        setNewPasswordErrorContent = { newPasswordErrorContent = it },
                        setConfirmPasswordErrorContent = { confirmPasswordErrorContent = it },
                    ),
                )
            },
        )
    }
}

@Composable
private fun PasswordTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    @StringRes label: Int,
    errorContent: String,
    isError: Boolean,
    isVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        supportingText = errorContent,
        icon = R.drawable.feature_update_password_lock,
        trailingIcon = {
            val image = if (isVisible) MifosIcons.Visibility else MifosIcons.VisibilityOff
            if (!isError) {
                IconButton(onClick = { onVisibilityChange(!isVisible) }) {
                    Icon(imageVector = image, contentDescription = "password visibility button")
                }
            } else {
                Icon(imageVector = MifosIcons.Error, contentDescription = null)
            }
        },
        error = isError,
        visualTransformation = if (isVisible) None else PasswordVisualTransformation(),
        keyboardType = KeyboardType.Password,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
}

@Composable
private fun UpdatePasswordButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Text(text = stringResource(id = R.string.change_password))
    }
}

data class PasswordValidationParams(
    val newPassword: String,
    val confirmPassword: String,
    val setNewPasswordError: (Boolean) -> Unit,
    val setConfirmPasswordError: (Boolean) -> Unit,
    val setNewPasswordErrorContent: (String) -> Unit,
    val setConfirmPasswordErrorContent: (String) -> Unit,
)
