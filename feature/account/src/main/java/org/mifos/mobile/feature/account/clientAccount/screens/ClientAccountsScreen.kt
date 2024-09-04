/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.clientAccount.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.rememberPagerState
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.components.FloatingActionButtonContent
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTabPager
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.CheckboxStatus
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.account.R
import org.mifos.mobile.feature.account.account.screens.AccountsScreen
import org.mifos.mobile.feature.account.clientAccount.utils.ClientAccountFilterDialog
import org.mifos.mobile.feature.account.clientAccount.utils.ClientAccountsScreenTopBar
import org.mifos.mobile.feature.account.viewmodel.AccountsViewModel

@Composable
internal fun ClientAccountsScreen(
    navigateBack: () -> Unit,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
    onItemClick: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    var isDialogActive by rememberSaveable { mutableStateOf(false) }
    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    val accountType by viewModel.accountType.collectAsStateWithLifecycle()
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
        currentPage = currentPage,
        isDialogActive = isDialogActive,
        filterList = filterList,
        openSearch = { isDialogActive = true },
        closeSearch = viewModel::stoppedSearching,
        onSearchQueryChange = viewModel::updateSearchQuery,
        pageChanged = { index -> currentPage = index },
        clearFilter = {
            viewModel.setFilterList(
                checkBoxList = emptyList(),
                currentPage = currentPage,
                context = context,
            )
            isDialogActive = false
        },
        cancelFilterDialog = { isDialogActive = false },
        filterAccounts = {
            viewModel.setFilterList(checkBoxList = it, currentPage = currentPage, context = context)
            isDialogActive = false
        },
        modifier = modifier,
        navigateBack = navigateBack,
        navigateToLoanApplicationScreen = navigateToLoanApplicationScreen,
        navigateToSavingsApplicationScreen = navigateToSavingsApplicationScreen,
        onItemClick = onItemClick,
    )
}

@Composable
private fun ClientAccountsScreen(
    currentPage: Int,
    isDialogActive: Boolean,
    filterList: List<CheckboxStatus>,
    openSearch: () -> Unit,
    closeSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    pageChanged: (index: Int) -> Unit,
    clearFilter: () -> Unit,
    cancelFilterDialog: () -> Unit,
    filterAccounts: (checkBoxList: List<CheckboxStatus>) -> Unit,
    navigateBack: () -> Unit,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
    onItemClick: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        stringResource(id = R.string.feature_account_savings_account),
        stringResource(id = R.string.feature_account_loan_account),
        stringResource(id = R.string.feature_account_share_account),
    )

    if (isDialogActive) {
        ClientAccountFilterDialog(
            title = tabs[currentPage],
            filterList = filterList,
            cancelDialog = { cancelFilterDialog.invoke() },
            clearFilter = { clearFilter.invoke() },
            updateFilterList = { list -> filterAccounts(list) },
        )
    }

    MifosScaffold(
        topBar = {
            ClientAccountsScreenTopBar(
                navigateBack = navigateBack,
                onChange = onSearchQueryChange,
                clickDialog = openSearch,
                closeSearch = closeSearch,
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
                    tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                )
            },
        ),
        content = {
            ClientAccountsTabRow(
                modifier = Modifier.padding(it),
                pageChanged = pageChanged,
                onItemClick = onItemClick,
                currentPage = currentPage,
            )
        },
        modifier = modifier,
    )
}

@Composable
@Suppress("DEPRECATION")
private fun ClientAccountsTabRow(
    currentPage: Int,
    pageChanged: (index: Int) -> Unit,
    onItemClick: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var page by remember { mutableIntStateOf(currentPage) }
    val pagerState = rememberPagerState()

    val tabs = listOf(
        stringResource(id = R.string.feature_account_savings),
        stringResource(id = R.string.feature_account_loan),
        stringResource(id = R.string.feature_account_share),
    )

    LaunchedEffect(key1 = page) {
        pageChanged(page)
        pagerState.animateScrollToPage(page)
    }

    LaunchedEffect(
        key1 = pagerState.currentPage,
        key2 = pagerState.isScrollInProgress,
    ) {
        page = if (!pagerState.isScrollInProgress) {
            pagerState.currentPage
        } else {
            pagerState.targetPage
        }
    }

    MifosTabPager(
        pagerState = pagerState,
        currentPage = page,
        modifier = modifier,
        tabs = tabs,
        setCurrentPage = { page = it },
    ) {
        when (page) {
            0 -> AccountsScreen(
                accountType = Constants.SAVINGS_ACCOUNTS,
                onItemClick = { _, accountId ->
                    onItemClick.invoke(
                        AccountType.SAVINGS,
                        accountId,
                    )
                },
            )

            1 -> AccountsScreen(
                accountType = Constants.LOAN_ACCOUNTS,
                onItemClick = { _, accountId ->
                    onItemClick.invoke(
                        AccountType.LOAN,
                        accountId,
                    )
                },
            )

            2 -> AccountsScreen(
                accountType = Constants.SHARE_ACCOUNTS,
                onItemClick = { _, accountId ->
                    onItemClick.invoke(
                        AccountType.SHARE,
                        accountId,
                    )
                },
            )
        }
    }
}

@DevicePreviews
@Composable
private fun ClientAccountsScreenPreview() {
    MifosMobileTheme {
        ClientAccountsScreen(
            currentPage = 0,
            isDialogActive = false,
            filterList = listOf(),
            openSearch = { },
            closeSearch = { },
            onSearchQueryChange = { },
            pageChanged = { },
            clearFilter = { },
            cancelFilterDialog = { },
            filterAccounts = { },
            navigateBack = {},
            navigateToLoanApplicationScreen = {},
            navigateToSavingsApplicationScreen = {},
            onItemClick = { _, _ -> },
        )
    }
}
