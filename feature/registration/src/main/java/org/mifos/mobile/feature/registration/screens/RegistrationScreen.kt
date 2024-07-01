package org.mifos.mobile.feature.registration.screens

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.util.PatternsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.owlbuddy.www.countrycodechooser.CountryCodeChooser
import com.owlbuddy.www.countrycodechooser.utils.enums.CountryCodeType
import kotlinx.coroutines.launch
import org.mifos.mobile.feature.registration.utils.RegistrationState
import org.mifos.mobile.feature.registration.R
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.registration.utils.PasswordStrength
import org.mifos.mobile.feature.registration.viewmodel.RegistrationViewModel


/**
 * @author pratyush
 * @since 28/12/2023
 */
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onVerified: () -> Unit,
    navigateBack: () -> Unit
) {

    val context = LocalContext.current
    val uiState by viewModel.registrationUiState.collectAsStateWithLifecycle()

    RegistrationScreen(
        uiState = uiState,
        onVerified = onVerified,
        navigateBack = navigateBack,
        register = { account, username, firstname, lastname, phoneNumber, email, password, authenticationMode, countryCode ->
            viewModel.registerUser(
                accountNumber = account,
                authenticationMode = authenticationMode,
                email = email,
                firstName = firstname,
                lastName = lastname,
                mobileNumber = "$countryCode$phoneNumber",
                password = password,
                username = username
            )
        },
        progress = { updatePasswordStrengthView(it, context) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreen(
    uiState: RegistrationState,
    onVerified: () -> Unit,
    navigateBack: () -> Unit,
    register: (accountNumber: String, username: String, firstName: String, lastName: String, phoneNumber: String, email: String, password: String, authMode: String, countryCode: String) -> Unit,
    progress: (String) -> Float
) {
    val context = LocalContext.current
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    MFScaffold(
        topBarTitleResId = R.string.register,
        navigateBack = navigateBack,
        scaffoldContent = { contentPadding ->

            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {

                RegistrationContent(
                    register = register,
                    progress = progress,
                    snackBarHostState = snackBarHostState
                )

                when (uiState) {

                    RegistrationState.Initial -> Unit

                    is RegistrationState.Error -> {
                        Toast.makeText(context, uiState.exception, Toast.LENGTH_SHORT).show()
                    }

                    RegistrationState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    RegistrationState.Success -> {
                        onVerified()
                    }
                }
            }
        }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    )
}

@Composable
fun RegistrationContent(
    register: (accountNumber: String, username: String, firstName: String, lastName: String, phoneNumber: String, email: String, password: String, authMode: String, countryCode: String) -> Unit,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            }
            .verticalScroll(
                state = scrollState,
                enabled = true
            )) {

        MifosMobileIcon(id = R.drawable.mifos_logo)

        MifosOutlinedTextField(
            value = accountNumber,
            onValueChange = { accountNumber = it },
            label = R.string.account_number,
            supportingText = ""
        )
        MifosOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = R.string.username,
            supportingText = ""
        )
        MifosOutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = R.string.first_name,
            supportingText = ""
        )
        MifosOutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = R.string.last_name,
            supportingText = ""
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CountryCodeChooser(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(5.dp),
                        color = Color.Gray
                    )
                    .padding(10.dp),
                defaultCountryCode = "91",
                countryCodeType = CountryCodeType.FLAG,
                onCountyCodeSelected = { code, codeWithPrefix ->
                    countryCode = code
                }
            )
            MifosOutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = R.string.phone_number,
                supportingText = "",
                keyboardType = KeyboardType.Number
            )
        }
        MifosOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = R.string.email,
            supportingText = ""
        )
        MifosOutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                onValueChangePassword = true
            },
            label = R.string.password,
            supportingText = "",
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, null)
                }
            },
            keyboardType = KeyboardType.Password
        )

        if (onValueChangePassword) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                color = when (progressIndicator) {
                    0.25f -> Color.Red
                    0.5f -> Color(alpha = 255, red = 220, green = 185, blue = 0)
                    0.75f -> Color.Green
                    else -> Color.Blue
                },
                progress = progressIndicator,
                trackColor = Color.White
            )
        }

        MifosOutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = R.string.confirm_password,
            supportingText = "",
            visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                    Icon(imageVector = image, null)
                }
            },
            keyboardType = KeyboardType.Password
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.verification_mode),
                modifier = Modifier.padding(end = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            radioOptions.forEach { authMode ->
                RadioButton(
                    selected = (authMode == authenticationMode),
                    onClick = { authenticationMode = authMode }
                )
                Text(
                    text = authMode,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Button(
            onClick = {
                val error = areFieldsValidated(
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
                        countryCode
                    )
                }else {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = error,
                            actionLabel = "Ok",
                            duration = SnackbarDuration.Short
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
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.register))
        }
    }
}

