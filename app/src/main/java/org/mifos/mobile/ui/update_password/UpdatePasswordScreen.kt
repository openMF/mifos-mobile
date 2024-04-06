package org.mifos.mobile.ui.update_password

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorWithText
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.utils.RegistrationUiState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdatePasswordScreen(
    viewModel: UpdatePasswordViewModel = hiltViewModel(),
    changePassword: (newPassword: String, confirmPassword: String) -> Unit,
    getNewPasswordError: (newPassword: String) -> String,
    getConfirmPasswordError: (confirmPassword: String) -> String,
    getBackToPreviousScreen: () -> Unit
) {
    var newPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var confirmPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var viewNewPassword by rememberSaveable {
        mutableStateOf(false)
    }
    var viewConfirmPassword by rememberSaveable {
        mutableStateOf(false)
    }

    var newPasswordError by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordError by rememberSaveable { mutableStateOf(false) }

    var newPasswordErrorContent by rememberSaveable {
        mutableStateOf("")
    }
    var confirmPasswordErrorContent by rememberSaveable {
        mutableStateOf("")
    }

    val uiState by viewModel.updatePasswordUiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    when (uiState) {
        RegistrationUiState.Loading -> {
            MifosProgressIndicatorWithText(text = stringResource(id = R.string.progress_message_loading))
        }

        is RegistrationUiState.Error -> {
            LaunchedEffect(key1 = true) {
                Toast.makeText(
                    context, R.string.could_not_update_password_error, Toast.LENGTH_SHORT
                ).show()
            }
        }

        RegistrationUiState.Initial -> Unit

        RegistrationUiState.Success -> {
            LaunchedEffect(key1 = true) {
                Toast.makeText(context, R.string.password_changed_successfully, Toast.LENGTH_SHORT)
                    .show()
                getBackToPreviousScreen.invoke()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MifosTopBar(
            title = {
                Text(text = stringResource(id = R.string.change_password))
            }, navigateBack = getBackToPreviousScreen
        )

        MifosOutlinedTextField(
            value = newPassword,
            onValueChange = {
                newPassword = it
                newPasswordError = false
            },
            label = R.string.new_password,
            supportingText = newPasswordErrorContent,
            icon = R.drawable.ic_lock_black_24dp,
            trailingIcon = {
                var image = if (viewNewPassword) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                if (!newPasswordError) {
                    IconButton(onClick = { viewNewPassword = !viewNewPassword }) {
                        Icon(
                            imageVector = image, contentDescription = "password visibility button"
                        )
                    }
                } else {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                }
            },
            error = newPasswordError,
            visualTransformation = if (viewNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosOutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = false
            },
            label = R.string.confirm_password,
            supportingText = confirmPasswordErrorContent,
            icon = R.drawable.ic_lock_black_24dp,
            trailingIcon = {
                var image = if (viewConfirmPassword) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                if (!confirmPasswordError) {
                    IconButton(onClick = { viewConfirmPassword = !viewConfirmPassword }) {
                        Icon(imageVector = image, contentDescription = "password visibility Button")
                    }
                } else {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                }
            },
            error = confirmPasswordError,
            visualTransformation = if (viewConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password

        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                keyboardController?.hide()
                newPasswordErrorContent = getNewPasswordError.invoke(newPassword.text)
                confirmPasswordErrorContent = getConfirmPasswordError.invoke(confirmPassword.text)

                when {
                    newPasswordErrorContent.isEmpty() && confirmPasswordErrorContent.isEmpty() -> {
                        changePassword.invoke(
                            newPassword.text, confirmPassword.text
                        )

                    }

                    newPasswordErrorContent.isEmpty() && confirmPasswordErrorContent.isNotEmpty() -> {
                        confirmPasswordError = true
                    }

                    newPasswordErrorContent.isNotEmpty() && confirmPasswordErrorContent.isEmpty() -> {
                        newPasswordError = true
                    }

                    else -> {
                        confirmPasswordError = true
                        newPasswordError = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp),
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color(
                    0xFF9bb1e3
                ) else Color(0xFF325ca8)
            )
        ) {
            Text(text = stringResource(id = R.string.change_password))
        }

    }
}

@Composable
@Preview(showBackground = true, uiMode = 2)
fun PreviewUpdatePasswordScreen() {
    UpdatePasswordScreen(changePassword = { newPassword, confirmPassword -> },
        getNewPasswordError = { "" },
        getConfirmPasswordError = { "" },
        getBackToPreviousScreen = { })
}

