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

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.create_an_account
import mifos_mobile.feature.auth.generated.resources.feature_auth_ic_person
import mifos_mobile.feature.auth.generated.resources.feature_auth_mifos_logo
import mifos_mobile.feature.auth.generated.resources.login
import mifos_mobile.feature.auth.generated.resources.password
import mifos_mobile.feature.auth.generated.resources.username
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosPasswordField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun LoginScreen(
    navigateToRegisterScreen: () -> Unit,
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
            is LoginEvent.NavigateToPasscodeScreen -> navigateToPasscodeScreen.invoke()
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
        snackbarHostState = snackbarHostState,
        modifier = modifier.fillMaxSize(),
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onAction: (LoginAction) -> Unit,
) {
    MifosScaffold(
        snackbarHostState = snackbarHostState,
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        LoginScreenContent(
            modifier = modifier
                .padding(paddingValues),
            state = state,
            onAction = onAction,
        )
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

        is LoginState.DialogState.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                    },
                )
            },
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MifosMobileIcon(mobileIcon = Res.drawable.feature_auth_mifos_logo)

        MifosOutlinedTextField(
            value = state.username,
            onValueChange = {
                onAction(LoginAction.UsernameChanged(it))
            },
            label = stringResource(Res.string.username),
            config = MifosTextFieldConfig(
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.feature_auth_ic_person),
                        contentDescription = null,
                    )
                },
            ),
        )

        MifosPasswordField(
            label = stringResource(Res.string.password),
            value = state.password,
            onValueChange = {
                onAction(LoginAction.PasswordChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            showPassword = state.isPasswordVisible,
            showPasswordChange = {
                onAction(LoginAction.TogglePasswordVisibility)
            },
        )

        MifosButton(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = state.isLoginButtonEnabled,
            onClick = {
                onAction(LoginAction.LoginClicked)
            },
        ) {
            Text(
                text = stringResource(Res.string.login).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                thickness = 1.dp,
                color = Color.Gray,
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "or",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                thickness = 1.dp,
                color = Color.Gray,
            )
        }

        MifosButton(
            onClick = { onAction(LoginAction.SignupClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        ) {
            Text(text = stringResource(Res.string.create_an_account))
        }
    }
}

@Preview
@Composable
private fun LoanScreenPreview() {
    MifosMobileTheme {
        LoginScreen(
            state = LoginState(dialogState = null),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
        )
    }
}