fun isInputFieldBlank(fieldText: String): Boolean {
    return fieldText.trim().isEmpty()
}
fun isPhoneNumberValid(fieldText: String?): Boolean {
    if (fieldText.isNullOrBlank()) {
        return false
    }

    val phoneNumberPattern = "^\\+?[0-9]{10,15}\$"
    val regex = phoneNumberPattern.toRegex()
    return regex.matches(fieldText.trim())
}

fun isInputLengthInadequate(fieldText: String): Boolean {
    return fieldText.trim().length < 6
}

fun inputHasSpaces(fieldText: String): Boolean {
    return fieldText.trim().contains(" ")
}

fun hasLeadingTrailingSpaces(fieldText: String): Boolean {
    return fieldText.trim().length < fieldText.length
}

fun isEmailInvalid(emailText: String): Boolean {
    return !PatternsCompat.EMAIL_ADDRESS.matcher(emailText.trim()).matches()
}
fun areFieldsValidated(
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
    return when {
        isInputFieldBlank(accountNumberContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.account_number)
            )
            errorMessage
        }

        isInputFieldBlank(usernameContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.username)
            )
            errorMessage
        }

        isInputLengthInadequate(usernameContent) -> {
            val errorMessage = context.getString(R.string.error_username_greater_than_six)
            errorMessage
        }

        inputHasSpaces(usernameContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_cannot_contain_spaces,
                context.getString(R.string.username),
                context.getString(R.string.not_contain_username),
            )
            errorMessage
        }

        isInputFieldBlank(firstNameContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.first_name)
            )
            errorMessage
        }

        isInputFieldBlank(lastNameContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.last_name)
            )
            errorMessage
        }

        !isPhoneNumberValid(phoneNumberContent) -> {
            val errorMessage = context.getString(R.string.invalid_phn_number)
            errorMessage
        }

        isInputFieldBlank(emailContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.email)
            )
            errorMessage
        }

        isInputFieldBlank(passwordContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.password)
            )
            errorMessage
        }

        hasLeadingTrailingSpaces(passwordContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                context.getString(R.string.password),
            )
            errorMessage
        }

        isEmailInvalid(emailContent) -> {
            val errorMessage = ContextCompat.getString(context, R.string.error_invalid_email)
            errorMessage
        }

        isInputLengthInadequate(passwordContent) -> {
            val errorMessage = context.getString(
                R.string.error_validation_minimum_chars,
                context.getString(R.string.password),
                context.resources.getInteger(R.integer.password_minimum_length),
            )
            errorMessage
        }

        passwordContent != confirmPasswordContent -> {
            val errorMessage = context.getString(R.string.error_password_not_match)
            errorMessage
        }

        else -> ""
    }
}

fun updatePasswordStrengthView(password: String, context: Context): Float {
    val str = PasswordStrength.calculateStrength(password)
    return when (str.getText(context)) {
        getString(context, R.string.password_strength_weak) -> 0.25f
        getString(context, R.string.password_strength_medium) -> 0.5f
        getString(context, R.string.password_strength_strong) -> 0.75f
        else -> 1f
    }
}

class RegistrationScreenPreviewProvider : PreviewParameterProvider<RegistrationState> {

    override val values: Sequence<RegistrationState>
    get() = sequenceOf(
        RegistrationState.Loading,
        RegistrationState.Error(1),
        RegistrationState.Success,
        RegistrationState.Initial,
    )
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable

private fun RegistrationScreenPreview(
    @PreviewParameter(RegistrationScreenPreviewProvider::class) registrationUiState: RegistrationState
) {
    MifosMobileTheme {
        RegistrationScreen(
            registrationUiState,
            {},
            {},
            { _, _, _, _, _, _, _, _, _ -> },
            { 0f }
        )

    }
}