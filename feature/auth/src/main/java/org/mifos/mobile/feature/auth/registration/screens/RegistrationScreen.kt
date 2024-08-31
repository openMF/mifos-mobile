/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.registration.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.core.util.PatternsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mifos.library.countrycodepicker.CountryCodePicker
import kotlinx.coroutines.launch
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.auth.R
import org.mifos.mobile.feature.auth.registration.utils.PasswordStrength
import org.mifos.mobile.feature.auth.registration.viewmodel.RegistrationUiState
import org.mifos.mobile.feature.auth.registration.viewmodel.RegistrationViewModel

@Composable
internal fun RegistrationScreen(
    onVerified: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.registrationUiState.collectAsStateWithLifecycle()

    RegistrationScreen(
        uiState = uiState,
        onVerified = onVerified,
        navigateBack = navigateBack,
        register = viewModel::registerUser,
        modifier = modifier,
    )
}

@Composable
private fun RegistrationScreen(
    uiState: RegistrationUiState,
    onVerified: () -> Unit,
    navigateBack: () -> Unit,
    register: (
        accountNumber: String,
        username: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        password: String,
        authMode: String,
        countryCode: String,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    MifosScaffold(
        topBarTitleResId = R.string.register,
        navigateBack = navigateBack,
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            ) {
                RegistrationContent(
                    register = register,
                    progress = {
                        updatePasswordStrengthView(it, context)
                    },
                    snackBarHostState = snackBarHostState,
                )

                when (uiState) {
                    RegistrationUiState.Initial -> Unit

                    is RegistrationUiState.Error -> {
                        Toast.makeText(context, uiState.exception, Toast.LENGTH_SHORT).show()
                    }

                    RegistrationUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    RegistrationUiState.Success -> {
                        onVerified()
                    }
                }
            }
        },
    )
}

