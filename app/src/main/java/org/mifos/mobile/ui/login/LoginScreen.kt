package org.mifos.mobile.ui.login

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    login: (username: String, password: String) -> Unit,
    createAccount: () -> Unit,
    getUsernameError: (String) -> String,
    getPasswordError: (String) -> String
) {
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

    val usernameErrorContent = getUsernameError.invoke(username.text)
    val passwordErrorContent = getPasswordError.invoke(password.text)


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

@Preview(showSystemUi = true, device = "id:pixel_5")
@Composable
fun LoanScreenPreview() {
    MifosMobileTheme {
        LoginScreen({ _, _ -> }, {}, { "" }, { "" })
    }
}
