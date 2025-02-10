/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.essenty.backhandler.BackCallback
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.authentication_token
import mifos_mobile.feature.auth.generated.resources.dialog_cancel_registration_message
import mifos_mobile.feature.auth.generated.resources.dialog_cancel_registration_title
import mifos_mobile.feature.auth.generated.resources.empty_requestid
import mifos_mobile.feature.auth.generated.resources.feature_auth_mifos_logo
import mifos_mobile.feature.auth.generated.resources.no
import mifos_mobile.feature.auth.generated.resources.register
import mifos_mobile.feature.auth.generated.resources.request_id
import mifos_mobile.feature.auth.generated.resources.verify
import mifos_mobile.feature.auth.generated.resources.yes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun RegistrationVerificationScreen(
    navigateBack: () -> Unit?,
    onVerified: () -> Unit,
    viewModel: RegistrationVerificationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    BackCallback(isEnabled = true) {
        VerificationAction.ConfirmationDialog(true)
    }

    VerificationDialogs(
        dialogState = state.dialogState,
        showConfirmationDialog = state.showConfirmationDialog,
        onDismissRequest = { VerificationAction.ConfirmationDialog(false) },
        onConfirmExit = {
            VerificationAction.ConfirmationDialog(false)
            navigateBack()
        },
    )

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is VerificationEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is VerificationEvent.NavigateToLogin -> {
                onVerified.invoke()
            }

            is VerificationEvent.NavigateToRegister -> {
                navigateBack.invoke()
            }
        }
    }
    RegistrationVerificationScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        snackbarHostState = snackbarHostState,
        navigateBack = { VerificationAction.ConfirmationDialog(true) },
    )
}

@Composable
private fun VerificationDialogs(
    dialogState: VerificationState.VerificationDialog?,
    showConfirmationDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmExit: () -> Unit,
) {
    when (dialogState) {
        is VerificationState.VerificationDialog.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                title = "Error",
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        is VerificationState.VerificationDialog.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )

        null -> Unit
    }

    if (showConfirmationDialog) {
        MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                title = stringResource(Res.string.dialog_cancel_registration_title),
                message = stringResource(Res.string.dialog_cancel_registration_message),
            ),
            confirmText = stringResource(Res.string.yes),
            cancelText = stringResource(Res.string.no),
            onDismissRequest = onDismissRequest,
            onConfirm = onConfirmExit,
        )
    }
}

@Composable
private fun RegistrationVerificationScreen(
    state: VerificationState,
    onAction: (VerificationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.register),
        backPress = navigateBack,
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            ) {
                RegistrationVerificationContent(state = state, onAction = onAction)
                when (state.dialogState) {
                    is VerificationState.VerificationDialog.Loading -> MifosProgressIndicatorOverlay()
                    is VerificationState.VerificationDialog.Error -> {}
                    null -> {}
                }
            }
        },
    )
}

@Composable
private fun RegistrationVerificationContent(
    state: VerificationState,
    onAction: (VerificationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(Res.drawable.feature_auth_mifos_logo),
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
            value = state.requestId,
            onValueChange = {
                onAction(VerificationAction.RequestIdError)
                onAction(VerificationAction.RequestIdChange(it))
            },
            label = stringResource(Res.string.request_id),
            config = MifosTextFieldConfig(
                errorText = stringResource(Res.string.empty_requestid),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            ),
        )

        MifosOutlinedTextField(
            value = state.authenticationToken,
            onValueChange = {
                onAction(VerificationAction.AuthenticationTokenChange(it))
            },
            label = stringResource(Res.string.authentication_token),
            config = MifosTextFieldConfig(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            ),
        )

        MifosButton(
            text = { stringResource(Res.string.verify) },
            onClick = {
                onAction(VerificationAction.SubmitClick)
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

@DevicePreview
@Composable
fun RegistrationVerificationScreenPreview(
    state: VerificationState,
) {
    MifosMobileTheme {
        RegistrationVerificationContent(
            state = state,
            onAction = {},
        )
    }
}
