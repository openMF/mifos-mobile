/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.core.ui.generated.resources.ic_icon_logo_1
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_Sign_in
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_dont_have_an_account
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_forgot_password
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_password_label
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_sign_up
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_sub_title
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_title
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_username_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosPasswordField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

@Composable
internal fun LoginScreen(
    navigateToRegisterScreen: () -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    navigateToPasscodeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is LoginEvent.NavigateToSignup -> navigateToRegisterScreen.invoke()

            is LoginEvent.NavigateToPasscode -> navigateToPasscodeScreen.invoke()

            is LoginEvent.NavigateToForgotPassword -> navigateToForgotPasswordScreen.invoke()

            is LoginEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LoginDialogs(
        dialogState = state.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(LoginAction.ErrorDialogDismiss) }
        },
    )

    LoginScreen(
        state = state,
        modifier = modifier.fillMaxSize(),
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    modifier: Modifier = Modifier,
    onAction: (LoginAction) -> Unit,
) {
    MifosScaffold(
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
                LoginScreenContent(
                    modifier = modifier,
                    state = state,
                    onAction = onAction,
                )

                if (state.showOverlay) {
                    MifosProgressIndicatorOverlay()
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun LoginDialogs(
    dialogState: LoginState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is LoginState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }
}

@Composable
private fun LoginScreenContent(
    state: LoginState,
    modifier: Modifier = Modifier,
    onAction: (LoginAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp)
            .padding(DesignToken.padding.large)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                    },
                )
            }
            .verticalScroll(rememberScrollState()),
    ) {
        LogoBox()
        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))
        InputBox(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
fun LogoBox(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Image(
            modifier = Modifier.height(48.dp).width(165.dp),
            painter = painterResource(
                mifos_mobile.core.ui.generated.resources.Res.drawable.ic_icon_logo_1,
            ),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(Res.string.feature_sign_in_title),
            style = MifosTypography.headlineMedium,
            color = AppColors.customBlack,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        Text(
            text = stringResource(Res.string.feature_sign_in_sub_title),
            style = MifosTypography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun InputBox(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
    ) {
        MifosOutlinedTextField(
            value = state.username,
            onValueChange = {
                onAction(LoginAction.UsernameChanged(it))
            },
            label = stringResource(Res.string.feature_sign_in_username_label),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            config = MifosTextFieldConfig(
                isError = state.isError,
                errorText = state.userNameError.takeIf { state.isError }?.let { stringResource(it) },
                trailingIcon = if (state.isError) {
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

        MifosPasswordField(
            label = stringResource(Res.string.feature_sign_in_password_label),
            value = state.password,
            onValueChange = {
                onAction(LoginAction.PasswordChanged(it))
            },
            shape = DesignToken.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            showPassword = state.isPasswordVisible,
            showPasswordChange = {
                onAction(LoginAction.TogglePasswordVisibility)
            },
            isError = state.isError,
            hint = state.passwordError.takeIf { state.isError }?.let { stringResource(it) },
        )

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .clickable(true) {
                    onAction(LoginAction.NavigateToForgotPassword)
                },
            text = stringResource(Res.string.feature_sign_in_forgot_password),
            style = MifosTypography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        MifosButton(
            modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
            enabled = state.isLoginButtonEnabled,
            onClick = {
                onAction(LoginAction.LoginClicked)
            },
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_sign_in_Sign_in),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.feature_sign_in_dont_have_an_account),
                style = MifosTypography.labelMedium,
            )

            Spacer(
                modifier = Modifier.width(DesignToken.spacing.extraSmall),
            )

            Text(
                modifier = Modifier.clickable(true) {
                    onAction(LoginAction.SignupClicked)
                },
                text = stringResource(Res.string.feature_sign_in_sign_up),
                style = MifosTypography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
private fun LoanScreenPreview() {
    MifosMobileTheme {
        LoginScreen(
            state = LoginState(uiState = ScreenUiState.Success),
            onAction = {},
        )
    }
}
