/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.recoverPassword

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_email_label
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_log_in
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_message
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_phone_number_label
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_remember_your_password
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

@Composable
internal fun RecoverPasswordScreen(
    navigateToOtpAuthenticationScreen: (String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecoverPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is RecoverPasswordEvent.NavigateToLogin -> navigateToLoginScreen.invoke()

            is RecoverPasswordEvent.NavigateToOtpAuth -> {
                navigateToOtpAuthenticationScreen(
                    event.nextRoute,
                )
            }
        }
    }

    RecoverPasswordScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    RecoverPasswordDialogs(
        dialogState = state.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(RecoverPasswordAction.OnDismissDialog) }
        },
    )
}

@Composable
internal fun RecoverPasswordScreen(
    state: RecoverPasswordState,
    modifier: Modifier = Modifier,
    onAction: (RecoverPasswordAction) -> Unit,
) {
    MifosScaffold(
        modifier = modifier,
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
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
                        .padding(DesignToken.padding.large)
                        .padding(top = DesignToken.padding.large)
                        .statusBarsPadding(),
                ) {
                    Text(
                        text = stringResource(Res.string.feature_recover_now_title),
                        style = MifosTypography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.feature_recover_now_message),
                        style = MifosTypography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    ForgotPasswordInputBox(
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
internal fun ForgotPasswordInputBox(
    state: RecoverPasswordState,
    onAction: (RecoverPasswordAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
    ) {
        MifosOutlinedTextField(
            value = state.phoneNumber,
            onValueChange = {
                onAction(RecoverPasswordAction.OnPhoneNumberChange(it))
            },
            label = stringResource(Res.string.feature_recover_now_phone_number_label),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                ),
                isError = state.phoneNumberError != null,
                errorText = state.phoneNumberError?.let { stringResource(it) },
                trailingIcon = if (state.phoneNumberError != null) {
                    {
                        Icon(
                            imageVector = MifosIcons.ErrorCircle,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                } else {
                    null
                },
            ),
        )

        MifosOutlinedTextField(
            value = state.email,
            onValueChange = {
                onAction(RecoverPasswordAction.OnEmailChange(it))
            },
            label = stringResource(Res.string.feature_recover_now_email_label),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                isError = state.emailError != null,
                errorText = state.emailError?.let { stringResource(it) },
                trailingIcon = if (state.emailError != null) {
                    {
                        Icon(
                            imageVector = MifosIcons.ErrorCircle,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                } else {
                    null
                },
            ),
        )

        MifosButton(
            modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.buttonHeight),
            enabled = state.isRecoverButtonEnabled,
            onClick = {
                onAction(RecoverPasswordAction.OnRecoverClicked)
            },
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_recover_now_title),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.feature_recover_now_remember_your_password),
                style = MifosTypography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                modifier = Modifier.clickable { onAction.invoke(RecoverPasswordAction.OnLogin) },
                text = stringResource(Res.string.feature_recover_now_log_in),
                style = MifosTypography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun RecoverPasswordDialogs(
    dialogState: RecoverPasswordState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is RecoverPasswordState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = dialogState.message,
                ),
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

@Preview
@Composable
private fun PreviewRecoverPassword() {
    RecoverPasswordScreen(
        onAction = {},
        state = RecoverPasswordState(),
    )
}
