/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.passcode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_authorization_passcode
import mifos_mobile.feature.settings.generated.resources.feature_settings_confirm_new_passcode
import mifos_mobile.feature.settings.generated.resources.feature_settings_new_passcode
import mifos_mobile.feature.settings.generated.resources.feature_settings_next
import mifos_mobile.feature.settings.generated.resources.feature_settings_old_passcode
import mifos_mobile.feature.settings.generated.resources.feature_settings_passcode_updated_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosPasswordField
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosSuccessDialog
import org.mifos.mobile.core.ui.component.SuccessDialogState
import org.mifos.mobile.core.ui.utils.EventsEffect

/**
 * A stateful composable that manages the "Update Passcode" screen.
 * It observes state and events from the [UpdatePasscodeViewModel] and handles navigation.
 *
 * @param navigateBack A lambda function to handle back navigation events.
 * @param modifier The [Modifier] to be applied to this screen.
 * @param viewmodel The ViewModel responsible for the screen's logic and state.
 */
@Composable
internal fun UpdatePasscodeScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: UpdatePasscodeViewModel = koinViewModel(),
) {
    val state = viewmodel.stateFlow.collectAsStateWithLifecycle().value

    EventsEffect(viewmodel.eventFlow) { event ->
        when (event) {
            PasscodeEvent.OnNavigateBack -> navigateBack.invoke()
            PasscodeEvent.OnNavigateToPasscodeScreen -> navigateBack.invoke()
        }
    }

    UpdatePasscodeScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewmodel) {
            { viewmodel.trySendAction(it) }
        },
    )

    PasscodeDialog(
        dialogState = state.dialogState,
        onDismiss = remember(state) {
            { viewmodel.trySendAction(PasscodeAction.NavigateToPasscodeScreen) }
        },
    )
}

/**
 * A stateless composable that renders the UI for the "Update Passcode" screen.
 * It includes the scaffold, top bar, and the main content area.
 *
 * @param state The current state of the passcode screen.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param onAction A callback to send actions to the ViewModel.
 */
@Composable
internal fun UpdatePasscodeScreen(
    state: PasscodeState,
    modifier: Modifier = Modifier,
    onAction: (action: PasscodeAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier.fillMaxSize(),
        onNavigateBack = remember(state) {
            { onAction(PasscodeAction.NavigateBackClick) }
        },
        topBarTitle = stringResource(Res.string.feature_settings_authorization_passcode),
    ) {
        PasscodeScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = DesignToken.padding.large)
                .statusBarsPadding(),
            passcodeData = state,
            onAction = onAction,
        )
    }
}

/**
 * Renders the form content for updating the passcode, including input fields and a submit button.
 *
 * @param passcodeData The current state containing the passcode fields' data and errors.
 * @param modifier The [Modifier] to be applied to the content layout.
 * @param onAction A callback to send user actions to the ViewModel.
 */
@Composable
internal fun PasscodeScreenContent(
    passcodeData: PasscodeState,
    modifier: Modifier = Modifier,
    onAction: (action: PasscodeAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .verticalScroll(
                rememberScrollState(),
            ),
    ) {
        MifosPasswordField(
            value = passcodeData.oldPasscode,
            onValueChange = { onAction(PasscodeAction.OnOldPasscodeChange(it)) },
            label = stringResource(Res.string.feature_settings_old_passcode),
            hint = if (passcodeData.oldPasscodeError != null) {
                stringResource(passcodeData.oldPasscodeError)
            } else {
                null
            },
            showPassword = passcodeData.isOldPasscodeVisible,
            keyboardType = KeyboardType.Number,
            showPasswordChange = { onAction(PasscodeAction.OldPasscodeVisibleClick) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        MifosPasswordField(
            value = passcodeData.newPasscode,
            onValueChange = { onAction.invoke(PasscodeAction.OnNewPasscodeChange(it)) },
            label = stringResource(Res.string.feature_settings_new_passcode),
            hint = if (passcodeData.newPasscodeError != null) {
                stringResource(passcodeData.newPasscodeError)
            } else {
                null
            },
            keyboardType = KeyboardType.Number,
            showPassword = passcodeData.isNewPasscodeVisible,
            showPasswordChange = { onAction(PasscodeAction.NewPasscodeVisibleClick) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        MifosPasswordField(
            value = passcodeData.confirmPasscode,
            onValueChange = {
                onAction.invoke(PasscodeAction.OnConfirmPasscodeChange(it))
            },
            hint = if (passcodeData.confirmPasscodeError != null) {
                stringResource(passcodeData.confirmPasscodeError)
            } else {
                null
            },
            label = stringResource(Res.string.feature_settings_confirm_new_passcode),
            showPassword = passcodeData.isConfirmPasscodeVisible,
            keyboardType = KeyboardType.Number,
            showPasswordChange = { onAction(PasscodeAction.ConfirmPasscodeVisibleClick) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        MifosButton(
            text = {
                Text(stringResource(Res.string.feature_settings_next))
            },
            onClick = {
                onAction(PasscodeAction.SubmitClick)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * A composable that displays a dialog based on the [PasscodeState.DialogState].
 * It can show a loading indicator or a success message.
 *
 * @param dialogState The current state of the dialog.
 * @param onDismiss A lambda to be executed when the success dialog is dismissed.
 */
@Composable
private fun PasscodeDialog(
    dialogState: PasscodeState.DialogState?,
    onDismiss: () -> Unit,
) {
    when (dialogState) {
        is PasscodeState.DialogState.Shown -> {
            MifosSuccessDialog(
                visibilityState = SuccessDialogState.Shown(
                    title = dialogState.message,
                    message = Res.string.feature_settings_passcode_updated_message,
                    buttonText = Res.string.feature_settings_next,
                    onBtnClick = onDismiss,
                ),
            )
        }

        is PasscodeState.DialogState.Loading -> {
            MifosProgressIndicator()
        }

        null -> Unit
    }
}
