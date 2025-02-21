/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_account_loan_account
import mifos_mobile.feature.accounts.generated.resources.feature_account_savings_account
import mifos_mobile.feature.accounts.generated.resources.feature_account_share_account
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTabPager
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.feature.accounts.component.AccountFilterDialog
import org.mifos.mobile.feature.accounts.component.AccountsScreenTopBar
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.viewmodel.AccountsViewModel

@Composable
fun AccountsScreen(
    navigateBack: () -> Unit,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
    onAccountClicked: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = koinViewModel(),
) {
    var isDialogActive by rememberSaveable { mutableStateOf(false) }
    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val isFiltered by viewModel.isFiltered.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val accountType by viewModel.accountType.collectAsStateWithLifecycle()
    val checkboxOptions by viewModel.checkboxOptions.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = accountType) {
        currentPage = accountType?.ordinal ?: 0
    }

    LaunchedEffect(key1 = currentPage) {
        viewModel.setCheckboxFilterList(
            checkBoxList = emptyList(),
            currentPage = currentPage,
        )
    }

    AccountsScreenContent(
        currentPage = currentPage,
        searchQuery = searchQuery,
        isSearching = isSearching,
        isFiltered = isFiltered,
        isDialogActive = isDialogActive,
        checkboxOptions = checkboxOptions,
        openFilterDialog = { isDialogActive = true },
        closeSearch = viewModel::stoppedSearching,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onPageChange = { index -> currentPage = index },
        clearFilter = {
            viewModel.setCheckboxFilterList(
                checkBoxList = emptyList(),
                currentPage = currentPage,
            )
            isDialogActive = false
        },
        cancelFilterDialog = { isDialogActive = false },
        updateCheckboxList = {
            viewModel.setCheckboxFilterList(checkBoxList = it, currentPage = currentPage)
            isDialogActive = false
        },
        modifier = modifier,
        navigateBack = navigateBack,
        navigateToLoanApplicationScreen = navigateToLoanApplicationScreen,
        navigateToSavingsApplicationScreen = navigateToSavingsApplicationScreen,
        onAccountClicked = onAccountClicked,
    )
}

@Composable
private fun AccountsScreenContent(
    currentPage: Int,
    searchQuery: String,
    isSearching: Boolean,
    isFiltered: Boolean,
    isDialogActive: Boolean,
    checkboxOptions: List<CheckboxStatus>,
    openFilterDialog: () -> Unit,
    closeSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPageChange: (index: Int) -> Unit,
    clearFilter: () -> Unit,
    cancelFilterDialog: () -> Unit,
    updateCheckboxList: (checkBoxList: List<CheckboxStatus>) -> Unit,
    navigateBack: () -> Unit,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
    onAccountClicked: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        stringResource(resource = Res.string.feature_account_savings_account),
        stringResource(resource = Res.string.feature_account_loan_account),
        stringResource(resource = Res.string.feature_account_share_account),
    )

    if (isDialogActive) {
        AccountFilterDialog(
            title = tabs[currentPage],
            checkboxOptions = checkboxOptions,
            cancelDialog = cancelFilterDialog,
            clearFilter = clearFilter,
            updateCheckboxList = updateCheckboxList,
        )
    }

    MifosScaffold(
        topBar = {
            AccountsScreenTopBar(
                navigateBack = navigateBack,
                onChange = onSearchQueryChange,
                openFilterDialog = openFilterDialog,
                closeSearch = closeSearch,
            )
        },
        floatingActionButton = {
            IconButton(
                onClick = {
                    when (currentPage) {
                        0 -> navigateToSavingsApplicationScreen()
                        1 -> navigateToLoanApplicationScreen()
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            ) {
                Icon(
                    imageVector = MifosIcons.Add,
                    contentDescription = "Create Account",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        ClientAccountsTabRow(
            tabs = tabs,
            checkboxOptions = checkboxOptions,
            currentPage = currentPage,
            searchQuery = searchQuery,
            isSearching = isSearching,
            isFiltered = isFiltered,
            onPageChange = onPageChange,
            onAccountClicked = onAccountClicked,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun ClientAccountsTabRow(
    tabs: List<String>,
    checkboxOptions: List<CheckboxStatus>,
    currentPage: Int,
    searchQuery: String,
    isSearching: Boolean,
    isFiltered: Boolean,
    onPageChange: (index: Int) -> Unit,
    onAccountClicked: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var page by remember { mutableIntStateOf(currentPage) }
    val pagerState = rememberPagerState(
        initialPage = page,
        initialPageOffsetFraction = 0f,
        pageCount = { 3 },
    )

    val selectedCheckboxLabels = remember(checkboxOptions) {
        checkboxOptions
            .filter { it.isChecked }
            .map { it.statusLabel }
    }

    LaunchedEffect(key1 = page) {
        onPageChange(page)
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
            0 -> SavingsAccountScreen(
                checkboxOptionsLabels = selectedCheckboxLabels,
                searchQuery = searchQuery,
                isSearchActive = isSearching,
                isFiltered = isFiltered,
                onAccountSelected = onAccountClicked,
            )

            1 -> LoanAccountScreen(
                checkboxOptionsLabels = selectedCheckboxLabels,
                searchQuery = searchQuery,
                isSearchActive = isSearching,
                isFiltered = isFiltered,
                onAccountSelected = onAccountClicked,
            )

            2 -> ShareAccountScreen(
                checkboxOptionsLabels = selectedCheckboxLabels,
                searchQuery = searchQuery,
                isSearchActive = isSearching,
                isFiltered = isFiltered,
                onAccountSelected = onAccountClicked,
            )
        }
    }
}

@DevicePreview
@Composable
internal fun ClientAccountsScreenPreview() {
    MifosMobileTheme {
        AccountsScreenContent(
            currentPage = 0,
            searchQuery = "",
            isSearching = false,
            isFiltered = false,
            isDialogActive = false,
            checkboxOptions = listOf(),
            openFilterDialog = { },
            closeSearch = { },
            onSearchQueryChange = { },
            onPageChange = { },
            clearFilter = { },
            cancelFilterDialog = { },
            updateCheckboxList = { },
            navigateBack = {},
            navigateToLoanApplicationScreen = {},
            navigateToSavingsApplicationScreen = {},
            onAccountClicked = { _, _ -> },
        )
    }
}
