/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.core.ui.generated.resources.ic_icon_logo_1
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_signup_already_started
import mifos_mobile.feature.auth.generated.resources.feature_signup_cell_phone_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_confirm_email_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_confirm_password_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_continue
import mifos_mobile.feature.auth.generated.resources.feature_signup_customer_account_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_email_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_enter_email_subtitle
import mifos_mobile.feature.auth.generated.resources.feature_signup_enter_email_title
import mifos_mobile.feature.auth.generated.resources.feature_signup_first_name_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_last_name_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_middle_name_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_password_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_pick_up_left_off
import mifos_mobile.feature.auth.generated.resources.feature_signup_privacy_acceptance
import mifos_mobile.feature.auth.generated.resources.feature_signup_privacy_notice
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun RegistrationScreen(
    navigateToNextScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SignUpEvent.NavigateToNextStep -> {
                navigateToNextScreen.invoke()
            }
            is SignUpEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is SignUpEvent.NavigateToLogin -> navigateToLoginScreen.invoke()
            SignUpEvent.NavigateToUploadDocuments -> TODO()
        }
    }

    SignUpDialog(
        dialogState = state.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(SignUpAction.ErrorDialogDismiss) }
        },
    )

    RegistrationScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun SignUpDialog(
    dialogState: SignUpState.SignUpDialog?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is SignUpState.SignUpDialog.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }
}

@Composable
private fun RegistrationScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .pointerInput(Unit) { detectTapGestures { keyboardController?.hide() } }
                        .verticalScroll(rememberScrollState())
                        .padding(KptTheme.spacing.md)
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
                ) {
                    MifosLogoHeader()
                    Spacer(modifier = Modifier.height(KptTheme.spacing.sm))
                    EmailEntryStep(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                if (state.showOverlay) {
                    MifosProgressIndicatorOverlay()
                }
            }

            else -> {}
        }
    }
}

@Composable
fun MifosLogoHeader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(mifos_mobile.core.ui.generated.resources.Res.drawable.ic_icon_logo_1),
            contentDescription = "Mifos Logo",
            modifier = Modifier
                .height(DesignToken.sizes.imageDp48)
                .width(DesignToken.sizes.imageDp128),
        )
    }
}

@Composable
private fun EmailEntryStep(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
    ) {
        Text(
            text = stringResource(Res.string.feature_signup_enter_email_title),
            style = MifosTypography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(Res.string.feature_signup_enter_email_subtitle),
            style = MifosTypography.bodyMedium,
            color = KptTheme.colorScheme.secondary,
        )

        Spacer(modifier = Modifier.height(KptTheme.spacing.xs))

        MifosOutlinedTextField(
            value = state.email,
            onValueChange = { onAction(SignUpAction.OnEmailChange(it)) },
            label = stringResource(Res.string.feature_signup_email_label),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.emailError != null,
                errorText = state.emailError?.let { stringResource(it) },
            ),
        )

        Spacer(modifier = Modifier.height(KptTheme.spacing.xs))

        Text(
            text = stringResource(Res.string.feature_signup_confirm_email_label),
            style = MifosTypography.labelLarge,
            fontWeight = FontWeight.Bold,
        )

        MifosOutlinedTextField(
            value = state.confirmEmail,
            onValueChange = { onAction(SignUpAction.OnConfirmEmailChange(it)) },
            label = stringResource(Res.string.feature_signup_confirm_email_label),
            modifier = Modifier.fillMaxWidth(),
            config = MifosTextFieldConfig(
                isError = state.confirmEmailError != null,
                errorText = state.confirmEmailError?.let { stringResource(it) },
            ),
        )

        Spacer(modifier = Modifier.height(KptTheme.spacing.xs))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.feature_signup_privacy_acceptance),
                style = MifosTypography.bodySmall,
            )
            Text(
                text = stringResource(Res.string.feature_signup_privacy_notice),
                style = MifosTypography.bodySmall,
                color = KptTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(KptTheme.spacing.md))

        MifosButton(
            onClick = { onAction(SignUpAction.OnEmailStepContinue) },
            enabled = state.isEmailStepContinueEnabled,
            modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
        ) {
            Text(text = stringResource(Res.string.feature_signup_continue))
        }

        Spacer(modifier = Modifier.height(KptTheme.spacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = stringResource(Res.string.feature_signup_already_started),
                style = MifosTypography.bodySmall,
            )
            Text(
                text = stringResource(Res.string.feature_signup_pick_up_left_off),
                style = MifosTypography.bodySmall,
                color = KptTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onAction(SignUpAction.OnNavigateToLogin) },
            )
        }
    }
}

