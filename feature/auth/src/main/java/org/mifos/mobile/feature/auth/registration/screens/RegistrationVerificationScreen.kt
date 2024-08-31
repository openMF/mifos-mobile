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

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTextButton
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.auth.R
import org.mifos.mobile.feature.auth.registration.viewmodel.RegistrationUiState
import org.mifos.mobile.feature.auth.registration.viewmodel.RegistrationViewModel

@Composable
internal fun RegistrationVerificationScreen(
    navigateBack: () -> Unit?,
    onVerified: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.registrationVerificationUiState.collectAsStateWithLifecycle()
    var showConfirmationDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showConfirmationDialog = true
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(text = stringResource(R.string.dialog_cancel_registration_title)) },
            text = {
                Text(text = stringResource(R.string.dialog_cancel_registration_message))
            },
            modifier = modifier,
            confirmButton = {
                MifosTextButton(
                    onClick = {
                        showConfirmationDialog = false
                        navigateBack.invoke()
                    },
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                MifosTextButton(
                    onClick = { showConfirmationDialog = false },
                ) {
                    Text(text = stringResource(R.string.no))
                }
            },
        )
    }

    RegistrationVerificationScreen(
        uiState = uiState,
        verifyUser = { token, id -> viewModel.verifyUser(token, id) },
        onVerified = onVerified,
        navigateBack = { showConfirmationDialog = true },
    )
}

@Composable
private fun RegistrationVerificationScreen(
    uiState: RegistrationUiState,
    onVerified: () -> Unit,
    navigateBack: () -> Unit,
    verifyUser: (authenticationToken: String, requestID: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    MifosScaffold(
        topBarTitleResId = R.string.register,
        navigateBack = navigateBack,
        modifier = modifier,
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            ) {
                RegistrationVerificationContent(verifyUser)

                when (uiState) {
                    is RegistrationUiState.Initial -> Unit

                    is RegistrationUiState.Loading -> MifosProgressIndicatorOverlay()

                    is RegistrationUiState.Error -> {
                        Toast.makeText(context, uiState.exception, Toast.LENGTH_SHORT).show()
                    }

                    is RegistrationUiState.Success -> {
                        Toast.makeText(
                            context,
                            stringResource(R.string.verified),
                            Toast.LENGTH_SHORT,
                        ).show()
                        onVerified()
                    }
                }
            }
        },
    )
}

@Composable
private fun RegistrationVerificationContent(
    verifyUser: (authenticationToken: String, requestID: String) -> Unit,
    modifier: Modifier = Modifier,
) {
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

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(R.drawable.feature_auth_mifos_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .height(100.dp)
                .fillMaxWidth(),
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
        )

        MifosOutlinedTextField(
            value = requestID,
            onValueChange = {
                requestID = it
                requestIDError = false
            },
            label = R.string.request_id,
            supportingText = stringResource(R.string.empty_requestid),
            error = requestIDError,
            keyboardType = KeyboardType.Number,
            trailingIcon = {
                if (requestIDError) {
                    Icon(imageVector = MifosIcons.Error, contentDescription = null)
                }
            },
        )

        MifosOutlinedTextField(
            value = authenticationToken,
            onValueChange = {
                authenticationToken = it
                authenticationTokenError = false
            },
            label = R.string.authentication_token,
            supportingText = stringResource(R.string.empty_authentication_token),
            error = authenticationTokenError,
            keyboardType = KeyboardType.Number,
            trailingIcon = {
                if (authenticationTokenError) {
                    Icon(imageVector = MifosIcons.Error, contentDescription = null)
                }
            },
        )

        MifosButton(
            textResId = R.string.verify,
            onClick = {
                if (validateInput()) {
                    verifyUser(authenticationToken.toString(), requestID.toString())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

internal class RegistrationVerificationScreenPreviewProvider :
    PreviewParameterProvider<RegistrationUiState> {
    override val values: Sequence<RegistrationUiState>
        get() = sequenceOf(
            RegistrationUiState.Initial,
            RegistrationUiState.Loading,
            RegistrationUiState.Success,
            RegistrationUiState.Error(R.string.register),
        )
}

@DevicePreviews
@Composable
private fun RegistrationVerificationScreenPreview(
    @PreviewParameter(RegistrationVerificationScreenPreviewProvider::class)
    registrationUiState: RegistrationUiState,
) {
    MifosMobileTheme {
        RegistrationVerificationScreen(
            uiState = registrationUiState,
            verifyUser = { _, _ -> },
            onVerified = {},
            navigateBack = { },
        )
    }
}
