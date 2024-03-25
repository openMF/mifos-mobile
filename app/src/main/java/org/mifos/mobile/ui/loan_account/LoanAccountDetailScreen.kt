package org.mifos.mobile.ui.loan_account

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.utils.Network

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
    Column {
        LoanAccountDetailTopBar(
            navigateBack = navigateBack,
            viewGuarantor = viewGuarantor,
            updateLoan = updateLoan,
            withdrawLoan = withdrawLoan
        )

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

@Composable
fun ErrorComponent(
    retryConnection: () -> Unit
) {
    val context = LocalContext.current
    if (!Network.isConnected(context)) {
        NoInternet(
            icon = R.drawable.ic_portable_wifi_off_black_24dp,
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