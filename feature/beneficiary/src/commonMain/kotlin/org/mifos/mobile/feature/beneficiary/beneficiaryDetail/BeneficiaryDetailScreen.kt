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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_detail
import mifos_mobile.feature.beneficiary.generated.resources.cancel
import mifos_mobile.feature.beneficiary.generated.resources.delete
import mifos_mobile.feature.beneficiary.generated.resources.delete_beneficiary
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun BeneficiaryDetailScreen(
    navigateBack: () -> Unit,
    updateBeneficiary: (beneficiaryId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryDetailViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.trySendAction(BeneficiaryDetailAction.OnRefresh)
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryDetailEvent.Navigate -> navigateBack.invoke()
            is BeneficiaryDetailEvent.UpdateBeneficiary -> {
                updateBeneficiary(event.beneficiaryId)
            }
        }
    }

    BeneficiaryDetailScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun BeneficiaryDialogs(
    state: BeneficiaryDetailState,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    when (state.beneficiaryDialog) {
        BeneficiaryDetailState.DialogState.Loading -> MifosProgressIndicator()
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
                icon = MifosIcons.Delete,
            )
        }
        null -> Unit
    }
}

@Composable
private fun BeneficiaryDetailScreen(
    state: BeneficiaryDetailState,
    onAction: (BeneficiaryDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.beneficiary_detail),
        onNavigateBack = { onAction(BeneficiaryDetailAction.OnNavigate) },
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier,
        ) {
            if (state.beneficiary != null && state.beneficiaryDialog == null) {
                BeneficiaryDetailContent(
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }
    BeneficiaryDialogs(
        state = state,
        onDismissRequest = { onAction(BeneficiaryDetailAction.ErrorDialogDismiss) },
        onConfirmDelete = { onAction(BeneficiaryDetailAction.DeleteBeneficiary) },
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
        )
    }
}
