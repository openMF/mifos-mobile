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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.update_password.generated.resources.Res
import mifos_mobile.feature.update_password.generated.resources.change_password
import mifos_mobile.feature.update_password.generated.resources.confirm_password
import mifos_mobile.feature.update_password.generated.resources.new_password
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons

@Composable
internal fun UpdatePasswordContent(
    state: EditPasswordState,
    modifier: Modifier = Modifier,
    onAction: (EditPasswordAction) -> Unit,
) {


    var newPasswordError by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordError by rememberSaveable { mutableStateOf(false) }

    var newPasswordErrorContent: StringResource? by rememberSaveable { mutableStateOf(null) }
    var confirmPasswordErrorContent: StringResource? by rememberSaveable { mutableStateOf(null) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var newPasswordVisible by rememberSaveable { mutableStateOf(false) }
        var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

        PasswordTextField(
            value = state.newPasswordInput,
            onValueChange = {
                onAction(EditPasswordAction.NewPasswordChange(it))
            },
            label = stringResource(Res.string.new_password),
            errorContent = newPasswordErrorContent?.let { stringResource(it) } ?: "",
            isError = newPasswordError,
            isVisible = newPasswordVisible,
            onVisibilityChange = { newPasswordVisible = it },
        )

        PasswordTextField(
            value = state.confirmPasswordInput,
            onValueChange = {
                onAction(EditPasswordAction.ConfirmPasswordChange(it))
            },
            label = stringResource(Res.string.confirm_password),
            errorContent = confirmPasswordErrorContent?.let { stringResource(it) } ?: "",
            isError = confirmPasswordError,
            isVisible = confirmPasswordVisible,
            onVisibilityChange = { confirmPasswordVisible = it },
        )

        UpdatePasswordButton(
            onClick = {
                keyboardController?.hide()
                onAction(EditPasswordAction.SubmitClick)
            },
        )
    }
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorContent: String,
    isError: Boolean,
    isVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mifosTextFieldConfig = MifosTextFieldConfig(
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
        isError = isError,
        visualTransformation = if (isVisible) None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        errorText = errorContent,
    )

    MifosOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        config = mifosTextFieldConfig,
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
        Text(text = stringResource(Res.string.change_password))
    }
}

data class PasswordValidationParams(
    val newPassword: String,
    val confirmPassword: String,
    val setNewPasswordError: (Boolean) -> Unit,
    val setConfirmPasswordError: (Boolean) -> Unit,
    val setNewPasswordErrorContent: (StringResource?) -> Unit,
    val setConfirmPasswordErrorContent: (StringResource?) -> Unit,
    val setPasswordDoesNotMatchOnBothError: (StringResource?) -> Unit,
)
