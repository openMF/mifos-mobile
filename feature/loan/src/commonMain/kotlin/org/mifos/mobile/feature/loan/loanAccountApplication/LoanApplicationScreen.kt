/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountApplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.apply_for_loan
import mifos_mobile.feature.loan.generated.resources.update_loan
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun LoanApplicationScreen(
    navigateBack: () -> Unit,
    reviewNewLoanApplication: (
        loanState: LoanState,
        loansPayloadString: LoansPayload,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
    submitUpdateLoanApplication: (
        loanState: LoanState,
        loansPayloadString: LoansPayload,
        loanId: Long?,
        loanName: String,
        accountNo: String,
    ) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanApplicationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            LoanApplicationEvent.NavigateBack -> navigateBack.invoke()
            is LoanApplicationEvent.ReviewLoanApplication -> {
                reviewNewLoanApplication(
                    event.loanState,
                    event.loansPayloadString,
                    state.loanId,
                    event.loanName,
                    event.accountNo,
                )
            }
            is LoanApplicationEvent.SubmitUpdateLoanApplication -> {
                submitUpdateLoanApplication(
                    event.loanState,
                    event.loansPayloadString,
                    state.loanId,
                    event.loanName,
                    event.accountNo,
                )
            }
        }
    }

    LaunchedEffect(key1 = state) {
        viewModel.loadLoanApplicationTemplate(state.loanState)
    }

    LoanApplicationScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,

    )
}

@Composable
private fun LoanApplicationDialog(
    dialogState: LoanApplicationState.DialogState?,
    onAction: (LoanApplicationAction) -> Unit,
    state: LoanApplicationState,
) {
    when (dialogState) {
        is LoanApplicationState.DialogState.Error -> MifosErrorComponent(
            isNetworkConnected = state.isOnline,
            isEmptyData = false,
            isRetryEnabled = true,
            onRetry = { onAction(LoanApplicationAction.Retry) },
        )
        LoanApplicationState.DialogState.Loading -> MifosProgressIndicator(modifier = Modifier.fillMaxSize())
        null -> Unit
    }
}

@Composable
private fun LoanApplicationScreen(
    state: LoanApplicationState,
    modifier: Modifier = Modifier,
    onAction: (LoanApplicationAction) -> Unit,
) {
    MifosScaffold(
        modifier = modifier,
        topBar = {
            MifosTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backPress = { onAction(LoanApplicationAction.BackPress) },
                topBarTitle =
                stringResource(
                    if (state.loanState == LoanState.CREATE) {
                        Res.string.apply_for_loan
                    } else {
                        Res.string.update_loan
                    },
                ),
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    LoanApplicationContent(
                        state = state,
                        selectProduct = { position ->
                            (onAction(LoanApplicationAction.ProductSelected(position)))
                        },
                        selectPurpose = { position ->
                            onAction(LoanApplicationAction.PurposeSelected(position))
                        },
                        reviewClicked = { principalAmount ->
                            onAction(LoanApplicationAction.SetPrincipalAmount(principalAmount))

                            onAction(
                                LoanApplicationAction.ReviewClicked
                                    (state.reviewNewLoanApplication, state.submitUpdateLoanApplication),
                            )
                        },
                        setDisbursementDate = { data ->
                            onAction(LoanApplicationAction.SetDisburseDate(data))
                        },
                    )
                }
            }
        },
    )
    LoanApplicationDialog(
        dialogState = state.dialogState,
        state = state,
        onAction = onAction,
    )
}

@Preview
@Composable
private fun ReviewLoanApplicationScreenPreview() {
    MifosMobileTheme {
        LoanApplicationScreen(
            state = LoanApplicationState(dialogState = null),
            onAction = {},
            modifier = Modifier,

        )
    }
}
