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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.update_password.generated.resources.Res
import mifos_mobile.feature.update_password.generated.resources.confirm_password
import mifos_mobile.feature.update_password.generated.resources.could_not_update_password_error
import mifos_mobile.feature.update_password.generated.resources.dialog_action_ok
import mifos_mobile.feature.update_password.generated.resources.error_validation_blank
import mifos_mobile.feature.update_password.generated.resources.error_validation_minimum_chars
import mifos_mobile.feature.update_password.generated.resources.new_password
import mifos_mobile.feature.update_password.generated.resources.no_internet_connection
import mifos_mobile.feature.update_password.generated.resources.password_changed_successfully
import mifos_mobile.feature.update_password.generated.resources.update_password
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
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
    val scope = rememberCoroutineScope()
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
                showSnackBar = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            it,
                            Res.string.dialog_action_ok.toString(),
                            duration = SnackbarDuration.Short
                        )
                    }
                },
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
    showSnackBar: (message: String) -> Unit,
) {
    with(params) {
        val newPasswordErrorContent =
            getPasswordError(newPassword, Res.string.new_password.toString())
        val confirmPasswordErrorContent =
            getPasswordError(confirmPassword, Res.string.confirm_password.toString())

        setNewPasswordErrorContent(newPasswordErrorContent)
        setConfirmPasswordErrorContent(confirmPasswordErrorContent)

        when {
            newPasswordErrorContent.isEmpty() && confirmPasswordErrorContent.isEmpty() -> {
                if (newPassword == confirmPassword) {
                    updateAccountPassword(newPassword, confirmPassword)
                } else {
                    showSnackBar.invoke("Test password does not match")
//                    Res.string.error_password_not_match.toString()
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
): String = when {
    password.isEmpty() -> password + Res.string.error_validation_blank
    password.length < 6 -> password +  Res.string.error_validation_minimum_chars
    else -> ""
}

object ResourceManager {
    val ERROR_VALIDATION_BLANK = stringResource(Res.string.error_validation_minimum_chars)
    const val ERROR_VALIDATION_MINIMUM_CHARS = "Password must be at least 6 characters"
}