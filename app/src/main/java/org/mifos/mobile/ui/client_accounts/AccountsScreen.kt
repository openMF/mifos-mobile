package org.mifos.mobile.ui.client_accounts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.accounts.share.ShareAccount
import org.mifos.mobile.utils.AccountsUiState
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Network
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.utils.AccountTypeItemIndicator
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString

@Composable
fun AccountsScreen(
    accountType: String,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    val context = LocalContext.current
    val viewModel : AccountsViewModel = hiltViewModel()
    val uiState by viewModel.accountsUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val isFiltered by viewModel.isFiltered.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterList by viewModel.filterList.collectAsStateWithLifecycle()

    if(accountType == Constants.SAVINGS_ACCOUNTS)
    {
        LaunchedEffect(key1 = Unit) {
            viewModel.loadAccounts(accountType)
        }
        AccountsSavingsScreen(
            uiState = uiState,
            isSearching = isSearching,
            isFiltered = isFiltered,
            onRetry = { viewModel.loadAccounts(accountType) },
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh(accountType) },
            getUpdatedSearchList = { accountsList -> viewModel.searchInSavingsList(accountsList, searchQuery) },
            getUpdatedFilterList = { accountsList -> viewModel.getFilterSavingsAccountList(accountsList = accountsList, filterList = filterList, context = context) },
            onItemClick = { accType, accountId -> onItemClick.invoke(accType, accountId) },
        )
    }
    else if(accountType == Constants.LOAN_ACCOUNTS){
        LaunchedEffect(key1 = Unit) {
            viewModel.loadAccounts(accountType)
        }
        AccountsLoanScreen(
            uiState = uiState,
            isSearching = isSearching,
            isFiltered = isFiltered,
            onRetry = { viewModel.loadAccounts(accountType) },
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh(accountType) },
            getUpdatedSearchList = { accountsList -> viewModel.searchInLoanList(accountsList, searchQuery)!! },
            getUpdatedFilterList = { accountsList -> viewModel.getFilterLoanAccountList(accountsList =  accountsList, filterList= filterList, context = context) },
            onItemClick = { accType, accountId -> onItemClick.invoke( accType, accountId) },
        )
    }
    else if(accountType == Constants.SHARE_ACCOUNTS){
        LaunchedEffect(key1 = Unit) {
            viewModel.loadAccounts(accountType)
        }
        AccountsShareScreen(
            uiState = uiState,
            isSearching = isSearching,
            isFiltered = isFiltered,
            onRetry = { viewModel.loadAccounts(accountType) },
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh(accountType) },
            getUpdatedSearchList = { accountsList -> viewModel.searchInSharesList(accountsList, searchQuery)!! },
            getUpdatedFilterList = { accountsList -> viewModel.getFilterShareAccountList(accountsList = accountsList, filterList= filterList, context = context) }
        )
    }
}

