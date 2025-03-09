/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.update.password

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.update_password.generated.resources.Res
import mifos_mobile.feature.update_password.generated.resources.update_password
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopBar
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun UpdatePasswordScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePasswordViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            EditPasswordEvent.NavigateBack -> navigateBack()
            is EditPasswordEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Box(modifier) {
        EditPasswordDialogs(
            dialogState = state.dialogState,
            onDismissRequest = remember(viewModel) {
                { viewModel.trySendAction(EditPasswordAction.ErrorDialogDismiss) }
            },
        )

        UpdatePasswordScreen(
            state = state,
            snackbarHostState = snackbarHostState,
            onAction = remember(viewModel) {
                { viewModel.trySendAction(it) }
            },
        )
    }
}

@Composable
private fun UpdatePasswordScreen(
    state: EditPasswordState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onAction: (EditPasswordAction) -> Unit,
) {
    MifosScaffold(
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        topBar = {
            MifosTopBar(
                topBarTitle = stringResource(Res.string.update_password),
                backPress = {
                    onAction(EditPasswordAction.NavigateBackClick)
                },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            UpdatePasswordContent(
                onAction = onAction,
                state = state,
            )
        }
    }
}

@Composable
private fun EditPasswordDialogs(
    dialogState: EditPasswordDialog?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is EditPasswordDialog.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        is EditPasswordDialog.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )

        null -> Unit
    }
}
