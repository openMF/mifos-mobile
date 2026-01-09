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
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * A composable function to display the beneficiary detail screen.
 *
 * @param navigateBack A callback to navigate back from the beneficiary detail screen.
 * @param updateBeneficiary A callback to update a beneficiary.
 * @param modifier The modifier to apply to the composable.
 * @param viewModel The view model to use for the beneficiary detail screen.
 */

@Composable
internal fun BeneficiaryDetailScreen(
    navigateBack: () -> Unit,
    updateBeneficiary: (beneficiaryId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryDetailViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryDetailEvent.NavigateBack -> navigateBack.invoke()
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

    BeneficiaryDialogs(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * A composable function to display the beneficiary detail dialogs.
 *
 * @param state The view state to use for the beneficiary detail dialogs.
 * @param onAction A callback to handle the dialog actions.
 */
@Composable
private fun BeneficiaryDialogs(
    state: BeneficiaryDetailState,
    onAction: (BeneficiaryDetailAction) -> Unit,
) {
    when (state.beneficiaryDialog) {
        is BeneficiaryDetailState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.beneficiaryDialog.message,
                ),
                onDismissRequest = { onAction(BeneficiaryDetailAction.ErrorDialogDismiss) },
            )
        }

        is BeneficiaryDetailState.DialogState.Confirmation -> {
            MifosAlertDialog(
                onDismissRequest = { onAction(BeneficiaryDetailAction.ErrorDialogDismiss) },
                dismissText = stringResource(Res.string.cancel),
                onConfirmation = { onAction(BeneficiaryDetailAction.DeleteBeneficiary) },
                confirmationText = stringResource(Res.string.delete),
                dialogTitle = stringResource(Res.string.delete_beneficiary),
                dialogText = state.beneficiaryDialog.message,
                icon = MifosIcons.Delete,
            )
        }

        null -> Unit
    }
}

/**
 * A composable function to display the beneficiary detail screen.
 *
 * @param state The view state to use for the beneficiary detail screen.
 * @param onAction A callback to handle the actions from the view state.
 * @param modifier The modifier to apply to the composable.
 */
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
            when (state.uiState) {
                is ScreenUiState.Error -> {
                    MifosErrorComponent(
                        isRetryEnabled = true,
                        message = stringResource(state.uiState.message),
                        onRetry = { onAction(BeneficiaryDetailAction.OnRefresh) },
                    )
                }

                ScreenUiState.Loading -> MifosProgressIndicator()

                ScreenUiState.Network -> {
                    MifosErrorComponent(
                        isNetworkConnected = state.networkStatus,
                        isRetryEnabled = true,
                        onRetry = { onAction(BeneficiaryDetailAction.OnRefresh) },
                    )
                }

                ScreenUiState.Success -> {
                    BeneficiaryDetailContent(
                        state = state,
                        onAction = onAction,
                    )
                }

                else -> { }
            }
        }
    }
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
