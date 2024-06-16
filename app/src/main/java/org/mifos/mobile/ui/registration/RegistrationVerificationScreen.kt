package org.mifos.mobile.ui.registration

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.RegistrationUiState

@Composable
fun RegistrationVerificationScreen(
    navigateBack: () -> Unit?,
    onVerified: () -> Unit
) {

    val context = LocalContext.current
    var showConfirmationDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showConfirmationDialog = true
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(text = getString(context, R.string.dialog_cancel_registration_title)) },
            text = { Text(text = getString(context, R.string.dialog_cancel_registration_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                    navigateBack.invoke()
                }) {
                    Text(text = getString(context, R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text(text = getString(context, R.string.no))
                }
            }
        )
    }

    val viewModel: RegistrationViewModel = hiltViewModel()
    val uiState by viewModel.registrationVerificationUiState.collectAsStateWithLifecycle()

    RegistrationVerificationScreen(
        uiState = uiState,
        verifyUser = { token, id -> viewModel.verifyUser(token, id) },
        onVerified = onVerified,
        navigateBack = { showConfirmationDialog = true }
    )
}


@Composable
fun RegistrationVerificationScreen(
    uiState: RegistrationUiState,
    verifyUser: (authenticationToken: String, requestID: String) -> Unit,
    onVerified: () -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    MFScaffold(
        topBarTitleResId = R.string.register,
        navigateBack = navigateBack,
        scaffoldContent = { contentPadding ->

            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {

                RegistrationVerificationContent(verifyUser)

                when (uiState) {

                    RegistrationUiState.Initial -> Unit

                    is RegistrationUiState.Error -> {
                        Toast.makeText(context, uiState.exception, Toast.LENGTH_SHORT).show()
                    }

                    RegistrationUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    RegistrationUiState.Success -> {
                        Toast.makeText(
                            context,
                            getString(context, R.string.verified),
                            Toast.LENGTH_SHORT
                        ).show()
                        onVerified()
                    }
                }
            }
        })
}

@Composable
fun RegistrationVerificationContent(verifyUser: (authenticationToken: String, requestID: String) -> Unit) {

    val context = LocalContext.current

    var requestID by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var authenticationToken by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var requestIDError by remember { mutableStateOf(false) }
    var authenticationTokenError by remember { mutableStateOf(false) }

    fun validateInput(): Boolean {

        var temp = true
        if (requestID.text.isEmpty()) {
            requestIDError = true
            temp = false
        }
        if (authenticationToken.text.isEmpty()) {
            authenticationTokenError = true
            temp = false
        }

        return temp
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.mifos_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.Mifos_DesignSystem_Spacing_screenHorizontalMargin))
                .height(dimensionResource(id = R.dimen.Mifos_DesignSystem_Size_LogoImageSize))
                .fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        MifosOutlinedTextField(
            value = requestID,
            onValueChange = {
                requestID = it
                requestIDError = false
            },
            label = R.string.request_id,
            supportingText = getString(context, R.string.empty_requestid) ,
            error = requestIDError,
            keyboardType = KeyboardType.Number,
            trailingIcon = {
                if (requestIDError) {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                }
            }
        )

        MifosOutlinedTextField(
            value = authenticationToken,
            onValueChange = {
                authenticationToken = it
                authenticationTokenError = false
            },
            label = R.string.authentication_token,
            supportingText = getString(context, R.string.empty_authentication_token),
            error = authenticationTokenError,
            keyboardType = KeyboardType.Number,
            trailingIcon = {
                if (authenticationTokenError) {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                }
            }
        )

        Button(
            onClick = {
                if (validateInput())
                    verifyUser(authenticationToken.toString(), requestID.toString())
            },
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color(0xFF9bb1e3)
                else Color(0xFF325ca8)
            )
        ) {
            Text(text = stringResource(id = R.string.verify))
        }
    }
}


class RegistrationVerificationScreenPreviewProvider :
    PreviewParameterProvider<RegistrationUiState> {
    override val values: Sequence<RegistrationUiState>
        get() = sequenceOf(
            RegistrationUiState.Initial,
            RegistrationUiState.Loading,
            RegistrationUiState.Success,
            RegistrationUiState.Error(R.string.register)
        )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun RegistrationVerificationScreenPreview(
    @PreviewParameter(RegistrationVerificationScreenPreviewProvider::class) registrationUiState: RegistrationUiState
) {
    MifosMobileTheme {
        RegistrationVerificationScreen(
            uiState = registrationUiState,
            verifyUser = { _, _ -> },
            onVerified = {},
            navigateBack = { }
        )
    }
}