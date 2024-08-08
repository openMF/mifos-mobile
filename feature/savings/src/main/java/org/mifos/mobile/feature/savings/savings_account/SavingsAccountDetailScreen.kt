package org.mifos.mobile.feature.savings.savings_account

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
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.feature.savings.R

@Composable
fun SavingsAccountDetailScreen(
    viewModel: SavingAccountsDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    updateSavingsAccount: (Long) -> Unit,
    withdrawSavingsAccount: (Long) -> Unit,
    makeTransfer: (Long) -> Unit,
    viewTransaction: (Long) -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (String) -> Unit,
    callUs: () -> Unit,
    deposit: (Long) -> Unit
) {
    val uiState by viewModel.savingAccountsDetailUiState.collectAsStateWithLifecycle()

    SavingsAccountDetailScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        updateSavingsAccount = { updateSavingsAccount(viewModel.savingsId.value ?: -1L) },
        withdrawSavingsAccount = { withdrawSavingsAccount(viewModel.savingsId.value ?: -1L) },
        makeTransfer = { makeTransfer(viewModel.savingsId.value ?: -1L) },
        viewTransaction = { viewTransaction(viewModel.savingsId.value ?: -1L) },
        viewCharges = viewCharges,
        viewQrCode = { viewQrCode(viewModel.getQrString(it)) },
        callUs = callUs,
        deposit = { deposit(viewModel.savingsId.value ?: -1L) },
    )
}


@Composable
fun SavingsAccountDetailScreen(
    uiState: SavingsAccountDetailUiState,
    navigateBack: () -> Unit,
    updateSavingsAccount: () -> Unit,
    withdrawSavingsAccount: () -> Unit,
    makeTransfer: () -> Unit,
    viewTransaction: () -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (SavingsWithAssociations) -> Unit,
    callUs: () -> Unit,
    deposit: () -> Unit,
) {
    MFScaffold(
        topBar = {
            SavingsAccountDetailTopBar(
                navigateBack = navigateBack,
                updateSavingsAccount = updateSavingsAccount,
                withdrawSavingsAccount = withdrawSavingsAccount
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (uiState) {
                is SavingsAccountDetailUiState.Error -> {
                    MifosErrorComponent()
                }

                is SavingsAccountDetailUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
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
}

@Preview(showSystemUi = true)
@Composable
fun SavingsAccountDetailScreenPreview() {
    MifosMobileTheme {
        SavingsAccountDetailScreen(
            uiState = SavingsAccountDetailUiState.Loading,
            {}, {}, {}, {}, {}, {}, {}, {}, {}
        )
    }
}