enum class InputFieldType {
    TEXT,
    PASSWORD,
    NUMBER,
    PHONE,
}

data class InputFieldConfig(
    val state: SignUpState,
    val value: String,
    val labelRes: StringResource,
    val isConfirmPassword: Boolean = false,
    val onValueChange: (String) -> Unit,
    val errorText: StringResource? = null,
    val fieldType: InputFieldType = InputFieldType.TEXT,
    val isPasswordVisible: Boolean = false,
    val onTogglePasswordVisibility: (() -> Unit)? = null,
)

@Composable
fun getInputConfigs(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
): List<InputFieldConfig> {
    return listOf(
        InputFieldConfig(
            value = state.firstName,
            state = state,
            errorText = state.firstNameError,
            labelRes = Res.string.feature_signup_first_name_label,
            onValueChange = { onAction(SignUpAction.OnFirstNameChange(it)) },
        ),
        InputFieldConfig(
            value = state.middleName,
            state = state,
            errorText = state.middleNameError,
            labelRes = Res.string.feature_signup_middle_name_label,
            onValueChange = { onAction(SignUpAction.OnMiddleNameChange(it)) },
        ),
        InputFieldConfig(
            value = state.lastName,
            state = state,
            errorText = state.lastNameError,
            labelRes = Res.string.feature_signup_last_name_label,
            onValueChange = { onAction(SignUpAction.OnLastNameChange(it)) },
        ),
        InputFieldConfig(
            value = state.email,
            state = state,
            errorText = state.emailError,
            labelRes = Res.string.feature_signup_email_label,
            onValueChange = { onAction(SignUpAction.OnEmailChange(it)) },
        ),
        InputFieldConfig(
            value = state.mobileNumber,
            state = state,
            errorText = state.mobileNumberError,
            labelRes = Res.string.feature_signup_cell_phone_label,
            fieldType = InputFieldType.PHONE,
            onValueChange = { onAction(SignUpAction.OnMobileNumberChange(it)) },
        ),
        InputFieldConfig(
            value = state.customerAccount,
            state = state,
            errorText = state.customerAccountError,
            labelRes = Res.string.feature_signup_customer_account_label,
            onValueChange = { onAction(SignUpAction.OnCustomerAccountChange(it)) },
        ),
        InputFieldConfig(
            value = state.password,
            state = state,
            errorText = state.passwordError,
            labelRes = Res.string.feature_signup_password_label,
            onValueChange = { onAction(SignUpAction.OnPasswordChange(it)) },
            fieldType = InputFieldType.PASSWORD,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = { onAction(SignUpAction.TogglePasswordVisibility) },
        ),
        InputFieldConfig(
            value = state.confirmPassword,
            state = state,
            isConfirmPassword = true,
            errorText = state.confirmPasswordError,
            labelRes = Res.string.feature_signup_confirm_password_label,
            onValueChange = { onAction(SignUpAction.OnConfirmPasswordChange(it)) },
            fieldType = InputFieldType.PASSWORD,
            isPasswordVisible = state.isConfirmPasswordVisible,
            onTogglePasswordVisibility = { onAction(SignUpAction.ConfirmTogglePasswordVisibility) },
        ),
    )
}

@Preview
@Composable
private fun RegistrationScreenPreview() {
    MifosMobileTheme {
        RegistrationScreen(
            state = SignUpState(dialogState = null),
            onAction = {},
            modifier = Modifier,
        )
    }
}
