/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.registration

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.account_number
import mifos_mobile.feature.auth.generated.resources.confirm_password
import mifos_mobile.feature.auth.generated.resources.email
import mifos_mobile.feature.auth.generated.resources.feature_auth_mifos_logo
import mifos_mobile.feature.auth.generated.resources.first_name
import mifos_mobile.feature.auth.generated.resources.last_name
import mifos_mobile.feature.auth.generated.resources.password
import mifos_mobile.feature.auth.generated.resources.phone_number
import mifos_mobile.feature.auth.generated.resources.rb_email
import mifos_mobile.feature.auth.generated.resources.rb_mobile
import mifos_mobile.feature.auth.generated.resources.register
import mifos_mobile.feature.auth.generated.resources.username
import mifos_mobile.feature.auth.generated.resources.verification_mode
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun RegistrationScreen(
    navigateToVerification: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SignUpEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is SignUpEvent.NavigateToVerification -> navigateToVerification.invoke()
            is SignUpEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    RegistrationScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun RegistrationScreen(
    state: SignUpState,
    snackbarHostState: SnackbarHostState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBarTitle = stringResource(Res.string.register),
        backPress = { onAction(SignUpAction.BackPress) },
        modifier = modifier,
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            ) {
                RegistrationScreenContent(
                    state = state,
                    onAction = onAction,
                )

                when {
                    state.dialogState is SignUpState.SignUpDialog.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }
                }
            }
        },
    )
}

@Composable
@Suppress("LongMethod")
private fun RegistrationScreenContent(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val radioOptions =
        listOf(stringResource(Res.string.rb_email), stringResource(Res.string.rb_mobile))

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.canScrollForward) {
        if (scrollState.canScrollForward) scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                    },
                )
            }
            .verticalScroll(
                state = scrollState,
                enabled = true,
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MifosMobileIcon(mobileIcon = Res.drawable.feature_auth_mifos_logo)

        MifosOutlinedTextField(
            value = state.accountNumber,
            onValueChange = { onAction(SignUpAction.AccountInputChange(it)) },
            label = stringResource(Res.string.account_number),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.accountNumber.isEmpty(),
            ),
        )
        MifosOutlinedTextField(
            value = state.userNameInput,
            onValueChange = { onAction(SignUpAction.UserNameInputChange(it)) },
            label = stringResource(Res.string.username),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.userNameInput.isEmpty(),
            ),
        )
        MifosOutlinedTextField(
            value = state.firstNameInput,
            onValueChange = { onAction(SignUpAction.FirstNameInputChange(it)) },
            label = stringResource(Res.string.first_name),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.firstNameInput.isEmpty(),
            ),
        )
        MifosOutlinedTextField(
            value = state.lastNameInput,
            onValueChange = { onAction(SignUpAction.LastNameInputChange(it)) },
            label = stringResource(Res.string.last_name),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.lastNameInput.isEmpty(),
            ),
        )

        MifosOutlinedTextField(
            value = state.mobileNumberInput,
            label = stringResource(Res.string.phone_number),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.mobileNumberInput.isEmpty(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                ),
            ),
            onValueChange = {
                onAction(SignUpAction.MobileNumberInputChange(it))
            },
        )

        MifosOutlinedTextField(
            value = state.emailInput,
            onValueChange = { onAction(SignUpAction.EmailInputChange(it)) },
            label = stringResource(Res.string.email),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.emailInput.isEmpty(),
            ),
        )

        MifosOutlinedTextField(
            value = state.passwordInput,
            onValueChange = {
                onAction(SignUpAction.PasswordInputChange(it))
                onAction(SignUpAction.IsPasswordChanges(true))
            },
            label = stringResource(Res.string.password),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    val image = if (state.isPasswordVisible) {
                        MifosIcons.Visibility
                    } else {
                        MifosIcons.VisibilityOff
                    }
                    IconButton(onClick = { onAction(SignUpAction.TogglePasswordVisibility) }) {
                        Icon(imageVector = image, null)
                    }
                },
                isError = state.passwordInput.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (state.isPasswordChanged) {
            val progress = state.passwordStrength
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(),
                color = state.getProgressColor,
                trackColor = Color.White,
            )
        }

        MifosOutlinedTextField(
            value = state.confirmPasswordInput,
            onValueChange = { onAction(SignUpAction.ConfirmPasswordInputChange(it)) },
            label = stringResource(Res.string.confirm_password),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                visualTransformation = if (state.isConfirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    val image = if (state.isConfirmPasswordVisible) {
                        MifosIcons.Visibility
                    } else {
                        MifosIcons.VisibilityOff
                    }
                    IconButton(onClick = { onAction(SignUpAction.ConfirmTogglePasswordVisibility) }) {
                        Icon(imageVector = image, null)
                    }
                },
                isError = state.confirmPasswordInput.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.verification_mode),
                color = MaterialTheme.colorScheme.onSurface,
            )
            radioOptions.forEach { authMode ->
                RadioButton(
                    selected = (authMode == state.authenticationMode),
                    onClick = { onAction(SignUpAction.AuthenticationMode(authMode)) },
                )
                Text(
                    text = authMode,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        MifosButton(
            onClick = {
                onAction(SignUpAction.SubmitClick)
                keyboardController?.hide()
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.register))
        }

        Spacer(modifier = Modifier.imePadding())
    }
}

@Preview
@Composable
private fun RegistrationScreenPreview() {
    MifosMobileTheme {
        RegistrationScreen(
            state = SignUpState(dialogState = null),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
            modifier = Modifier,
        )
    }
}
