package org.mifos.mobile.feature.login.screens

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.feature.login.R
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.login.utils.LoginState
import org.mifos.mobile.feature.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    startRegisterActivity: () -> Unit,
    startPassCodeActivity: () -> Unit,
) {

    val context = LocalContext.current
    val uiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    LoginScreen(
        uiState = uiState,
        loadClient = { viewModel.loadClient() },
        startPassCodeActivity = startPassCodeActivity,
        startRegisterActivity = startRegisterActivity,
        login = { username, password ->
            if (Network.isConnected(context)) {
                if (isCredentialsValid(username, password))
                    viewModel.login(username, password)
            } else {
                Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Composable
fun LoginScreen(
    uiState: LoginState,
    loadClient: () -> Unit,
    startPassCodeActivity: () -> Unit,
    startRegisterActivity: () -> Unit,
    login: ( username: String, password: String ) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LoginContent(
            login= login,
            createAccount = startRegisterActivity
        )

        when (uiState) {

            LoginState.Initial -> Unit

            is LoginState.Error -> {
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }

            LoginState.Loading -> {
                MifosProgressIndicatorOverlay()
            }

            is LoginState.LoginSuccess -> {
                loadClient.invoke()
            }

            is LoginState.LoadClientSuccess -> {
                startPassCodeActivity.invoke()
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, context.getString(R.string.toast_welcome, uiState.clientName), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


@Composable
fun LoginContent(
    login: (username: String, password: String) -> Unit,
    createAccount: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var username by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    var passwordVisibility: Boolean by rememberSaveable { mutableStateOf(false) }

    var usernameError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    val usernameErrorContent = validateUsername(username.text, context)
    val passwordErrorContent = validatePassword(password.text, context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            }
    ) {
        MifosMobileIcon(id = R.drawable.mifos_logo)

        MifosOutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = false
            },
            label = R.string.username,
            icon = R.drawable.ic_person_black_24dp,
            error = usernameError,
            supportingText = usernameErrorContent,
            trailingIcon = {
                if (usernameError) {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosOutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = R.string.password,
            icon = R.drawable.ic_lock_black_24dp,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (!passwordError) {
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = image, null)
                    }
                } else {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                }
            },
            error = passwordError,
            supportingText = passwordErrorContent,
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                when {
                    usernameErrorContent.isEmpty() && passwordErrorContent.isEmpty() -> {
                        login.invoke(username.text, password.text)
                    }

                    usernameErrorContent.isEmpty() && passwordErrorContent.isNotEmpty() -> {
                        passwordError = true
                    }

                    usernameErrorContent.isNotEmpty() && passwordErrorContent.isEmpty() -> {
                        usernameError = true
                    }

                    else -> {
                        passwordError = true
                        usernameError = true
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
            Text(text = stringResource(id = R.string.login))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .weight(1f), color = Color.Gray, thickness = 1.dp
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "or",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
                    .weight(1f), color = Color.Gray, thickness = 1.dp
            )
        }

        TextButton(
            onClick = {
                createAccount.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.create_an_account))
        }
    }
}

fun isFieldEmpty(fieldText: String): Boolean {
    return fieldText.isEmpty()
}

fun isUsernameLengthInadequate(username: String): Boolean {
    return username.length < 5
}

fun isPasswordLengthInadequate(password: String): Boolean {
    return password.length < 6
}

fun usernameHasSpaces(username: String): Boolean {
    return username.trim().contains(" ")
}

private fun isCredentialsValid(username: String, password: String): Boolean {
    var credentialValid = true
    when {
        isFieldEmpty(username) -> {
            credentialValid = false
        }

        isUsernameLengthInadequate(username) -> {
            credentialValid = false
        }

        usernameHasSpaces(username) -> {
            credentialValid = false
        }
    }

    when {
        isFieldEmpty(password) -> {
            credentialValid = false
        }

        isPasswordLengthInadequate(password) -> {
            credentialValid = false
        }
    }
    return credentialValid
}

private fun validateUsername(username: String, context: Context): String {
    var usernameError = ""
    when {
        isFieldEmpty(username) -> {
            usernameError = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.username),
            ).toString()
        }

        isUsernameLengthInadequate(username) -> {
            usernameError = context.getString(
                R.string.error_validation_minimum_chars,
                context.getString(R.string.username),
                context.resources?.getInteger(R.integer.username_minimum_length),
            ).toString()
        }

        usernameHasSpaces(username) -> {
            usernameError = context.getString(
                R.string.error_validation_cannot_contain_spaces,
                context.getString(R.string.username),
                context.getString(R.string.not_contain_username),
            ).toString()
        }
    }
    return usernameError
}

private fun validatePassword(password: String, context: Context): String {
    var passwordError = ""
    when {
        isFieldEmpty(password) -> {
            passwordError = context.getString(
                R.string.error_validation_blank,
                context.getString(R.string.password),
            ).toString()
        }

        isPasswordLengthInadequate(password) -> {
            passwordError = context.getString(
                R.string.error_validation_minimum_chars,
                context.getString(R.string.password),
                context.resources.getInteger(R.integer.password_minimum_length),
            ).toString()
        }
    }
    return passwordError
}

class LoginScreenPreviewProvider : PreviewParameterProvider<LoginState> {

    override val values: Sequence<LoginState>
        get() = sequenceOf(
            LoginState.Loading,
            LoginState.Error,
            LoginState.LoginSuccess,
            LoginState.LoadClientSuccess(""),
            LoginState.Initial,
        )
}

private fun showToast(context: Context, text : String){
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview(
    @PreviewParameter(LoginScreenPreviewProvider::class) loginUiState: LoginState
) {
    MifosMobileTheme {
        LoginScreen(
            loginUiState,
            {},
            {},
            {},
            { _, _ -> }
        )
    }
}