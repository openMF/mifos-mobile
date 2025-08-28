/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.setNewPassword

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_set_new_password_action_tip
import mifos_mobile.feature.auth.generated.resources.feature_set_new_password_confirm_password_label
import mifos_mobile.feature.auth.generated.resources.feature_set_new_password_message
import mifos_mobile.feature.auth.generated.resources.feature_set_new_password_new_password_label
import mifos_mobile.feature.auth.generated.resources.feature_set_new_password_submit
import mifos_mobile.feature.auth.generated.resources.feature_set_new_password_tip
import mifos_mobile.feature.auth.generated.resources.feature_set_new_password_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosPasswordField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.CombinedPasswordErrorCard
import org.mifos.mobile.core.ui.PasswordStrengthIndicator
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

@Composable
internal fun SetPasswordScreen(
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SetPasswordViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SetPasswordEvent.NavigateToLogin -> navigateToLoginScreen.invoke()

            is SetPasswordEvent.NavigateToStatus -> navigateToStatusScreen(
                event.eventType,
                event.eventDestination,
                event.title,
                event.subtitle,
                event.buttonText,
            )
        }
    }

    SetPasswordDialogs(
        dialogState = uiState.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(SetPasswordAction.OnDismissDialog) }
        },
    )

    SetPasswordScreen(
        modifier = modifier,
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun SetPasswordDialogs(
    dialogState: SetPasswordState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is SetPasswordState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }
}

@Composable
internal fun SetPasswordScreen(
    state: SetPasswordState,
    modifier: Modifier = Modifier,
    onAction: (SetPasswordAction) -> Unit,
) {
    MifosScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = DesignToken.padding.large)
                        .padding(DesignToken.padding.large)
                        .statusBarsPadding(),
                ) {
                    Text(
                        text = stringResource(Res.string.feature_set_new_password_title),
                        style = MifosTypography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.feature_set_new_password_message),
                        style = MifosTypography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SetPasswordInputBox(
                        state = state,
                        onAction = onAction,
                    )
                }

                if (state.showOverlay) {
                    MifosProgressIndicatorOverlay()
                }
            }

            else -> { }
        }
    }
}

@Composable
internal fun SetPasswordInputBox(
    state: SetPasswordState,
    modifier: Modifier = Modifier,
    onAction: (SetPasswordAction) -> Unit,
) {
    val hasError = state.passwordError != null || state.passwordFeedback.isNotEmpty()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            MifosPasswordField(
                label = stringResource(Res.string.feature_set_new_password_new_password_label),
                value = state.password,
                onValueChange = { onAction(SetPasswordAction.OnPasswordChange(it)) },
                shape = DesignToken.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                showPassword = state.isPasswordVisible,
                showPasswordChange = {
                    onAction(SetPasswordAction.OnTogglePassword)
                },
                isError = state.passwordError != null,
                hint = state.passwordError?.let { stringResource(it) },
            )

            if (state.password.isNotEmpty() && !hasError) {
                PasswordStrengthIndicator(
                    state = state.passwordStrengthState,
                    currentCharacterCount = state.password.length,
                    minimumCharacterCount = 8,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Show combined error card with integrated strength indicator when there are errors

            if (hasError && state.password.isNotEmpty()) {
                CombinedPasswordErrorCard(
                    passwordStrengthState = state.passwordStrengthState,
                    currentCharacterCount = state.password.length,
                    errorText = state.passwordError,
                    errors = state.passwordFeedback,
                    minimumCharacterCount = 8,
                )
            }
        }

        MifosPasswordField(
            label = stringResource(Res.string.feature_set_new_password_confirm_password_label),
            value = state.confirmPassword,
            onValueChange = { onAction(SetPasswordAction.OnConfirmPasswordChange(it)) },
            shape = DesignToken.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            showPassword = state.isConfirmPasswordVisible,
            showPasswordChange = {
                onAction(SetPasswordAction.OnToggleConfirmPassword)
            },
            isError = state.confirmPasswordError != null,
            hint = state.confirmPasswordError?.let { stringResource(it) },
            keyboardType = KeyboardType.Password,
        )

        MifosButton(
            modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.buttonHeight),
            enabled = state.isSubmitButtonEnabled,
            onClick = {
                onAction(SetPasswordAction.OnSubmit)
            },
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_set_new_password_submit),
                style = MifosTypography.titleMedium,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.feature_set_new_password_tip),
                style = MifosTypography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.width(DesignToken.spacing.small))

            Text(
                modifier = Modifier.clickable { onAction.invoke(SetPasswordAction.OnLogIn) },
                text = stringResource(Res.string.feature_set_new_password_action_tip),
                style = MifosTypography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
private fun Set_Password_Preview() {
    MifosMobileTheme {
        SetPasswordScreen(
            state = SetPasswordState(dialogState = null),
            onAction = {},
        )
    }
}
