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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_signup_already_have_an_account
import mifos_mobile.feature.auth.generated.resources.feature_signup_confirm_password_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_customer_account_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_email_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_first_name_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_last_name_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_log_in
import mifos_mobile.feature.auth.generated.resources.feature_signup_middle_name_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_password_label
import mifos_mobile.feature.auth.generated.resources.feature_signup_sub_title
import mifos_mobile.feature.auth.generated.resources.feature_signup_submit
import mifos_mobile.feature.auth.generated.resources.feature_signup_title
import org.jetbrains.compose.resources.StringResource
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
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.CombinedPasswordErrorCard
import org.mifos.mobile.core.ui.PasswordStrengthIndicator
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun RegistrationScreen(
    navigateToUploadIdScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
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

            is SignUpEvent.NavigateToUploadDocuments -> navigateToUploadIdScreen.invoke()

            is SignUpEvent.NavigateToLogin -> navigateToLoginScreen.invoke()
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

        is SignUpState.SignUpDialog.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
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
    MifosScaffold(
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) { paddingValues ->
        RegistrationScreenContent(
            state = state,
            onAction = onAction,
            modifier = modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun RegistrationScreenContent(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.canScrollForward) {
        if (scrollState.canScrollForward) scrollState.scrollTo(scrollState.maxValue)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    keyboardController?.hide()
                }
            }
            .padding(DesignToken.padding.large),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        contentPadding = PaddingValues(
            bottom = DesignToken.spacing.extraLarge,
        ),
    ) {
        item {
            Text(
                text = stringResource(Res.string.feature_signup_title),
                style = MifosTypography.headlineMedium,
                color = AppColors.customBlack,
            )
        }

        item {
            Text(
                text = stringResource(Res.string.feature_signup_sub_title),
                style = MifosTypography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        item {
            FormSection(
                inputConfigs = getInputConfigs(state, onAction),
            )
        }

        item {
            Spacer(modifier = Modifier.height(DesignToken.spacing.small))
            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.inputHeight),
                onClick = { onAction(SignUpAction.SubmitClick) },
                shape = DesignToken.shapes.medium,
                enabled = state.isSubmitButtonEnabled,
            ) {
                Text(
                    text = stringResource(Res.string.feature_signup_submit),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(DesignToken.spacing.small))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.feature_signup_already_have_an_account),
                    style = MifosTypography.labelMedium,
                )
                Spacer(modifier = Modifier.width(DesignToken.spacing.extraSmall))
                Text(
                    modifier = Modifier.clickable {
                        onAction(SignUpAction.OnNavigateToLogin)
                    },
                    text = stringResource(Res.string.feature_signup_log_in),
                    style = MifosTypography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun FormSection(
    inputConfigs: List<InputFieldConfig>,
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = DesignToken.spacing.largeIncreased,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
    ) {
        inputConfigs.forEach { config ->
            MifosInputField(config)
        }
    }
}

@Composable
fun MifosInputField(
    config: InputFieldConfig,
    modifier: Modifier = Modifier,
) {
    val visualTransformation =
        if (config.fieldType == InputFieldType.PASSWORD && !config.isPasswordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }

    val trailingIcon: @Composable (() -> Unit)? = when {
        config.fieldType == InputFieldType.PASSWORD && config.onTogglePasswordVisibility != null -> {
            {
                IconButton(onClick = config.onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (config.isPasswordVisible) MifosIcons.EyeOff else MifosIcons.Eye,
                        contentDescription = "Toggle password visibility",
                        tint = if (config.errorText != null) {
                            MaterialTheme.colorScheme.error
                        } else {
                            Color.Unspecified
                        },
                    )
                }
            }
        }
        config.errorText != null -> {
            {
                Icon(
                    imageVector = MifosIcons.ErrorCircle,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
        else -> null
    }

    val hasError = config.errorText != null || config.state.passwordFeedback.isNotEmpty()

    if (config.fieldType == InputFieldType.PASSWORD) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MifosPasswordField(
                label = stringResource(config.labelRes),
                value = config.value,
                onValueChange = config.onValueChange,
                shape = DesignToken.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                showPassword = config.isPasswordVisible,
                showPasswordChange = {
                    config.onTogglePasswordVisibility?.invoke()
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
                isError = config.errorText != null,
                hint = config.errorText?.let { stringResource(it) },
            )
            if (!config.isConfirmPassword) {
                if (config.value.isNotEmpty() && !hasError) {
                    PasswordStrengthIndicator(
                        state = config.state.passwordStrengthState,
                        currentCharacterCount = config.value.length,
                        minimumCharacterCount = 8,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // Show combined error card with integrated strength indicator when there are errors

                if (hasError && config.value.isNotEmpty()) {
                    CombinedPasswordErrorCard(
                        passwordStrengthState = config.state.passwordStrengthState,
                        currentCharacterCount = config.value.length,
                        errorText = config.errorText,
                        errors = config.state.passwordFeedback,
                        minimumCharacterCount = 8,
                    )
                }
            }
        }
    } else {
        MifosOutlinedTextField(
            value = config.value,
            onValueChange = config.onValueChange,
            label = stringResource(config.labelRes),
            shape = DesignToken.shapes.medium,
            textStyle = MifosTypography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                errorBorderColor = MaterialTheme.colorScheme.error,
            ),
            config = MifosTextFieldConfig(
                isError = config.errorText != null,
                errorText = config.errorText?.let { stringResource(it) },
                trailingIcon = trailingIcon,
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (config.fieldType == InputFieldType.PASSWORD) {
                        KeyboardType.Password
                    } else {
                        KeyboardType.Text
                    },
                    imeAction = ImeAction.Next,
                ),
            ),
        )
    }
}

enum class InputFieldType {
    TEXT,
    PASSWORD,
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
