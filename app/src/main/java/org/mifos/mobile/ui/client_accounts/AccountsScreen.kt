package org.mifos.mobile.ui.client_accounts

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import org.mifos.mobile.models.CheckboxStatus
import org.mifos.mobile.utils.AccountsFilterUtil
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString

@Composable
fun AccountsScreen(
    navigateBack: () -> Unit?,
    accountType: String,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {

    val viewModel : AccountsViewModel = hiltViewModel()
    val uiState by viewModel.accountsUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val isFiltered by viewModel.isFiltered.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterList by viewModel.filterList.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if(accountType == Constants.SAVINGS_ACCOUNTS)
    {
        LaunchedEffect(key1 = Unit) {
            viewModel.loadAccounts(accountType)
        }
        AccountsSavingsScreen(
            uiState = uiState,
            isSearching = isSearching,
            isFiltered = isFiltered,
            navigateBack = { navigateBack },
            onRetry = { viewModel.loadAccounts(accountType) },
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh(accountType) },
            getUpdatedSearchList = { accountsList -> viewModel.searchInSavingsList(accountsList, searchQuery) },
            getUpdatedFilterList = { accountsList -> getFilterSavingsAccountList(accountsList = accountsList, filterList, viewModel, context) },
            onItemClick = onItemClick
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
            navigateBack = { navigateBack },
            onRetry = { viewModel.loadAccounts(accountType) },
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh(accountType) },
            getUpdatedSearchList = { accountsList -> viewModel.searchInLoanList(accountsList, searchQuery)!! },
            getUpdatedFilterList = { accountsList -> getFilterLoanAccountList(accountsList =  accountsList, filterList, viewModel, context ) },
            onItemClick = onItemClick
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
            navigateBack = { navigateBack },
            onRetry = { viewModel.loadAccounts(accountType) },
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh(accountType) },
            getUpdatedSearchList = { accountsList -> viewModel.searchInSharesList(accountsList, searchQuery)!! },
            getUpdatedFilterList = { accountsList -> getFilterShareAccountList(accountsList = accountsList, filterList, viewModel, context) },
            onItemClick = onItemClick
        )
    }
}

fun getFilterLoanAccountList(
    accountsList: List<LoanAccount?>,
    filterList: List<CheckboxStatus>,
    viewModel: AccountsViewModel,
    context: Context
): List<LoanAccount?> {
    val newList : MutableList<LoanAccount?> = mutableListOf()
    for( filter in filterList)
    {
        if(filter.isChecked)
            newList.addAll( viewModel.getFilteredLoanAccount(accountsList,filter, getFilterStrings(context))!! )
    }
    return newList
}

fun getFilterSavingsAccountList(
    accountsList: List<SavingAccount?>,
    filterList: List<CheckboxStatus>,
    viewModel: AccountsViewModel,
    context: Context
): List<SavingAccount?> {

    val newList : MutableList<SavingAccount?> = mutableListOf()
    for( filter in filterList)
    {
        if( filter.isChecked )
            newList.addAll( viewModel.getFilteredSavingsAccount(accountsList,filter, getFilterStrings(context))!! )
    }
    return newList
}

fun getFilterShareAccountList(
    accountsList: List<ShareAccount?>,
    filterList: List<CheckboxStatus>,
    viewModel: AccountsViewModel,
    context: Context
): List<ShareAccount?> {
    val newList : MutableList<ShareAccount?> = mutableListOf()
    for( filter in filterList)
    {
        if(filter.isChecked)
            newList.addAll( viewModel.getFilteredShareAccount(accountsList,filter, getFilterStrings(context))!! )
    }
    return newList
}

private fun getFilterStrings( context : Context?): AccountsFilterUtil {
    return AccountsFilterUtil().apply {
        this.activeString = context?.getString(R.string.active)
        this.approvedString = context?.getString(R.string.approved)
        this.approvalPendingString = context?.getString(R.string.approval_pending)
        this.maturedString = context?.getString(R.string.matured)
        this.waitingForDisburseString = context?.getString(R.string.waiting_for_disburse)
        this.overpaidString = context?.getString(R.string.overpaid)
        this.closedString = context?.getString(R.string.closed)
        this.withdrawnString = context?.getString(R.string.withdrawn)
        this.inArrearsString = context?.getString(R.string.in_arrears)
    }
}

