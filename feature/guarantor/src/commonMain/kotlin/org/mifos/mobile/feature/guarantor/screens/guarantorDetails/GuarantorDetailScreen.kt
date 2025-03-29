/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorDetails

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
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.delete_guarantor
import mifos_mobile.feature.guarantor.generated.resources.dialog_are_you_sure_that_you_want_to_string
import mifos_mobile.feature.guarantor.generated.resources.dismiss
import mifos_mobile.feature.guarantor.generated.resources.guarantor_details
import mifos_mobile.feature.guarantor.generated.resources.update_guarantor
import mifos_mobile.feature.guarantor.generated.resources.yes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosDropdownMenu
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun GuarantorDetailScreen(
    navigateBack: () -> Unit,
    updateGuarantor: (index: Int, loanId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuarantorDetailViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            GuarantorDetailEvent.NavigateBack -> navigateBack()

            is GuarantorDetailEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is GuarantorDetailEvent.UpdateGuarantor -> {
                updateGuarantor.invoke(event.index, event.loanId)
            }
        }
    }

    GuarantorDetailScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun GuarantorDetailScreen(
    state: GuarantorDetailState,
    onAction: (GuarantorDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        modifier = modifier,
        topBar = {
            MifosTopAppBar(
                topBarTitle = stringResource(Res.string.guarantor_details),
                backPress = { onAction(GuarantorDetailAction.NavigateBack) },
                actions = {
                    MifosDropdownMenu(
                        menuItems = listOf(
                            stringResource(Res.string.update_guarantor)
                                to { onAction(GuarantorDetailAction.UpdateGuarantor) },
                            stringResource(Res.string.delete_guarantor)
                                to { onAction(GuarantorDetailAction.UpdateMenuDialogValue) },
                        ),
                    )
                },
            )
        },
        snackbarHostState = snackbarHostState,
        content = {
            Box(modifier = Modifier.padding(it)) {
                state.guarantor?.let { it1 -> GuarantorDetailContent(data = it1) }
            }
        },
    )

    GuarantorDetailsDialog(
        alertDialogState = state.showDialog,
        dialogState = state.dialogState,
        onDismissRequest = { onAction.invoke(GuarantorDetailAction.DismissDialog) },
        onAction = onAction,
    )
}

@Composable
private fun GuarantorDetailsDialog(
    alertDialogState: Boolean,
    dialogState: GuarantorDetailState.DialogState?,
    onDismissRequest: () -> Unit,
    onAction: (GuarantorDetailAction) -> Unit,
) {
    when (dialogState) {
        GuarantorDetailState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        is GuarantorDetailState.DialogState.ShowToast -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }

    if (alertDialogState) {
        MifosAlertDialog(
            onDismissRequest = { onAction.invoke(GuarantorDetailAction.UpdateMenuDialogValue) },
            dismissText = stringResource(Res.string.dismiss),
            confirmationText = stringResource(Res.string.yes),
            dialogTitle = stringResource(Res.string.delete_guarantor),
            onConfirmation = {
                onAction.invoke(GuarantorDetailAction.DeleteGuarantor)
                onAction.invoke(GuarantorDetailAction.UpdateMenuDialogValue)
            },
            dialogText = stringResource(
                Res.string.dialog_are_you_sure_that_you_want_to_string,
                stringResource(Res.string.delete_guarantor),
            ),
        )
    }
}