@OptIn( ExperimentalMaterialApi::class)
@Composable
fun AccountsSavingsScreen(
    uiState: AccountsUiState,
    onRetry: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<SavingAccount?>) -> List<SavingAccount?>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<SavingAccount?>) -> List<SavingAccount?>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.pullRefresh(pullRefreshState))
        {
            when (uiState) {
                is AccountsUiState.Error -> {
                    MifosErrorComponent(
                        isNetworkConnected = Network.isConnected(context),
                        isRetryEnabled = true,
                        onRetry = onRetry
                    )
                }

                is AccountsUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is AccountsUiState.ShowSavingsAccounts -> {

                    if((uiState.savingAccounts!!.isEmpty())) {
                        EmptyDataView(
                            icon = R.drawable.ic_error_black_24dp,
                            error = R.string.empty_savings_accounts,
                            modifier = Modifier.fillMaxSize()
                        )
                    }else {
                        AccountScreenSavingsContent(
                            accountsList = uiState.savingAccounts,
                            isSearching = isSearching,
                            isFiltered= isFiltered,
                            getUpdatedSearchList = { accountsList -> getUpdatedSearchList(accountsList) },
                            getUpdatedFilterList = { accountsList -> getUpdatedFilterList(accountsList) },
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                }

                is AccountsUiState.ShowLoanAccounts -> Unit
                is AccountsUiState.ShowShareAccounts -> Unit
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn( ExperimentalMaterialApi::class)
@Composable
fun AccountsLoanScreen(
    uiState: AccountsUiState,
    onRetry: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<LoanAccount?>) -> List<LoanAccount?>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<LoanAccount?>) -> List<LoanAccount?>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.pullRefresh(pullRefreshState))
        {
            when (uiState) {
                is AccountsUiState.Error -> {
                    MifosErrorComponent(
                        isNetworkConnected = Network.isConnected(context),
                        isRetryEnabled = true,
                        onRetry = onRetry,
                    )
                }

                is AccountsUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is AccountsUiState.ShowLoanAccounts -> {

                    if(uiState.loanAccounts!!.isEmpty()) {
                        EmptyDataView(
                            icon = R.drawable.ic_error_black_24dp,
                            error = R.string.empty_loan_accounts,
                            modifier = Modifier.fillMaxSize()
                        )
                    }else {
                        AccountScreenLoanContent(
                            accountsList = uiState.loanAccounts,
                            isSearching = isSearching,
                            isFiltered = isFiltered,
                            getUpdatedSearchList = { accountsList -> getUpdatedSearchList(accountsList) },
                            getUpdatedFilterList = { accountsList -> getUpdatedFilterList(accountsList) },
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                }

                is AccountsUiState.ShowSavingsAccounts -> Unit
                is AccountsUiState.ShowShareAccounts -> Unit
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn( ExperimentalMaterialApi::class)
@Composable
fun AccountsShareScreen(
    uiState: AccountsUiState,
    onRetry: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<ShareAccount?>) -> List<ShareAccount?>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<ShareAccount?>) -> List<ShareAccount?>,
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Box(Modifier.pullRefresh(pullRefreshState))
        {
            when (uiState) {
                is AccountsUiState.Error -> {
                    MifosErrorComponent(
                        isNetworkConnected = Network.isConnected(context),
                        isRetryEnabled = true,
                        onRetry = onRetry
                    )
                }

                is AccountsUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is AccountsUiState.ShowShareAccounts -> {

                    if(uiState.shareAccounts!!.isEmpty()) {
                        EmptyDataView(
                            icon = R.drawable.ic_error_black_24dp,
                            error = R.string.empty_share_accounts,
                            modifier = Modifier.fillMaxSize()
                        )
                    }else {
                        AccountScreenShareContent(
                            accountsList = uiState.shareAccounts,
                            isSearching = isSearching,
                            isFiltered = isFiltered,
                            getUpdatedSearchList = { accountsList -> getUpdatedSearchList(accountsList) },
                            getUpdatedFilterList = { accountsList -> getUpdatedFilterList(accountsList) }
                        )
                    }
                }

                is AccountsUiState.ShowLoanAccounts -> Unit

                is AccountsUiState.ShowSavingsAccounts -> Unit
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun AccountScreenSavingsContent(
    accountsList: List<SavingAccount?>,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<SavingAccount?>) -> List<SavingAccount?>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<SavingAccount?>) -> List<SavingAccount?>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
) {

    var accounts by rememberSaveable {
        mutableStateOf(accountsList)
    }

    when {
        isFiltered && isSearching -> {
            accounts = getUpdatedSearchList(getUpdatedFilterList( accountsList))
        }
        isSearching -> {
            accounts = getUpdatedSearchList(accountsList)
        }
        isFiltered -> {
            accounts = getUpdatedFilterList(accountsList)
        }
        else -> {
            accounts = accountsList
        }
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {
        items(items = accounts) { savingAccount->

            if (savingAccount != null) {
                when {
                    savingAccount.status?.active == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.deposit_green),
                            stringResource = getDateAsString( savingAccount.lastActiveTransactionDate),
                            numColor = colorResource(R.color.deposit_green),
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }

                    savingAccount.status?.approved == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.light_green),
                            stringResource = "${stringResource(id = R.string.approved)} ${getDateAsString( savingAccount.timeLine?.approvedOnDate)}",
                            numColor = null,
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }

                    savingAccount.status?.submittedAndPendingApproval == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.light_yellow),
                            stringResource = "${stringResource(id = R.string.submitted)} ${getDateAsString( savingAccount.timeLine?.submittedOnDate)}",
                            numColor = null,
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }

                    savingAccount.status?.matured == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.red_light),
                            stringResource = getDateAsString( savingAccount.lastActiveTransactionDate),
                            numColor = colorResource(R.color.red_light),
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }

                    else -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.light_yellow),
                            stringResource = "${stringResource(id = R.string.closed)} ${getDateAsString( savingAccount?.timeLine?.closedOnDate)}",
                            numColor = null,
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AccountScreenLoanContent(
    accountsList: List<LoanAccount?>,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<LoanAccount?>) -> List<LoanAccount?>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<LoanAccount?>) -> List<LoanAccount?>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
) {

    var accounts by rememberSaveable {
        mutableStateOf(accountsList)
    }

    when {
        isFiltered && isSearching -> {
            accounts = getUpdatedSearchList(getUpdatedFilterList( accountsList))
        }
        isSearching -> {
            accounts = getUpdatedSearchList(accountsList)
        }
        isFiltered -> {
            accounts = getUpdatedFilterList(accountsList)
        }
        else -> {
            accounts = accountsList
        }
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {

        items(items = accounts) { loanAccount->

            if (loanAccount != null) {
                when {
                    loanAccount.status?.active == true && loanAccount.inArrears == true -> {
                        AccountScreenLoanListItem(
                            loanAccount = loanAccount,
                            color = colorResource(R.color.red),
                            stringResource = "${stringResource(id = R.string.disbursement)} ${getDateAsString( loanAccount.timeline?.actualDisbursementDate)}",
                            numColor = colorResource(R.color.red),
                            onItemClick= { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                    loanAccount.status?.active == true -> {
                        AccountScreenLoanListItem(
                            loanAccount = loanAccount,
                            color = colorResource(R.color.deposit_green),
                            stringResource = "${stringResource(id = R.string.disbursement)} ${getDateAsString( loanAccount.timeline?.actualDisbursementDate)}",
                            numColor = colorResource(R.color.deposit_green),
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                    loanAccount.status?.waitingForDisbursal == true -> {
                        AccountScreenLoanListItem(
                            loanAccount = loanAccount,
                            color = colorResource(id =R.color.blue),
                            stringResource = "${stringResource(id = R.string.approved)} ${getDateAsString( loanAccount.timeline?.approvedOnDate)}",
                            numColor = null,
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                    loanAccount.status?.pendingApproval == true -> {
                        AccountScreenLoanListItem(
                            loanAccount = loanAccount,
                            color = colorResource(R.color.light_yellow),
                            stringResource = "${stringResource(id = R.string.submitted)} ${getDateAsString( loanAccount.timeline?.submittedOnDate)}",
                            numColor = null,
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                    loanAccount.status?.overpaid == true -> {
                        AccountScreenLoanListItem(
                            loanAccount = loanAccount,
                            color = colorResource(R.color.purple),
                            stringResource = "${stringResource(id = R.string.approved)} ${getDateAsString( loanAccount.timeline?.actualDisbursementDate)}",
                            numColor = colorResource(R.color.purple),
                            onItemClick = onItemClick
                        )
                    }
                    loanAccount.status?.closed == true -> {
                        AccountScreenLoanListItem(
                            loanAccount = loanAccount,
                            color = colorResource(R.color.black),
                            stringResource = "${stringResource(id = R.string.closed)} ${getDateAsString( loanAccount.timeline?.closedOnDate)}",
                            numColor = null,
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                    else -> {
                        AccountScreenLoanListItem(
                            loanAccount = loanAccount,
                            color = colorResource(R.color.gray_dark),
                            stringResource = "${stringResource(id = R.string.withdrawn)} ${getDateAsString( loanAccount.timeline?.withdrawnOnDate)}",
                            numColor = null,
                            onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AccountScreenShareContent(
    accountsList: List<ShareAccount?>,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<ShareAccount?>) -> List<ShareAccount?>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<ShareAccount?>) -> List<ShareAccount?>
) {

    var accounts by rememberSaveable {
        mutableStateOf(accountsList)
    }

    when {
        isFiltered && isSearching -> {
            accounts = getUpdatedSearchList(getUpdatedFilterList( accountsList))
        }
        isSearching -> {
            accounts = getUpdatedSearchList(accountsList)
        }
        isFiltered -> {
            accounts = getUpdatedFilterList(accountsList)
        }
        else -> {
            accounts = accountsList
        }
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {
        items(items = accounts) { shareAccount->
            if (shareAccount != null) {
                when {
                    shareAccount.status?.active == true -> {
                        AccountScreenShareListItem(
                            shareAccount = shareAccount,
                            color = colorResource(R.color.deposit_green),
                            setSharingAccountDetail = true
                        )
                    }

                    shareAccount.status?.approved == true -> {
                        AccountScreenShareListItem(
                            shareAccount = shareAccount,
                            color = colorResource(R.color.light_green),
                            setSharingAccountDetail = false
                        )
                    }

                    shareAccount.status?.submittedAndPendingApproval == true -> {
                        AccountScreenShareListItem(
                            shareAccount = shareAccount,
                            color = colorResource(R.color.light_yellow),
                            setSharingAccountDetail = false
                        )
                    }

                    else -> {
                        AccountScreenShareListItem(
                            shareAccount = shareAccount,
                            color = colorResource(R.color.light_blue),
                            setSharingAccountDetail = false
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun AccountScreenLoanListItem(
    loanAccount: LoanAccount,
    color: Color,
    stringResource: String,
    numColor: Color?,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.clickable { onItemClick.invoke( Constants.LOAN_ACCOUNTS, loanAccount.id) },
        verticalAlignment = Alignment.CenterVertically)
    {
        AccountTypeItemIndicator(color)

        Column(modifier = Modifier.padding(all=12.dp)) {
            loanAccount.accountNo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            loanAccount.productName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorResource(id = R.color.gray_dark)
                )
            }
            Text(
                text = stringResource,
                style = MaterialTheme.typography.labelLarge,
                color = colorResource(id = R.color.gray_dark),
            )
        }

        Spacer(Modifier.weight(1f))

        numColor?.let {
            val amountBalance: Double =
                if (loanAccount.loanBalance != 0.0) loanAccount.loanBalance else 0.0
            Text(
                text = formatCurrency(context, amountBalance),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp),
                color = it

                )
        }
    }
}

@Composable
fun AccountScreenSavingsListItem(
    savingAccount: SavingAccount,
    color: Color,
    stringResource: String,
    numColor: Color?,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.clickable { onItemClick.invoke( Constants.SAVINGS_ACCOUNTS, savingAccount.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AccountTypeItemIndicator(color)

        Column(modifier = Modifier.padding(all=12.dp)) {
            savingAccount.accountNo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            savingAccount.productName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorResource(id = R.color.gray_dark),
                )
            }

            Text(
                text = stringResource,
                style = MaterialTheme.typography.labelLarge,
                color = colorResource(id = R.color.gray_dark),
            )
        }

        Spacer( Modifier.weight(1f))

        numColor?.let {
            val amountBalance = context.getString(
            R.string.string_and_string,
            savingAccount.currency?.displaySymbol ?: savingAccount.currency?.code,
            formatCurrency(context, savingAccount.accountBalance)
            )

            Text(
                text = amountBalance,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp),
                color = it

            )
        }
    }
}



@Composable
fun AccountScreenShareListItem(
    shareAccount: ShareAccount,
    setSharingAccountDetail: Boolean,
    color: Color
) {

    Row( verticalAlignment = Alignment.CenterVertically) {

        AccountTypeItemIndicator(color)

        Column(modifier = Modifier.padding(all=12.dp)) {
            shareAccount.accountNo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            shareAccount.productName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorResource(id = R.color.gray_dark),
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
            {
                Row() {
                    Text(
                        text = "pending",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(id = R.color.gray_dark),
                    )

                    Text(
                        text = " ${shareAccount.totalPendingForApprovalShares}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.black),
                    )
                }

                if(setSharingAccountDetail){
                    Row()
                    {
                        Text(
                            text = "approved",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorResource(id = R.color.gray_dark),
                        )

                        Text(
                            modifier = Modifier.padding(end=12.dp),
                            text = " ${shareAccount.totalApprovedShares}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.black),
                        )

                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

class AccountsScreenPreviewProvider : PreviewParameterProvider<AccountsUiState> {

    override val values: Sequence<AccountsUiState>
        get() = sequenceOf(
            AccountsUiState.Loading,
            AccountsUiState.Error,
            AccountsUiState.ShowLoanAccounts(List(10) {null}),
            AccountsUiState.ShowShareAccounts(List(10) {null}),
            AccountsUiState.ShowSavingsAccounts(List(10) {null})
        )
}

@Preview(showSystemUi = true)
@Composable
private fun AccountSavingsScreenPreview(
    @PreviewParameter( AccountsScreenPreviewProvider::class) accountUiState: AccountsUiState
) {
    MifosMobileTheme {

        AccountsSavingsScreen(
            uiState = accountUiState,
            isSearching = true,
            isFiltered = true,
            onRetry = {  },
            isRefreshing = true,
            onRefresh = {  },
            getUpdatedSearchList = { _-> listOf() },
            getUpdatedFilterList = { _-> listOf() },
            onItemClick = { _,_-> },
        )

    }
}