@OptIn( ExperimentalMaterialApi::class)
@Composable
fun AccountsSavingsScreen(
    uiState: AccountsUiState,
    navigateBack: () -> Unit,
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

                is AccountsUiState.ShowLoanAccounts -> {}

                is AccountsUiState.ShowSavingsAccounts -> {

                    if( ( uiState.savingAccounts!!.isEmpty()) )
                    {
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
                            onItemClick = onItemClick
                        )
                    }
                }
                is AccountsUiState.ShowShareAccounts -> {}
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
    navigateBack: () -> Unit,
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
                        onRetry = onRetry,
                    )
                }

                is AccountsUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is AccountsUiState.ShowLoanAccounts -> {

                    if( uiState.loanAccounts!!.isEmpty() )
                    {
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
                            onItemClick = onItemClick
                        )
                    }
                }

                is AccountsUiState.ShowSavingsAccounts -> {}
                is AccountsUiState.ShowShareAccounts -> {}
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
    navigateBack: () -> Unit,
    onRetry: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<ShareAccount?>) -> List<ShareAccount?>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<ShareAccount?>) -> List<ShareAccount?>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
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

                is AccountsUiState.ShowLoanAccounts -> {}

                is AccountsUiState.ShowSavingsAccounts -> {}

                is AccountsUiState.ShowShareAccounts -> {

                    if( uiState.shareAccounts!!.isEmpty() )
                    {
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

    if( isFiltered && isSearching)
    {
        accounts = getUpdatedSearchList(getUpdatedFilterList( accountsList))
    }else if( isSearching ){
        accounts = getUpdatedSearchList(accountsList)
    }else if( isFiltered ){
        accounts = getUpdatedFilterList(accountsList)
    }else {
        accounts = accountsList
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {
        items(items = accounts) { savingAccount->

            if (savingAccount != null) {
                when {
                    savingAccount?.status?.active == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.deposit_green),
                            stringResource = getDateAsString( savingAccount.lastActiveTransactionDate),
                            numcolor = colorResource(R.color.deposit_green),
                            onItemClick = onItemClick
                        )
                    }

                    savingAccount?.status?.approved == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.light_green),
                            stringResource = "${stringResource(id = R.string.approved)} ${getDateAsString( savingAccount.timeLine?.approvedOnDate)}",
                            numcolor = null,
                            onItemClick = onItemClick
                        )
                    }

                    savingAccount?.status?.submittedAndPendingApproval == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.light_yellow),
                            stringResource = "${stringResource(id = R.string.submitted)} ${getDateAsString( savingAccount.timeLine?.submittedOnDate)}",
                            numcolor = null,
                            onItemClick = onItemClick
                        )
                    }

                    savingAccount?.status?.matured == true -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.red_light),
                            stringResource = getDateAsString( savingAccount.lastActiveTransactionDate),
                            numcolor = colorResource(R.color.red_light),
                            onItemClick = onItemClick
                        )
                    }

                    else -> {
                        AccountScreenSavingsListItem(
                            savingAccount = savingAccount,
                            color = colorResource(R.color.light_yellow),
                            stringResource = "${stringResource(id = R.string.closed)} ${getDateAsString( savingAccount?.timeLine?.closedOnDate)}",
                            numcolor = null,
                            onItemClick = onItemClick
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

    if( isFiltered && isSearching)
    {
        accounts = getUpdatedSearchList(getUpdatedFilterList( accountsList))
    }else if( isSearching ){
        accounts = getUpdatedSearchList(accountsList)
    }else if( isFiltered ){
        accounts = getUpdatedFilterList(accountsList)
    }else {
        accounts = accountsList
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {

        items(items = accounts) { loanAccount->

            if (loanAccount != null) {
                if (loanAccount?.status?.active == true && loanAccount.inArrears == true) {
                    AccountScreenLoanListItem(
                        loanAccount = loanAccount,
                        color = colorResource(R.color.red),
                        stringResource = "${stringResource(id = R.string.disbursement)} ${getDateAsString( loanAccount.timeline?.actualDisbursementDate)}",
                        numcolor = colorResource(R.color.red),
                        onItemClick= onItemClick
                    )
                } else if (loanAccount?.status?.active == true) {
                    AccountScreenLoanListItem(
                        loanAccount = loanAccount,
                        color = colorResource(R.color.deposit_green),
                        stringResource = "${stringResource(id = R.string.disbursement)} ${getDateAsString( loanAccount.timeline?.actualDisbursementDate)}",
                        numcolor = colorResource(R.color.deposit_green),
                        onItemClick = onItemClick
                    )
                } else if (loanAccount?.status?.waitingForDisbursal == true) {
                    AccountScreenLoanListItem(
                        loanAccount = loanAccount,
                        color = colorResource(id =R.color.blue),
                        stringResource = "${stringResource(id = R.string.approved)} ${getDateAsString( loanAccount.timeline?.approvedOnDate)}",
                        numcolor = null,
                        onItemClick = onItemClick
                    )
                } else if (loanAccount?.status?.pendingApproval == true) {
                    AccountScreenLoanListItem(
                        loanAccount = loanAccount,
                        color = colorResource(R.color.light_yellow),
                        stringResource = "${stringResource(id = R.string.submitted)} ${getDateAsString( loanAccount.timeline?.submittedOnDate)}",
                        numcolor = null,
                        onItemClick = onItemClick
                    )
                } else if (loanAccount?.status?.overpaid == true) {
                    AccountScreenLoanListItem(
                        loanAccount = loanAccount,
                        color = colorResource(R.color.purple),
                        stringResource = "${stringResource(id = R.string.approved)} ${getDateAsString( loanAccount.timeline?.actualDisbursementDate)}",
                        numcolor = colorResource(R.color.purple),
                        onItemClick = onItemClick
                    )
                } else if (loanAccount?.status?.closed == true) {
                    AccountScreenLoanListItem(
                        loanAccount = loanAccount,
                        color = colorResource(R.color.black),
                        stringResource = "${stringResource(id = R.string.closed)} ${getDateAsString( loanAccount.timeline?.closedOnDate)}",
                        numcolor = null,
                        onItemClick = onItemClick
                    )
                } else {
                    AccountScreenLoanListItem(
                        loanAccount = loanAccount,
                        color = colorResource(R.color.gray_dark),
                        stringResource = "${stringResource(id = R.string.withdrawn)} ${getDateAsString( loanAccount.timeline?.withdrawnOnDate)}",
                        numcolor = null,
                        onItemClick = onItemClick
                    )
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

    if( isFiltered && isSearching)
    {
        accounts = getUpdatedSearchList(getUpdatedFilterList( accountsList))
    }else if( isSearching ){
        accounts = getUpdatedSearchList(accountsList)
    }else if( isFiltered ){
        accounts = getUpdatedFilterList(accountsList)
    }else {
        accounts = accountsList
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {
        items(items = accounts) { shareAccount->
            if (shareAccount != null) {
                when {
                    shareAccount?.status?.active == true -> {
                        AccountScreenShareListItem(
                            shareAccount = shareAccount,
                            color = colorResource(R.color.deposit_green),
                            setSharingAccountDetail = true
                        )
                    }

                    shareAccount?.status?.approved == true -> {
                        AccountScreenShareListItem(
                            shareAccount = shareAccount,
                            color = colorResource(R.color.light_green),
                            setSharingAccountDetail = false
                        )
                    }

                    shareAccount?.status?.submittedAndPendingApproval == true -> {
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
    numcolor: Color?,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.clickable { onItemClick.invoke( Constants.LOAN_ACCOUNTS, loanAccount.id) },
        verticalAlignment = Alignment.CenterVertically)
    {
        CustomShapeBox(color)

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

        numcolor?.let {
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
    numcolor: Color?,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    val context = LocalContext.current
    Row( modifier = Modifier.clickable { onItemClick.invoke( Constants.SAVINGS_ACCOUNTS, savingAccount.id) },
        verticalAlignment = Alignment.CenterVertically) {

        CustomShapeBox(color)

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

        numcolor?.let {
            val amountBalance = context.getString(
            R.string.string_and_string,
            savingAccount.currency?.displaySymbol ?: savingAccount.currency?.code,
            formatCurrency(context, savingAccount.accountBalance))

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

        CustomShapeBox(color)

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

        Spacer( Modifier.weight(1f))


    }
}


@Composable
fun CustomShapeBox( color: Color) {
    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier
            .height(60.dp)
            .width(5.dp)) {
            val radius = 10.dp.toPx()
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width - radius, 0f)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        offset = Offset(size.width - radius, 0f),
                        size = Size(radius, radius)
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(size.width, size.height - radius)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        offset = Offset(size.width - radius, size.height - radius),
                        size = Size(radius, radius)
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = path,
                color = color
            )
        }
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
private fun AccountScreenPreview(
    @PreviewParameter( AccountsScreenPreviewProvider::class) accountUiState: AccountsUiState
) {
    MifosMobileTheme {
        AccountsScreen(
            navigateBack = {},
            accountType = "",
            onItemClick = { _, _ -> }
        )
    }
}
