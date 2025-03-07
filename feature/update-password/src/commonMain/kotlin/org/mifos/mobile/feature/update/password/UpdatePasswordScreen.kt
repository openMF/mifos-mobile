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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.update_password.generated.resources.Res
import mifos_mobile.feature.update_password.generated.resources.confirm_password_error_validation_blank
import mifos_mobile.feature.update_password.generated.resources.confirm_password_error_validation_minimum_chars
import mifos_mobile.feature.update_password.generated.resources.could_not_update_password_error
import mifos_mobile.feature.update_password.generated.resources.dialog_action_ok
import mifos_mobile.feature.update_password.generated.resources.error_password_not_match
import mifos_mobile.feature.update_password.generated.resources.new_password_error_validation_blank
import mifos_mobile.feature.update_password.generated.resources.new_password_error_validation_minimum_chars
import mifos_mobile.feature.update_password.generated.resources.password_changed_successfully
import mifos_mobile.feature.update_password.generated.resources.update_password
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopBar
import org.mifos.mobile.core.ui.component.MifosProgressIndicator

@Composable
internal fun UpdatePasswordScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePasswordViewModel = koinViewModel(),
) {
    val uiState by viewModel.updatePasswordUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    UpdatePasswordScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        navigateBack = navigateBack,
        modifier = modifier,
        validateAndUpdatePassword = { params ->
            validateAndUpdatePassword(
                params = params,
                updateAccountPassword = viewModel::updateAccountPassword,
            )
        },
    )
}

@Composable
private fun UpdatePasswordScreen(
    uiState: UpdatePasswordUiState,
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    validateAndUpdatePassword: (PasswordValidationParams) -> Unit,
    modifier: Modifier = Modifier,
) {
    var updatePasswordButtonClicked by remember { mutableStateOf(false) }

    MifosScaffold(
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        topBar = {
            MifosTopBar(
                topBarTitle = stringResource(Res.string.update_password),
                backPress = navigateBack,
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
    when (uiState) {
        is UpdatePasswordUiState.Loading -> {
            MifosProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        is UpdatePasswordUiState.Error -> {
            if (updatePasswordButtonClicked) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = Res.string.could_not_update_password_error.toString(),
                        actionLabel = Res.string.dialog_action_ok.toString(),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }

        is UpdatePasswordUiState.Success -> {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = Res.string.password_changed_successfully.toString(),
                    actionLabel = Res.string.dialog_action_ok.toString(),
                    duration = SnackbarDuration.Short,
                )
                navigateBack()
            }
        }

        is UpdatePasswordUiState.Initial -> Unit
    }
}

private fun validateAndUpdatePassword(
    params: PasswordValidationParams,
    updateAccountPassword: (newPassword: String, confirmPassword: String) -> Unit,
) {
    with(params) {
        var newPasswordErrorContent = getPasswordError(newPassword, PasswordType.NEW)
        var confirmPasswordErrorContent = getPasswordError(confirmPassword, PasswordType.CONFIRM)

        setNewPasswordErrorContent(newPasswordErrorContent)
        setConfirmPasswordErrorContent(confirmPasswordErrorContent)

        when {
            newPasswordErrorContent == null && confirmPasswordErrorContent == null -> {
                if (newPassword == confirmPassword) {
                    updateAccountPassword(newPassword, confirmPassword)
                } else {
                    setPasswordDoesNotMatchOnBothError(Res.string.error_password_not_match)
                }
            }

            newPasswordErrorContent == null && confirmPasswordErrorContent != null -> {
                setConfirmPasswordError(true)
            }

            newPasswordErrorContent != null && confirmPasswordErrorContent == null -> {
                setNewPasswordError(true)
            }

            else -> {
                setNewPasswordError(true)
                setConfirmPasswordError(true)
            }
        }
    }
}

private enum class PasswordType {
    NEW,
    CONFIRM,
}

private fun getPasswordError(
    password: String,
    type: PasswordType,
): StringResource? {
    return when {
        password.isEmpty() -> when (type) {
            PasswordType.NEW -> Res.string.new_password_error_validation_blank
            PasswordType.CONFIRM -> Res.string.confirm_password_error_validation_blank
        }

        password.length < 6 -> when (type) {
            PasswordType.NEW -> Res.string.new_password_error_validation_minimum_chars
            PasswordType.CONFIRM -> Res.string.confirm_password_error_validation_minimum_chars
        }

        else -> null
    }
}
