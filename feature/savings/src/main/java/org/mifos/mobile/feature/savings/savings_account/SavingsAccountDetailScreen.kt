package org.mifos.mobile.feature.savings.savings_account

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.feature.savings.R

@Composable
fun SavingsAccountDetailScreen(
    uiState: SavingsAccountDetailUiState,
    navigateBack: () -> Unit,
    updateSavingsAccount: (SavingsWithAssociations?) -> Unit,
    withdrawSavingsAccount: (SavingsWithAssociations?) -> Unit,
    makeTransfer: (Boolean) -> Unit,
    viewTransaction: () -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (SavingsWithAssociations) -> Unit,
    callUs: () -> Unit,
    deposit: (Boolean) -> Unit,
    retryConnection: () -> Unit
) {
    Column {
        SavingsAccountDetailTopBar(
            navigateBack = navigateBack,
            updateSavingsAccount = {
                updateSavingsAccount.invoke(
                    (uiState as? SavingsAccountDetailUiState.Success)?.savingAccount
                )
            },
            withdrawSavingsAccount = {
                withdrawSavingsAccount.invoke(
                    (uiState as? SavingsAccountDetailUiState.Success)?.savingAccount
                )
            },
        )
        when (uiState) {
            is SavingsAccountDetailUiState.Error -> {
                ErrorComponent(retryConnection = retryConnection)
            }

            is SavingsAccountDetailUiState.Loading -> {
                MifosProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            is SavingsAccountDetailUiState.Success -> {
                if (uiState.savingAccount.status?.submittedAndPendingApproval == true) {
                    EmptyDataView(
                        modifier = Modifier.fillMaxSize(),
                        icon = R.drawable.ic_assignment_turned_in_black_24dp,
                        error = R.string.approval_pending
                    )
                } else {
                    SavingsAccountDetailContent(
                        savingsAccount = uiState.savingAccount,
                        makeTransfer = makeTransfer,
                        viewCharges = viewCharges,
                        viewTransaction = viewTransaction,
                        viewQrCode = viewQrCode,
                        callUs = callUs,
                        deposit = deposit
                    )
                }
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
            error = R.string.no_internet_connection,
            isRetryEnabled = true,
            retry = retryConnection
        )
        Toast.makeText(
            context, stringResource(R.string.internet_not_connected), Toast.LENGTH_SHORT,
        ).show()
    } else {
        EmptyDataView(
            error = R.string.error_saving_account_details_loading
        )
        Toast.makeText(
            context,
            stringResource(id = R.string.error_saving_account_details_loading),
            Toast.LENGTH_SHORT
        ).show()
    }
}


@Preview(showSystemUi = true)
@Composable
fun SavingsAccountDetailScreenPreview() {
    MifosMobileTheme {
        SavingsAccountDetailScreen(
            uiState = SavingsAccountDetailUiState.Loading,
            {}, {}, {}, {}, {}, {}, {}, {}, {}, {}
        )
    }
}