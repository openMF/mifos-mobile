/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_detail
import mifos_mobile.feature.beneficiary.generated.resources.cancel
import mifos_mobile.feature.beneficiary.generated.resources.delete
import mifos_mobile.feature.beneficiary.generated.resources.delete_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.update_beneficiary
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun BeneficiaryDetailScreen(
    navigateBack: () -> Unit,
    updateBeneficiary: (beneficiary: Beneficiary?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryDetailViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.trySendAction(BeneficiaryDetailAction.OnRefresh)
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryDetailEvent.Navigate -> navigateBack.invoke()
            is BeneficiaryDetailEvent.UpdateBeneficiary -> {
                updateBeneficiary(event.beneficiary)
            }
            is BeneficiaryDetailEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    BeneficiaryDetailScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun BeneficiaryDialogs(
    state: BeneficiaryDetailState,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    when (state.beneficiaryDialog) {
        BeneficiaryDetailState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        is BeneficiaryDetailState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.beneficiaryDialog.message,
                ),
                onDismissRequest = onDismissRequest,
            )
        }
        is BeneficiaryDetailState.DialogState.Confirmation -> {
            MifosAlertDialog(
                onDismissRequest = onDismissRequest,
                dismissText = stringResource(Res.string.cancel),
                onConfirmation = onConfirmDelete,
                confirmationText = stringResource(Res.string.delete),
                dialogTitle = stringResource(Res.string.delete_beneficiary),
                dialogText = state.beneficiaryDialog.message,
            )
        }
        null -> Unit
    }
}

@Composable
private fun BeneficiaryDetailScreen(
    state: BeneficiaryDetailState,
    onAction: (BeneficiaryDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    var openDropdown by rememberSaveable { mutableStateOf(false) }

    MifosScaffold(
        topBar = {
            BeneficiaryDetailTopAppBar(
                navigateBack = { onAction(BeneficiaryDetailAction.OnNavigate) },
                updateBeneficiaryClicked = { onAction(BeneficiaryDetailAction.OnUpdateBeneficiary(state.beneficiary)) },
                updateDropdownValue = { value ->
                    openDropdown = value
                    openDropdown
                },
                showAlert = {
                    onAction(BeneficiaryDetailAction.ShowDeleteConfirmation)
                },
            )
        },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.padding(it),
        ) {
            if (state.beneficiary != null) {
                BeneficiaryDetailContent(
                    state = state,
                )
            }
        }
    }
    BeneficiaryDialogs(
        state = state,
        onDismissRequest = { onAction(BeneficiaryDetailAction.ErrorDialogDismiss) },
        onConfirmDelete = { onAction(BeneficiaryDetailAction.DeleteBeneficiary(state.beneficiary?.id)) },
    )
}

@Composable
private fun BeneficiaryDetailTopAppBar(
    navigateBack: () -> Unit,
    updateBeneficiaryClicked: () -> Unit,
    updateDropdownValue: (value: Boolean) -> Boolean,
    showAlert: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var openDropdown by rememberSaveable {
        mutableStateOf(false)
    }

    MifosTopAppBar(
        backPress = navigateBack,
        topBarTitle = stringResource(Res.string.beneficiary_detail),
        actions = {
            IconButton(
                onClick = { openDropdown = updateDropdownValue.invoke(!openDropdown) },
            ) {
                Icon(
                    imageVector = MifosIcons.MoreVert,
                    contentDescription = "More",
                )
            }
            DropdownMenu(
                expanded = openDropdown,
                onDismissRequest = {
                    openDropdown = updateDropdownValue.invoke(!openDropdown)
                },
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(Res.string.update_beneficiary)) },
                    onClick = {
                        openDropdown = updateDropdownValue.invoke(!openDropdown)
                        updateBeneficiaryClicked.invoke()
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(Res.string.delete_beneficiary)) },
                    onClick = {
                        openDropdown = updateDropdownValue.invoke(!openDropdown)
                        showAlert.invoke()
                    },
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
@Preview
private fun PreviewBeneficiaryDetailScreen() {
    MifosMobileTheme {
        BeneficiaryDetailScreen(
            state = BeneficiaryDetailState(beneficiaryDialog = null),
            onAction = { },
            modifier = Modifier,
            snackbarHostState = SnackbarHostState(),
        )
    }
}
