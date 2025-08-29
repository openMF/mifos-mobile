/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.otpAuthentication

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_common_cancel
import mifos_mobile.feature.auth.generated.resources.feature_common_next
import mifos_mobile.feature.auth.generated.resources.feature_otp_action_tip
import mifos_mobile.feature.auth.generated.resources.feature_otp_authentication_code_label
import mifos_mobile.feature.auth.generated.resources.feature_otp_message
import mifos_mobile.feature.auth.generated.resources.feature_otp_request_id_label
import mifos_mobile.feature.auth.generated.resources.feature_otp_subtitle
import mifos_mobile.feature.auth.generated.resources.feature_otp_tip
import mifos_mobile.feature.auth.generated.resources.feature_otp_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

@Composable
internal fun OtpAuthenticationScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToSetPasswordScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OtpAuthenticationViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is OtpAuthEvent.NavigateToStatus -> {
                navigateToStatusScreen(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }

            is OtpAuthEvent.NavigateNext -> {
                if (uiState.nextRoute == Constants.SET_PASSWORD) {
                    navigateToSetPasswordScreen.invoke()
                }
            }

            is OtpAuthEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    OtpAuthDialogs(
        dialogState = uiState.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(OtpAuthAction.OnDismissDialog) }
        },
    )

    OptAuthScreenContent(
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun OtpAuthDialogs(
    dialogState: OtpAuthState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is OtpAuthState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }
}

@Composable
internal fun OptAuthScreenContent(
    state: OtpAuthState,
    onAction: (OtpAuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier.fillMaxWidth().navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(DesignToken.padding.large)
                        .padding(top = DesignToken.padding.large)
                        .statusBarsPadding(),

                ) {
                    Text(
                        text = stringResource(Res.string.feature_otp_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MifosTypography.headlineMedium,
                    )

                    Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

                    Text(
                        text = stringResource(Res.string.feature_otp_subtitle),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MifosTypography.titleSmallEmphasized,
                    )

                    Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

                    Text(
                        text = stringResource(Res.string.feature_otp_message),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MifosTypography.bodySmall,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OtpInputForm(
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
internal fun OtpInputForm(
    state: OtpAuthState,
    onAction: (OtpAuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
    ) {
        if (state.nextRoute != Constants.SET_PASSWORD) {
            MifosOutlinedTextField(
                value = state.requestId,
                onValueChange = {
                    onAction(OtpAuthAction.OnRequestIdChange(it))
                },
                label = stringResource(Res.string.feature_otp_request_id_label),
                shape = DesignToken.shapes.medium,
                textStyle = MifosTypography.bodyLarge,
                config = MifosTextFieldConfig(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    isError = state.requestIdError != null,
                    errorText = state.requestIdError?.let { stringResource(it) },
                    trailingIcon = if (state.requestIdError != null) {
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
        }

        MifosOutlinedTextField(
            value = state.otp,
            onValueChange = {
                onAction(OtpAuthAction.OnOtpChange(it))
            },
            label = stringResource(Res.string.feature_otp_authentication_code_label),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                isError = state.otpError != null,
                errorText = state.otpError?.let { stringResource(it) },
                trailingIcon = if (state.otpError != null) {
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

        Column(
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            MifosOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                onClick = {
                    onAction(OtpAuthAction.OnCancelClick)
                },
                shape = DesignToken.shapes.medium,
            ) {
                Text(
                    text = stringResource(Res.string.feature_common_cancel),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight),
                onClick = {
                    onAction(OtpAuthAction.OnNextClick)
                },
                enabled = state.isNextButtonEnabled,
                shape = DesignToken.shapes.medium,
            ) {
                Text(
                    text = stringResource(Res.string.feature_common_next),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.feature_otp_tip),
                style = MifosTypography.labelMedium,
            )

            Spacer(
                modifier = Modifier.width(DesignToken.spacing.extraSmall),
            )

            Text(
                modifier = Modifier.clickable(true) {
                    onAction(OtpAuthAction.OnResendClick)
                },
                text = stringResource(Res.string.feature_otp_action_tip),
                style = MifosTypography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
internal fun Otp_Auth_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.padding(DesignToken.padding.large),
        ) {
            OptAuthScreenContent(
                state = OtpAuthState(dialogState = null),
                onAction = { },
            )
        }
    }
}
