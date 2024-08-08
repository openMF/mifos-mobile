package org.mifos.mobile.feature.account.client_account.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.feature.account.R
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosIcons
import org.mifos.mobile.core.ui.component.MifosTabPager
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.account.account.screens.AccountsScreen
import org.mifos.mobile.core.model.entity.CheckboxStatus
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.account.client_account.utils.ClientAccountFilterDialog
import org.mifos.mobile.feature.account.client_account.utils.ClientAccountsScreenTopBar
import org.mifos.mobile.feature.account.viewmodel.AccountsViewModel

@Composable
fun ClientAccountsScreen(
    viewModel: AccountsViewModel = hiltViewModel(),
    navigateBack: () -> Unit?,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
    onItemClick: (accountType: AccountType, accountId: Long) -> Unit
) {
    val context = LocalContext.current
    var isDialogActive by rememberSaveable { mutableStateOf(false) }
    val accountType by viewModel.accountType.collectAsStateWithLifecycle()
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    val filterList by viewModel.filterList.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = accountType) {
        currentPage = accountType?.ordinal ?: 0
    }

    LaunchedEffect(key1 = currentPage) {
        viewModel.setFilterList(
            checkBoxList = emptyList(),
            currentPage = currentPage,
            context = context,
        )
    }

    ClientAccountsScreen(
        navigateBack = navigateBack,
        navigateToLoanApplicationScreen = navigateToLoanApplicationScreen,
        navigateToSavingsApplicationScreen = navigateToSavingsApplicationScreen,
        onItemClick = { accountType, accountId -> onItemClick.invoke(accountType, accountId) },
        cancelFilterDialog = { isDialogActive = false },
        clearFilter = {
            viewModel.setFilterList(
                checkBoxList = emptyList(),
                currentPage = currentPage,
                context = context
            )
            isDialogActive = false
        },
        filterAccounts = {
            viewModel.setFilterList(checkBoxList = it, currentPage = currentPage, context = context)
            isDialogActive = false
        },
        onSearchQueryChange = { viewModel.updateSearchQuery(query = it) },
        openSearch = { isDialogActive = true },
        closeSearch = { viewModel.stoppedSearching() },
        currentPage = currentPage,
        pageChanged = { index -> currentPage = index },
        isDialogActive = isDialogActive,
        filterList = filterList,
    )
}

@Composable
fun ClientAccountsScreen(
    navigateBack: () -> Unit?,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
    onItemClick: (accountType: AccountType, accountId: Long) -> Unit,
    cancelFilterDialog: () -> Unit,
    clearFilter: () -> Unit,
    filterAccounts: (checkBoxList: List<CheckboxStatus>) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    openSearch: () -> Unit,
    closeSearch: () -> Unit,
    currentPage: Int,
    pageChanged: (index: Int) -> Unit,
    isDialogActive: Boolean,
    filterList: List<CheckboxStatus>,
) {
    val tabs = listOf(
        stringResource(id = R.string.savings_account),
        stringResource(id = R.string.loan_account),
        stringResource(id = R.string.share_account)
    )

    if (isDialogActive) {
        ClientAccountFilterDialog(
            filterList = filterList,
            cancelDialog = { cancelFilterDialog.invoke() },
            clearFilter = { clearFilter.invoke() },
            updateFilterList = { list -> filterAccounts(list) },
            title = tabs[currentPage]
        )
    }

    MFScaffold(
        topBar = {
            ClientAccountsScreenTopBar(
                navigateBack = navigateBack,
                onChange = { onSearchQueryChange(it) },
                clickDialog = { openSearch.invoke() },
                closeSearch = { closeSearch.invoke() }
            )
        },

        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = {
                when (currentPage) {
                    0 -> navigateToSavingsApplicationScreen()
                    1 -> navigateToLoanApplicationScreen()
                }
            },
            contentColor = MaterialTheme.colorScheme.primary,
            content = {
                Icon(
                    imageVector = MifosIcons.Add,
                    contentDescription = "Create Account",
                    tint = if (isSystemInDarkTheme()) Color.Black else Color.White
                )
            }
        ),

        scaffoldContent = {
            ClientAccountsTabRow(
                modifier = Modifier.padding(it),
                pageChanged = { index -> pageChanged.invoke(index) },
                onItemClick = { accountType, accountId ->
                    onItemClick.invoke(
                        accountType,
                        accountId
                    )
                },
                currentPage = currentPage
            )
        }
    )
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientAccountsTabRow(
    modifier: Modifier,
    pageChanged: (index: Int) -> Unit,
    onItemClick: (accountType: AccountType, accountId: Long) -> Unit,
    currentPage: Int
) {
    var currentPage by remember { mutableIntStateOf(currentPage) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val tabs = listOf(
        stringResource(id = R.string.savings),
        stringResource(id = R.string.loan),
        stringResource(id = R.string.share)
    )

    LaunchedEffect(key1 = currentPage) {
        pageChanged(currentPage)
        pagerState.animateScrollToPage(currentPage)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        currentPage = if (!pagerState.isScrollInProgress)
            pagerState.currentPage
        else {
            pagerState.targetPage
        }
    }

    MifosTabPager(
        pagerState = pagerState,
        currentPage = currentPage,
        modifier = modifier,
        tabs = tabs,
        setCurrentPage = { currentPage = it }
    ) {
        when (currentPage) {
            0 -> AccountsScreen(
                accountType = Constants.SAVINGS_ACCOUNTS,
                onItemClick = { accType, accountId ->
                    onItemClick.invoke(
                        AccountType.SAVINGS,
                        accountId
                    )
                }
            )

            1 -> AccountsScreen(
                accountType = Constants.LOAN_ACCOUNTS,
                onItemClick = { accType, accountId ->
                    onItemClick.invoke(
                        AccountType.LOAN,
                        accountId
                    )
                }
            )

            2 -> AccountsScreen(
                accountType = Constants.SHARE_ACCOUNTS,
                onItemClick = { accType, accountId ->
                    onItemClick.invoke(
                        AccountType.SHARE,
                        accountId
                    )
                }
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ClientAccountsScreenPreview() {
    MifosMobileTheme {
        ClientAccountsScreen(
            navigateBack = {},
            onItemClick = { accountType, accountId -> },
            cancelFilterDialog = { },
            clearFilter = { },
            filterAccounts = { },
            onSearchQueryChange = { },
            openSearch = { },
            closeSearch = { },
            currentPage = 0,
            pageChanged = { index -> },
            isDialogActive = false,
            filterList = listOf(),
            navigateToLoanApplicationScreen = {},
            navigateToSavingsApplicationScreen = {}
        )
    }
}