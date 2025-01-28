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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
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
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.PasswordStrengthState
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun RegistrationScreen(
    onVerified: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel) { event ->
        when (event) {
            is SignUpEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is SignUpEvent.NavigateToLogin -> onVerified.invoke()
        }
    }

    RegistrationScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        navigateBack = navigateBack,
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
    navigateBack: () -> Unit,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBarTitle = stringResource(Res.string.register),
        backPress = navigateBack,
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
                    state.dialogState is SignUpDialog.Loading -> {
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
            .padding(bottom = 12.dp)
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
    ) {
        MifosMobileIcon(mobileIcon = Res.drawable.feature_auth_mifos_logo)

        MifosOutlinedTextField(
            value = state.accountNumber,
            onValueChange = { onAction(SignUpAction.AccountInputChange(it)) },
            label = stringResource(Res.string.account_number),
            modifier = Modifier.fillMaxWidth(),
            isError = state.accountNumber.isEmpty(),
        )
        MifosOutlinedTextField(
            value = state.userNameInput,
            onValueChange = { onAction(SignUpAction.EmailInputChange(it)) },
            label = stringResource(Res.string.username),
            modifier = Modifier.fillMaxWidth(),
            isError = state.userNameInput.isEmpty(),
        )
        MifosOutlinedTextField(
            value = state.firstNameInput,
            onValueChange = { onAction(SignUpAction.FirstNameInputChange(it)) },
            label = stringResource(Res.string.first_name),
            modifier = Modifier.fillMaxWidth(),
            isError = state.firstNameInput.isEmpty(),
        )
        MifosOutlinedTextField(
            value = state.lastNameInput,
            onValueChange = { onAction(SignUpAction.LastNameInputChange(it)) },
            label = stringResource(Res.string.last_name),
            modifier = Modifier.fillMaxWidth(),
            isError = state.lastNameInput.isEmpty(),
        )

        MifosOutlinedTextField(
            value = state.mobileNumberInput,
            label = stringResource(Res.string.phone_number),
            modifier = Modifier.fillMaxWidth(),
            isError = state.mobileNumberInput.isEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
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
            isError = state.emailInput.isEmpty(),
        )

        MifosOutlinedTextField(
            value = state.passwordInput,
            onValueChange = {
                onAction(SignUpAction.PasswordInputChange(it))
                onAction(SignUpAction.IsPasswordChanges(true))
            },
            label = stringResource(Res.string.password),
            modifier = Modifier.fillMaxWidth(),
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
        )

        if (state.isPasswordChanged) {
            val progress = when (state.passwordStrengthState) {
                PasswordStrengthState.NONE -> 0f
                PasswordStrengthState.WEAK_1 -> 0.25f
                PasswordStrengthState.WEAK_2 -> 0.5f
                PasswordStrengthState.WEAK_3 -> 0.75f
                PasswordStrengthState.STRONG,
                PasswordStrengthState.GOOD,
                PasswordStrengthState.VERY_STRONG,
                -> 1f
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                color = when (progress) {
                    0.25f -> Color.Red
                    0.5f -> Color(alpha = 255, red = 220, green = 185, blue = 0)
                    0.75f -> Color.Green
                    else -> Color.Blue
                },
                trackColor = Color.White,
            )
        }

        MifosOutlinedTextField(
            value = state.confirmPasswordInput,
            onValueChange = { onAction(SignUpAction.ConfirmPasswordInputChange(it)) },
            label = stringResource(Res.string.confirm_password),
            modifier = Modifier.fillMaxWidth(),
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.verification_mode),
                modifier = Modifier.padding(end = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
            )
            radioOptions.forEach { authMode ->
                RadioButton(
                    selected = (authMode == state.authenticationMode),
//                    onClick = { authenticationMode = authMode },
                    onClick = { onAction(SignUpAction.AuthenticationMode(authMode)) },
                )
                Text(
                    text = authMode,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        MifosButton(
            text = { stringResource(Res.string.register) },
            onClick = {
                onAction(SignUpAction.SubmitClick)

                keyboardController?.hide()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp),
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        )

        Spacer(modifier = Modifier.imePadding())
    }
}

@DevicePreviews
@Composable
private fun RegistrationScreenPreview() {
    MifosMobileTheme {
        RegistrationScreen(
            state = SignUpState(dialogState = null),
            snackbarHostState = remember { SnackbarHostState() },
            navigateBack = {},
            onAction = {},
            modifier = Modifier,
        )
    }
}
