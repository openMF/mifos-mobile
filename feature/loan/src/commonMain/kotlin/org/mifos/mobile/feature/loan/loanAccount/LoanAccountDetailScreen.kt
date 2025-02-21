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
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
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
    viewCharges: () -> Unit,
    viewRepaymentSchedule: (Long) -> Unit,
    viewTransactions: (Long) -> Unit,
    viewQr: (String) -> Unit,
    makePayment: (accountId: Long, outstandingBalance: Double?, transferType: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanAccountsDetailViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val outStanding = state.loanAccountAssociations?.summary?.totalOutstanding

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is LoanAccountsEvent.NavigateBack -> navigateBack.invoke()
            is LoanAccountsEvent.ViewGuarantor -> viewGuarantor(state.loanId)
            is LoanAccountsEvent.MakePayment -> makePayment(state.loanId, outStanding, TRANSFER_PAY_TO)
            is LoanAccountsEvent.UpdateLoan -> updateLoan(state.loanId)
            is LoanAccountsEvent.ViewCharges -> viewCharges()
            is LoanAccountsEvent.ViewLoanSummary -> viewLoanSummary(state.loanId)
            is LoanAccountsEvent.ViewQr -> viewQr(state.loanId.toString())
            is LoanAccountsEvent.ViewRepaymentSchedule -> viewRepaymentSchedule(state.loanId)
            is LoanAccountsEvent.ViewTransactions -> viewTransactions(state.loanId)
            is LoanAccountsEvent.WithDrawLoan -> withdrawLoan(state.loanId)
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
                when (state.dialogState) {
                    is LoanAccountsState.DialogState.Loading -> {
                        MifosProgressIndicator(modifier = Modifier.fillMaxSize())
                    }
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

                    is LoanAccountsState.DialogState.ApprovalPending -> {
                        EmptyDataView(
                            modifier = Modifier.fillMaxSize(),
                            // TODO() we have to replace this icon from Drawable to ImageVector
//                            icon = Res.drawable.ic_assignment_turned_in_black_24dp,
                            icon = MifosIcons.Error,
                            error = Res.string.approval_pending,
                        )
                    }

                    is LoanAccountsState.DialogState.WaitingForDisburse -> {
                        EmptyDataView(
                            modifier = Modifier.fillMaxSize(),
                            // TODO() we have to replace this icon from Drawable to ImageVector
//                            icon = Res.drawable.ic_assignment_turned_in_black_24dp,
                            icon = MifosIcons.Error,
                            error = Res.string.waiting_for_disburse,
                        )
                    }
                    else -> {
                        state.loanAccountAssociations?.let { loan ->
                            LoanAccountDetailContent(
                                loanWithAssociations = loan,
                                viewLoanSummary = { onAction(LoanAccountAction.ViewLoanSummaryClicked) },
                                viewCharges = { onAction(LoanAccountAction.ViewCharges) },
                                viewRepaymentSchedule = { onAction(LoanAccountAction.ViewRepaymentScheduleClicked) },
                                viewTransactions = { onAction(LoanAccountAction.ViewTransactionsClicked) },
                                viewQr = { onAction(LoanAccountAction.ViewQRClicked) },
                                makePayment = { onAction(LoanAccountAction.MakePaymentClicked) },
                            )
                        } ?: ErrorComponent(
                            retryConnection = {
                                onAction(
                                    LoanAccountAction
                                        .ViewLoanSummaryClicked,
                                )
                            }, isOnline = state.isOnline,
                        )
                    }
                }
            }
        },
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
//            icon = Res.drawable.ic_error_black_24dp,
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
