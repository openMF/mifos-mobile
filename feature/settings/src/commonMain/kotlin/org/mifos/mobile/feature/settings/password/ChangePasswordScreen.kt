/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_confirm_new_password
import mifos_mobile.feature.settings.generated.resources.feature_settings_new_password
import mifos_mobile.feature.settings.generated.resources.feature_settings_next
import mifos_mobile.feature.settings.generated.resources.feature_settings_old_password
import mifos_mobile.feature.settings.generated.resources.feature_settings_password
import mifos_mobile.feature.settings.generated.resources.password_update_success_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosPasswordField
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.ui.CombinedPasswordErrorCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosSuccessDialog
import org.mifos.mobile.core.ui.component.SuccessDialogState
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun ChangePasswordScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: ChangePasswordViewModel = koinViewModel(),
) {
    val state = viewmodel.stateFlow.collectAsStateWithLifecycle().value

    EventsEffect(eventFlow = viewmodel.eventFlow) { event ->
        when (event) {
            PasswordEvent.OnNavigateBack -> navigateBack.invoke()
        }
    }

    ChangePasswordScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewmodel) {
            { viewmodel.trySendAction(it) }
        },
    )

    PasswordDialog(
        dialogState = state.dialogState,
        onDismiss = remember(viewmodel) {
            { viewmodel.trySendAction(PasswordAction.DismissDialog) }
        },
    )
}

@Composable
internal fun ChangePasswordScreen(
    state: PasswordState,
    modifier: Modifier = Modifier,
    onAction: (action: PasswordAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        onNavigateBack = remember(state) {
            { onAction(PasswordAction.NavigateBack) }
        },
        topBarTitle = stringResource(Res.string.feature_settings_password),
    ) {
        if (state.dialogState != PasswordState.DialogState.Loading) {
            PasswordScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = DesignToken.padding.large),
                state = state,
                onAction = onAction,
            )
        }
    }
}

@Composable
internal fun PasswordScreenContent(
    state: PasswordState,
    modifier: Modifier = Modifier,
    onAction: (action: PasswordAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = DesignToken.padding.largeIncreased),
    ) {
        MifosPasswordField(
            modifier = Modifier,
            value = state.oldPassword,
            hint = if (state.oldPasswordError != null) {
                stringResource(state.oldPasswordError)
            } else {
                null
            },
            showPassword = state.oldPasswordVisible,
            label = stringResource(Res.string.feature_settings_old_password),
            showPasswordChange = { onAction(PasswordAction.OldPasswordVisibleClick) },
            onValueChange = { onAction(PasswordAction.OnOldPasswordChange(it)) },
        )

        Spacer(modifier = Modifier.height(DesignToken.padding.medium))

        MifosPasswordField(
            modifier = Modifier,
            value = state.newPassword,
            hint = if (state.newPasswordError == null) {
                state.newPasswordError
            } else {
                null
            },
            showPassword = state.newPasswordVisible,
            label = stringResource(Res.string.feature_settings_new_password),
            showPasswordChange = { onAction(PasswordAction.NewPasswordVisibleClick) },
            onValueChange = {
                onAction(PasswordAction.OnNewPasswordChange(it))
            },
        )

        CombinedPasswordErrorCard(
            passwordStrengthState = state.passwordStrengthState,
            currentCharacterCount = state.newPassword.length,
            errorText = state.newPasswordError,
            errors = state.passwordFeedback,
            minimumCharacterCount = 12,
        )

        Spacer(modifier = Modifier.height(DesignToken.padding.medium))

        MifosPasswordField(
            modifier = Modifier,
            value = state.confirmPassword,
            hint = if (state.confirmPasswordError != null) {
                stringResource(state.confirmPasswordError)
            } else {
                null
            },
            showPassword = state.confirmPasswordVisible,
            label = stringResource(Res.string.feature_settings_confirm_new_password),
            showPasswordChange = { onAction(PasswordAction.ConfirmPasswordVisibleClick) },
            onValueChange = {
                onAction(PasswordAction.OnConfirmPasswordChange(it))
            },
        )

        Spacer(modifier = Modifier.height(DesignToken.padding.largeIncreased))

        MifosButton(
            enabled = state.isEnabled,
            text = {
                Text(stringResource(Res.string.feature_settings_next))
            },
            onClick = {
                onAction.invoke(PasswordAction.SubmitClick)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PasswordDialog(
    dialogState: PasswordState.DialogState?,
    onDismiss: () -> Unit,
) {
    when (dialogState) {
        is PasswordState.DialogState.Success -> MifosSuccessDialog(
            visibilityState = SuccessDialogState.Shown(
                message = Res.string.password_update_success_message,
                title = dialogState.message,
                buttonText = null,
                onBtnClick = {},
            ),
        )

        is PasswordState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = stringResource(dialogState.message),
            ),
            onDismissRequest = onDismiss,
        )

        is PasswordState.DialogState.Loading -> {
            MifosProgressIndicator()
        }

        null -> Unit
    }
}
