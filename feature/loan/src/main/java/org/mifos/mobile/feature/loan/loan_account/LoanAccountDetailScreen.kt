package org.mifos.mobile.feature.loan.loan_account

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.feature.loan.R

@Composable
fun LoanAccountDetailScreen(
    viewModel: LoanAccountsDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    viewGuarantor: (loanId: Long) -> Unit,
    updateLoan: (Long) -> Unit,
    withdrawLoan: (Long) -> Unit,
    viewLoanSummary: (Long) -> Unit,
    viewCharges: () -> Unit,
    viewRepaymentSchedule: (Long) -> Unit,
    viewTransactions: (Long) -> Unit,
    viewQr: (String) -> Unit,
    makePayment: (accountId: Long, outstandingBalance: Double?, transferType: String) -> Unit
) {
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()
    val loanId by viewModel.loanId.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = loanId) {
        viewModel.loadLoanAccountDetails()
    }

    LoanAccountDetailScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        viewGuarantor = { viewGuarantor(loanId) },
        updateLoan = { updateLoan(loanId) },
        withdrawLoan = { withdrawLoan(loanId) },
        retryConnection = { viewModel.loadLoanAccountDetails() },
        viewLoanSummary = { viewLoanSummary(loanId) },
        viewCharges = viewCharges,
        viewRepaymentSchedule = { viewRepaymentSchedule(loanId) },
        viewTransactions = { viewTransactions(loanId) },
        viewQr = { viewQr(viewModel.getQrString()) },
        makePayment = { makePayment(loanId, viewModel.loanWithAssociations?.summary?.totalOutstanding, TRANSFER_PAY_TO) }
    )
}


@Composable
fun LoanAccountDetailScreen(
    uiState: LoanAccountDetailUiState,
    navigateBack: () -> Unit,
    viewGuarantor: () -> Unit,
    updateLoan: () -> Unit,
    withdrawLoan: () -> Unit,
    retryConnection: () -> Unit,
    viewLoanSummary: () -> Unit,
    viewCharges: () -> Unit,
    viewRepaymentSchedule: () -> Unit,
    viewTransactions: () -> Unit,
    viewQr: () -> Unit,
    makePayment: () -> Unit
) {
    MFScaffold(
        topBar = {
            LoanAccountDetailTopBar(
                navigateBack = navigateBack,
                viewGuarantor = viewGuarantor,
                updateLoan = updateLoan,
                withdrawLoan = withdrawLoan
            )
        },
        scaffoldContent = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is LoanAccountDetailUiState.Success -> {
                        LoanAccountDetailContent(
                            loanWithAssociations = uiState.loanWithAssociations,
                            viewLoanSummary = viewLoanSummary,
                            viewCharges = viewCharges,
                            viewRepaymentSchedule = viewRepaymentSchedule,
                            viewTransactions = viewTransactions,
                            viewQr = viewQr,
                            makePayment = makePayment
                        )
                    }

                    is LoanAccountDetailUiState.Loading -> {
                        MifosProgressIndicator(modifier = Modifier.fillMaxSize())
                    }

                    is LoanAccountDetailUiState.Error -> {
                        ErrorComponent(retryConnection = retryConnection)
                    }

                    is LoanAccountDetailUiState.ApprovalPending -> {
                        EmptyDataView(
                            modifier = Modifier.fillMaxSize(),
                            icon = R.drawable.ic_assignment_turned_in_black_24dp,
                            error = R.string.approval_pending
                        )
                    }

                    is LoanAccountDetailUiState.WaitingForDisburse -> {
                        EmptyDataView(
                            modifier = Modifier.fillMaxSize(),
                            icon = R.drawable.ic_assignment_turned_in_black_24dp,
                            error = R.string.waiting_for_disburse
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ErrorComponent(
    retryConnection: () -> Unit
) {
    val context = LocalContext.current
    if (!Network.isConnected(context)) {
        NoInternet(
            error = R.string.no_internet_connection,
            isRetryEnabled = true,
            retry = retryConnection
        )
        Toast.makeText(
            context,
            stringResource(R.string.internet_not_connected),
            Toast.LENGTH_SHORT,
        ).show()
    } else {
        EmptyDataView(
            icon = R.drawable.ic_error_black_24dp,
            error = R.string.loan_account_details,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun LoanAccountDetailScreenPreview() {
    MifosMobileTheme {
        LoanAccountDetailScreen(
            uiState = LoanAccountDetailUiState.Success(LoanWithAssociations()),
            {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}
        )
    }
}