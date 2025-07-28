package org.mifos.mobile.feature.accounts.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_account_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.rememberMifosPullToRefreshState
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.TransactionScreenItem
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.accounts.model.FilterType
import org.mifos.mobile.feature.accounts.viewmodel.AccountTransactionAction
import org.mifos.mobile.feature.accounts.viewmodel.AccountTransactionState
import org.mifos.mobile.feature.accounts.viewmodel.AccountsAction
import org.mifos.mobile.feature.accounts.viewmodel.AccountsEvent
import org.mifos.mobile.feature.accounts.viewmodel.AccountsState
import org.mifos.mobile.feature.accounts.viewmodel.AccountsTransactionViewModel
import org.mifos.mobile.feature.accounts.viewmodel.AccountsViewModel
import org.mifos.mobile.feature.accounts.viewmodel.getTransactionCreditStatus
import org.mifos.mobile.feature.savingsaccount.savingsAccount.SavingsAccountScreen

@Composable
internal fun TransactionScreen(
    viewModel: AccountsTransactionViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            else -> {}
        }
    }

    TransactionScreenContent(
        state=state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    AccountTransactionsDialog(
        state=state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun TransactionScreenContent(
    state: AccountTransactionState,
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.isRefreshing
    val pullToRefreshState = rememberMifosPullToRefreshState(
        isEnabled = true,
        isRefreshing = isRefreshing,
        onRefresh = {
            onAction(AccountTransactionAction.Refresh)
        },
    )

    MifosElevatedScaffold(
        onNavigateBack = {  },
        topBarTitle = "Transaction History",
        pullToRefreshState = pullToRefreshState,
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        LazyColumn {
            items(state.data.size){ index->
                val transaction=state.data[index]
                TransactionScreenItem(
                    title = transaction.paymentDetailData?.paymentType?.name?:"",
                    date = DateHelper.getDateAsString(transaction.date),
                    time = "",
                    transactionAmount = CurrencyFormatter
                        .format(
                            balance = transaction.amount,
                            currencyCode = transaction.currency?.code?:"USD",
                            maximumFractionDigits = 3,
                        ),
                    isCredited = getTransactionCreditStatus(transaction.transactionType),
                )
            }
        }
    }
}


@Composable
internal fun AccountTransactionsDialog(
    state: AccountTransactionState,
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.dialogState) {
        is AccountTransactionState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.dialogState.message,
                ),
                onDismissRequest = { onAction(AccountTransactionAction.DismissDialog) },
            )
        }
        AccountTransactionState.DialogState.Filters -> {}
        AccountTransactionState.DialogState.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )
        null -> {}
    }
}
