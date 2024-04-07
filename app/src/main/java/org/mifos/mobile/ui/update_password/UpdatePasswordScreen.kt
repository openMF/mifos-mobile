package org.mifos.mobile.ui.update_password

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.savings_account_withdraw.UiStatesParameterProvider
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.RegistrationUiState


@Composable
fun UpdatePasswordScreen(
    viewModel: UpdatePasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.updatePasswordUiState.collectAsStateWithLifecycle()

    UpdatePasswordScreen(
        uiState = uiState,
        navigateBack = navigateBack
    )
}

@Composable
fun UpdatePasswordScreen(
    uiState: RegistrationUiState,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var updatePasswordButtonClicked by remember {
        mutableStateOf(false)
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, topBar = {
        MifosTopBar(
            title = { Text(text = stringResource(id = R.string.change_password)) },
            navigateBack = navigateBack
        )
    }) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            UpdatePasswordContent(
                updatePasswordButtonClicked = {
                    updatePasswordButtonClicked = true
                }
            )

            when (uiState) {
                is RegistrationUiState.Loading -> {
                    MifosProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(0.8f))
                    )
                }

                is RegistrationUiState.Error -> {
                    if (updatePasswordButtonClicked) {
                        LaunchedEffect(snackbarHostState) {
                            snackbarHostState.showSnackbar(
                                context.getString(R.string.could_not_update_password_error),
                                context.getString(R.string.dialog_action_ok),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }

                is RegistrationUiState.Initial -> Unit

                is RegistrationUiState.Success -> {
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.password_changed_successfully),
                            context.getString(R.string.dialog_action_ok),
                            duration = SnackbarDuration.Short
                        )
                    }
                    navigateBack.invoke()
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun UpdatePasswordScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class) registrationUiState: RegistrationUiState
) {
    MifosMobileTheme {
        UpdatePasswordScreen(navigateBack = {})
    }
}

fun validateAndUpdatePassword(
    context: Context,
    viewModel: UpdatePasswordViewModel,
    newPassword: String,
    confirmPassword: String,
    newPasswordError: (isError: Boolean) -> Unit,
    confirmPasswordError: (isError: Boolean) -> Unit,
    setNewPasswordErrorContent: (error: String) -> Unit,
    setConfirmPasswordErrorContent: (error: String) -> Unit
) {

    var newPasswordErrorContent = getPasswordError(
        password = newPassword,
        passwordType = context.getString(R.string.new_password),
        context = context
    )
    var confirmPasswordErrorContent = getPasswordError(
        password = confirmPassword,
        passwordType = context.getString(R.string.confirm_password),
        context = context
    )

    setNewPasswordErrorContent.invoke(newPasswordErrorContent)
    setConfirmPasswordErrorContent.invoke(confirmPasswordErrorContent)

    val passwordMatches = validatePasswordMatch(newPassword, confirmPassword)

    when {
        newPasswordErrorContent.isEmpty() && confirmPasswordErrorContent.isEmpty() -> {
            if (passwordMatches && Network.isConnected(context)) {
                viewModel.updateAccountPassword(
                    newPassword = newPassword, confirmPassword = confirmPassword
                )
            } else errorComponent(context)
        }

        newPasswordErrorContent.isEmpty() && confirmPasswordErrorContent.isNotEmpty() -> {
            confirmPasswordError.invoke(true)
        }

        newPasswordErrorContent.isNotEmpty() && confirmPasswordErrorContent.isEmpty() -> {
            newPasswordError.invoke(true)
        }

        else -> {
            newPasswordError.invoke(true)
            confirmPasswordError.invoke(true)
        }
    }
}

fun getPasswordError(password: String, passwordType: String, context: Context): String {
    var passwordError = ""
    when {
        isInputFieldEmpty(password) -> {
            passwordError = context.getString(
                R.string.error_validation_blank,
                passwordType,
            )
        }

        isInputLengthInadequate(password) -> {
            passwordError = context.getString(
                R.string.error_validation_minimum_chars,
                passwordType,
                context.resources.getInteger(R.integer.password_minimum_length),
            )
        }
    }
    return passwordError
}

fun errorComponent(
    context: Context
) {
    var errorMessage: String
    if (!Network.isConnected(context)) {
        errorMessage = context.getString(R.string.no_internet_connection)
    } else errorMessage = context.getString(R.string.error_password_not_match)

    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
}

fun isInputFieldEmpty(fieldText: String): Boolean {
    return fieldText.isEmpty()
}

fun isInputLengthInadequate(fieldText: String): Boolean {
    return fieldText.length < 6
}

fun validatePasswordMatch(newPassword: String, confirmPassword: String): Boolean {
    return newPassword == confirmPassword
}





