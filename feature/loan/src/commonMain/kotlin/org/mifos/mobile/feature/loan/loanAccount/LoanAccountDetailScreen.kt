/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccount

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.approval_pending
import mifos_mobile.feature.loan.generated.resources.loan_account_details
import mifos_mobile.feature.loan.generated.resources.no_internet_connection
import mifos_mobile.feature.loan.generated.resources.waiting_for_disburse
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.TransferArgs
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun LoanAccountDetailScreen(
    navigateBack: () -> Unit,
    viewGuarantor: (loanId: Long) -> Unit,
    updateLoan: (Long) -> Unit,
    withdrawLoan: (Long) -> Unit,
    viewLoanSummary: (Long) -> Unit,
    viewCharges: (ChargeType, Long) -> Unit,
    viewRepaymentSchedule: (Long) -> Unit,
    viewTransactions: (Long) -> Unit,
    viewQr: (String) -> Unit,
    makePayment: (
        TransferArgs,
    ) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanAccountsDetailViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is LoanAccountsEvent.NavigateBack -> navigateBack.invoke()
            is LoanAccountsEvent.ViewGuarantor -> state.loanId?.let { viewGuarantor(it) }
            is LoanAccountsEvent.MakePayment -> state.loanId?.let {
                makePayment(
                    event.transferArgs,
                )
            }
            is LoanAccountsEvent.UpdateLoan -> state.loanId?.let { updateLoan(it) }
            is LoanAccountsEvent.ViewCharges -> {
                viewCharges(
                    ChargeType.LOAN,
                    state.loanId ?: -1L,
                )
            }
            is LoanAccountsEvent.ViewLoanSummary -> state.loanId?.let { viewLoanSummary(it) }
            is LoanAccountsEvent.ViewQr -> {
                viewQr(event.qrArgs)
            }
            is LoanAccountsEvent.ViewRepaymentSchedule -> state.loanId?.let {
                viewRepaymentSchedule(it)
            }
            is LoanAccountsEvent.ViewTransactions -> state.loanId?.let { viewTransactions(it) }
            is LoanAccountsEvent.WithDrawLoan -> state.loanId?.let { withdrawLoan(it) }
            is LoanAccountsEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LoanAccountDetailScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun LoanAccountDetailDialog(
    dialogState: LoanAccountsState.DialogState?,
    onAction: (LoanAccountAction) -> Unit,
    state: LoanAccountsState,
) {
    when (dialogState) {
        is LoanAccountsState.DialogState.Error -> {
            ErrorComponent(
                retryConnection = {
                    onAction(
                        LoanAccountAction
                            .RetryConnectionClicked,
                    )
                },
                isOnline = state.isOnline,
            )
        }

        is LoanAccountsState.DialogState.Loading -> MifosProgressIndicator()
        LoanAccountsState.DialogState.ApprovalPending -> EmptyDataView(
            modifier = Modifier.fillMaxSize(),
            icon = MifosIcons.Error,
            error = Res.string.approval_pending,
        )
        LoanAccountsState.DialogState.WaitingForDisburse -> EmptyDataView(
            modifier = Modifier.fillMaxSize(),
            icon = MifosIcons.Error,
            error = Res.string.waiting_for_disburse,
        )
        null -> Unit
    }
}

@Composable
private fun LoanAccountDetailScreen(
    state: LoanAccountsState,
    onAction: (LoanAccountAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        modifier = modifier,
        topBar = {
            LoanAccountDetailTopBar(
                navigateBack = { onAction(LoanAccountAction.BackPress) },
                viewGuarantor = { onAction(LoanAccountAction.ViewGuarantorClicked) },
                updateLoan = { onAction(LoanAccountAction.UpdateLoanClicked) },
                withdrawLoan = { onAction(LoanAccountAction.WithDrawLoanClicked) },
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                if (state.loanAccountAssociations != null) {
                    LoanAccountDetailContent(
                        loanWithAssociations = state.loanAccountAssociations,
                        viewLoanSummary = { onAction(LoanAccountAction.ViewLoanSummaryClicked) },
                        viewCharges = { onAction(LoanAccountAction.ViewCharges) },
                        viewRepaymentSchedule = { onAction(LoanAccountAction.ViewRepaymentScheduleClicked) },
                        viewTransactions = { onAction(LoanAccountAction.ViewTransactionsClicked) },
                        viewQr = { onAction(LoanAccountAction.ViewQRClicked) },
                        makePayment = { onAction(LoanAccountAction.MakePaymentClicked) },
                    )
                }
            }
        },
    )
    LoanAccountDetailDialog(
        dialogState = state.dialogState,
        state = state,
        onAction = onAction,
    )
}

@Composable
private fun ErrorComponent(
    retryConnection: () -> Unit,
    isOnline: Boolean,
) {
    if (!isOnline) {
        NoInternet(
            error = Res.string.no_internet_connection,
            isRetryEnabled = true,
            retry = retryConnection,
        )
    } else {
        EmptyDataView(
            icon = MifosIcons.Error,
            error = Res.string.loan_account_details,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun LoanAccountDetailScreenPreview() {
    MifosMobileTheme {
        LoanAccountDetailScreen(
            state = LoanAccountsState(dialogState = null),
            modifier = Modifier,
            onAction = {},
        )
    }
}
