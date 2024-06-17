package org.mifos.mobile.ui.registration

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.owlbuddy.www.countrycodechooser.CountryCodeChooser
import com.owlbuddy.www.countrycodechooser.utils.enums.CountryCodeType
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosMobileIcon
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField

/**
 * @author pratyush
 * @since 28/12/2023
 */

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreen(
    register: (accountNumber: String, username: String, firstName: String, lastName: String, phoneNumber: String, email: String, password: String, authMode: String, countryCode: String) -> Unit,
    progress: (String) -> Float,
) {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            }) {

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
                supportingText = ""
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
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
            radioOptions.forEach { authMode ->
                RadioButton(
                    selected = (authMode == authenticationMode),
                    onClick = { authenticationMode = authMode }
                )
                Text(
                    text = authMode,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }

        Button(
            onClick = {
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
                keyboardController?.hide()
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
            Text(text = stringResource(id = R.string.register))
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen({ _, _, _, _, _, _, _, _, _ -> }, { 0f })
}