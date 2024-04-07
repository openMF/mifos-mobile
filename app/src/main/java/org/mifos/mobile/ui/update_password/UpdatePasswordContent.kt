package org.mifos.mobile.ui.update_password

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdatePasswordContent(
    viewModel: UpdatePasswordViewModel = hiltViewModel(),
    updatePasswordButtonClicked : () -> Unit
) {
    var newPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var confirmPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var viewNewPassword by rememberSaveable { mutableStateOf(false) }
    var viewConfirmPassword by rememberSaveable { mutableStateOf(false) }

    var newPasswordError by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordError by rememberSaveable { mutableStateOf(false) }

    var newPasswordErrorContent by rememberSaveable { mutableStateOf("") }
    var confirmPasswordErrorContent by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

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
                        Icon(
                            imageVector = image, contentDescription = "password visibility Button"
                        )
                    }
                } else {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                }
            },
            error = confirmPasswordError,
            visualTransformation = if (viewConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password
        )

        Button(
            onClick = {
                keyboardController?.hide()
                updatePasswordButtonClicked.invoke()

                validateAndUpdatePassword(context,
                    viewModel,
                    newPassword.text,
                    confirmPassword.text,
                    newPasswordError = {
                        newPasswordError = it
                    },
                    confirmPasswordError = {
                        confirmPasswordError = it
                    },
                    setNewPasswordErrorContent = {
                        newPasswordErrorContent = it
                    },
                    setConfirmPasswordErrorContent = {
                        confirmPasswordErrorContent = it
                    })
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp),
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.change_password))
        }

    }
}