@Composable
@Suppress("LongMethod")
private fun RegistrationContent(
    register: (
        accountNumber: String,
        username: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        password: String,
        authMode: String,
        countryCode: String,
    ) -> Unit,
    progress: (String) -> Float,
    snackBarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var accountNumber by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var username by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var firstName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var lastName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var phoneNumber by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var onValueChangePassword by rememberSaveable {
        mutableStateOf(false)
    }
    var confirmPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var countryCode by rememberSaveable {
        mutableStateOf("")
    }
    val radioOptions =
        listOf(stringResource(id = R.string.rb_email), stringResource(id = R.string.rb_mobile))

    var authenticationMode by remember { mutableStateOf(radioOptions[0]) }

    val progressIndicator = progress(password.text)
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    var confirmPasswordVisibility: Boolean by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(scrollState.canScrollForward) {
        if (scrollState.canScrollForward) scrollState.scrollTo(scrollState.maxValue)
    }

    fun validateAllFields(): String {
        return areFieldsValidated(
            context = context,
            accountNumberContent = accountNumber.text,
            usernameContent = username.text,
            firstNameContent = firstName.text,
            lastNameContent = lastName.text,
            phoneNumberContent = phoneNumber.text,
            emailContent = email.text,
            passwordContent = password.text,
            confirmPasswordContent = confirmPassword.text,
        )
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
        MifosMobileIcon(id = R.drawable.feature_auth_mifos_logo)

        MifosOutlinedTextField(
            value = accountNumber,
            onValueChange = { accountNumber = it },
            label = R.string.account_number,
            supportingText = "",
        )
        MifosOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = R.string.username,
            supportingText = "",
        )
        MifosOutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = R.string.first_name,
            supportingText = "",
        )
        MifosOutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = R.string.last_name,
            supportingText = "",
        )

        CountryCodePicker(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(2.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
            initialPhoneNumber = phoneNumber.text,
            onValueChange = { (code, phone), _ ->
                phoneNumber = TextFieldValue(phone)
                countryCode = code
            },
            label = { Text(stringResource(id = R.string.phone_number)) },
            keyboardActions = KeyboardActions { keyboardController?.hide() },
        )

        MifosOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = R.string.email,
            supportingText = "",
        )

        MifosOutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                onValueChangePassword = true
            },
            label = R.string.password,
            supportingText = "",
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (passwordVisibility) {
                    MifosIcons.Visibility
                } else {
                    MifosIcons.VisibilityOff
                }
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, null)
                }
            },
            keyboardType = KeyboardType.Password,
        )

        if (onValueChangePassword) {
            LinearProgressIndicator(
                progress = { progressIndicator },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                color = when (progressIndicator) {
                    0.25f -> Color.Red
                    0.5f -> Color(alpha = 255, red = 220, green = 185, blue = 0)
                    0.75f -> Color.Green
                    else -> Color.Blue
                },
                trackColor = Color.White,
            )
        }

        MifosOutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = R.string.confirm_password,
            supportingText = "",
            visualTransformation = if (confirmPasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (confirmPasswordVisibility) {
                    MifosIcons.Visibility
                } else {
                    MifosIcons.VisibilityOff
                }
                IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                    Icon(imageVector = image, null)
                }
            },
            keyboardType = KeyboardType.Password,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.verification_mode),
                modifier = Modifier.padding(end = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
            )
            radioOptions.forEach { authMode ->
                RadioButton(
                    selected = (authMode == authenticationMode),
                    onClick = { authenticationMode = authMode },
                )
                Text(
                    text = authMode,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        MifosButton(
            textResId = R.string.register,
            onClick = {
                val error = validateAllFields()

                if (error == "") {
                    register.invoke(
                        accountNumber.text,
                        username.text,
                        firstName.text,
                        lastName.text,
                        phoneNumber.text,
                        email.text,
                        password.text,
                        authenticationMode,
                        countryCode,
                    )
                } else {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = error,
                            actionLabel = "Ok",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }

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

private fun isInputFieldBlank(fieldText: String) = fieldText.trim().isEmpty()

private fun isPhoneNumberValid(fieldText: String?): Boolean {
    if (fieldText.isNullOrBlank()) {
        return false
    }

    val phoneNumberPattern = "^\\+?[0-9]{10,15}\$"
    val regex = phoneNumberPattern.toRegex()
    return regex.matches(fieldText.trim())
}

private fun isInputLengthInadequate(fieldText: String) = fieldText.trim().length < 6

private fun inputHasSpaces(fieldText: String) = fieldText.trim().contains(" ")

private fun hasLeadingTrailingSpaces(fieldText: String) = fieldText.trim().length < fieldText.length

private fun isEmailInvalid(emailText: String) =
    !PatternsCompat.EMAIL_ADDRESS.matcher(emailText.trim()).matches()

@Suppress("CyclomaticComplexMethod")
private fun areFieldsValidated(
    context: Context,
    accountNumberContent: String,
    usernameContent: String,
    firstNameContent: String,
    lastNameContent: String,
    phoneNumberContent: String,
    emailContent: String,
    passwordContent: String,
    confirmPasswordContent: String,
): String {
    with(context) {
        return when {
            isInputFieldBlank(accountNumberContent) -> {
                getString(
                    R.string.error_validation_blank,
                    context.getString(R.string.account_number),
                )
            }

            isInputFieldBlank(usernameContent) -> {
                getString(R.string.error_validation_blank, context.getString(R.string.username))
            }

            isInputLengthInadequate(usernameContent) -> {
                getString(R.string.error_username_greater_than_six)
            }

            inputHasSpaces(usernameContent) -> {
                getString(
                    R.string.error_validation_cannot_contain_spaces,
                    context.getString(R.string.username),
                    context.getString(R.string.not_contain_username),
                )
            }

            isInputFieldBlank(firstNameContent) -> {
                getString(R.string.error_validation_blank, context.getString(R.string.first_name))
            }

            isInputFieldBlank(lastNameContent) -> {
                getString(R.string.error_validation_blank, context.getString(R.string.last_name))
            }

            !isPhoneNumberValid(phoneNumberContent) -> {
                getString(R.string.invalid_phn_number)
            }

            isInputFieldBlank(emailContent) -> {
                getString(R.string.error_validation_blank, context.getString(R.string.email))
            }

            isInputFieldBlank(passwordContent) -> {
                getString(R.string.error_validation_blank, context.getString(R.string.password))
            }

            hasLeadingTrailingSpaces(passwordContent) -> {
                getString(
                    R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                    context.getString(R.string.password),
                )
            }

            isEmailInvalid(emailContent) -> {
                getString(context, R.string.error_invalid_email)
            }

            isInputLengthInadequate(passwordContent) -> {
                getString(
                    R.string.error_validation_minimum_chars,
                    context.getString(R.string.password),
                    context.resources.getInteger(R.integer.password_minimum_length),
                )
            }

            passwordContent != confirmPasswordContent -> {
                getString(R.string.error_password_not_match)
            }

            else -> ""
        }
    }
}

private fun updatePasswordStrengthView(password: String, context: Context): Float {
    val str = PasswordStrength.calculateStrength(password)
    return when (str.getText(context)) {
        getString(context, R.string.password_strength_weak) -> 0.25f
        getString(context, R.string.password_strength_medium) -> 0.5f
        getString(context, R.string.password_strength_strong) -> 0.75f
        else -> 1f
    }
}

internal class RegistrationScreenPreviewProvider : PreviewParameterProvider<RegistrationUiState> {

    override val values: Sequence<RegistrationUiState>
        get() = sequenceOf(
            RegistrationUiState.Loading,
            RegistrationUiState.Error(1),
            RegistrationUiState.Success,
            RegistrationUiState.Initial,
        )
}

@DevicePreviews
@Composable
private fun RegistrationScreenPreview(
    @PreviewParameter(RegistrationScreenPreviewProvider::class)
    registrationUiState: RegistrationUiState,
) {
    MifosMobileTheme {
        RegistrationScreen(
            uiState = registrationUiState,
            onVerified = {},
            navigateBack = {},
            register = { _, _, _, _, _, _, _, _, _ -> },
        )
    }
}
