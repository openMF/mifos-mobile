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

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
internal fun UpdatePasswordScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.updatePasswordUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    UpdatePasswordScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        modifier = modifier,
        validateAndUpdatePassword = { params ->
            validateAndUpdatePassword(
                context = context,
                params = params,
                updateAccountPassword = viewModel::updateAccountPassword,
            )
        },
    )
}

@Composable
private fun UpdatePasswordScreen(
    uiState: UpdatePasswordUiState,
    navigateBack: () -> Unit,
    validateAndUpdatePassword: (PasswordValidationParams) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var updatePasswordButtonClicked by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
        topBar = {
            MifosTopBar(
                title = { Text(stringResource(R.string.change_password)) },
                navigateBack = navigateBack,
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            UpdatePasswordContent(
                updatePasswordButtonClicked = { updatePasswordButtonClicked = true },
                validateAndUpdatePassword = validateAndUpdatePassword,
            )

            HandleUpdatePasswordState(
                uiState = uiState,
                updatePasswordButtonClicked = updatePasswordButtonClicked,
                snackbarHostState = snackbarHostState,
                navigateBack = navigateBack,
            )
        }
    }
}

@Composable
@Suppress("ModifierMissing")
private fun HandleUpdatePasswordState(
    uiState: UpdatePasswordUiState,
    updatePasswordButtonClicked: Boolean,
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    when (uiState) {
        is UpdatePasswordUiState.Loading -> {
            MifosProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
            )
        }

        is UpdatePasswordUiState.Error -> {
            if (updatePasswordButtonClicked) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.could_not_update_password_error),
                        actionLabel = context.getString(R.string.dialog_action_ok),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }

        is UpdatePasswordUiState.Success -> {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.password_changed_successfully),
                    actionLabel = context.getString(R.string.dialog_action_ok),
                    duration = SnackbarDuration.Short,
                )
                navigateBack()
            }
        }

        is UpdatePasswordUiState.Initial -> Unit
    }
}

private fun validateAndUpdatePassword(
    context: Context,
    params: PasswordValidationParams,
    updateAccountPassword: (newPassword: String, confirmPassword: String) -> Unit,
) {
    with(params) {
        val newPasswordErrorContent =
            getPasswordError(newPassword, context.getString(R.string.new_password), context)
        val confirmPasswordErrorContent =
            getPasswordError(confirmPassword, context.getString(R.string.confirm_password), context)

        setNewPasswordErrorContent(newPasswordErrorContent)
        setConfirmPasswordErrorContent(confirmPasswordErrorContent)

        when {
            newPasswordErrorContent.isEmpty() && confirmPasswordErrorContent.isEmpty() -> {
                if (validatePasswordMatch(newPassword, confirmPassword) &&
                    Network.isConnected(context)
                ) {
                    updateAccountPassword(newPassword, confirmPassword)
                } else {
                    showErrorToast(context)
                }
            }

            newPasswordErrorContent.isEmpty() && confirmPasswordErrorContent.isNotEmpty() -> {
                setConfirmPasswordError(true)
            }

            newPasswordErrorContent.isNotEmpty() && confirmPasswordErrorContent.isEmpty() -> {
                setNewPasswordError(true)
            }

            else -> {
                setNewPasswordError(true)
                setConfirmPasswordError(true)
            }
        }
    }
}

private fun getPasswordError(
    password: String,
    passwordType: String,
    context: Context,
): String = when {
    password.isEmpty() -> context.getString(R.string.error_validation_blank, passwordType)
    password.length < context.resources.getInteger(R.integer.password_minimum_length) ->
        context.getString(
            R.string.error_validation_minimum_chars,
            passwordType,
            context.resources.getInteger(R.integer.password_minimum_length),
        )

    else -> ""
}

private fun showErrorToast(context: Context) {
    val errorMessage = if (!Network.isConnected(context)) {
        context.getString(R.string.no_internet_connection)
    } else {
        context.getString(R.string.error_password_not_match)
    }
    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
}

private fun validatePasswordMatch(newPassword: String, confirmPassword: String): Boolean {
    return newPassword == confirmPassword
}

internal class UiStatesParameterProvider : PreviewParameterProvider<UpdatePasswordUiState> {
    override val values: Sequence<UpdatePasswordUiState>
        get() = sequenceOf(
            UpdatePasswordUiState.Initial,
            UpdatePasswordUiState.Error(1),
            UpdatePasswordUiState.Loading,
            UpdatePasswordUiState.Success,
        )
}

@Composable
@DevicePreviews
private fun UpdatePasswordScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class)
    updatePasswordUiState: UpdatePasswordUiState,
) {
    MifosMobileTheme {
        UpdatePasswordScreen(
            uiState = updatePasswordUiState,
            navigateBack = {},
            validateAndUpdatePassword = {
                PasswordValidationParams(
                    newPassword = "",
                    confirmPassword = "",
                    setNewPasswordError = {},
                    setConfirmPasswordError = {},
                    setNewPasswordErrorContent = {},
                    setConfirmPasswordErrorContent = {},
                )
            },
        )
    }
}
