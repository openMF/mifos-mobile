/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_account_title
import mifos_mobile.feature.accounts.generated.resources.feature_loan_account_title
import mifos_mobile.feature.accounts.generated.resources.feature_saving_account_title
import mifos_mobile.feature.accounts.generated.resources.feature_share_account_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.rememberMifosPullToRefreshState
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.accounts.component.FilterSection
import org.mifos.mobile.feature.accounts.component.FilterTopSection
import org.mifos.mobile.feature.accounts.model.FilterType
import org.mifos.mobile.feature.loanaccount.loanAccount.LoanAccountScreen
import org.mifos.mobile.feature.savingsaccount.savingsAccount.SavingsAccountScreen
import org.mifos.mobile.feature.shareaccount.shareAccount.ShareAccountScreen

@Composable
internal fun AccountsScreen(
    navigateBack: () -> Unit,
    onAccountClicked: (accountType: String, accountId: Long) -> Unit,
    viewModel: AccountsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is AccountsEvent.AccountClicked -> {
                onAccountClicked(event.accountType, event.accountId)
            }
            AccountsEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    AccountScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    AccountsDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun AccountsDialog(
    state: AccountsState,
    onAction: (AccountsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.dialogState) {
        is AccountsState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.dialogState.message,
                ),
                onDismissRequest = { onAction(AccountsAction.DismissDialog) },
            )
        }
        AccountsState.DialogState.Filters -> SavingsAccountFilters(
            state = state,
            onAction = onAction,
            modifier = modifier,
        )
        AccountsState.DialogState.Loading -> MifosProgressIndicator()
        null -> {}
    }
}

@Composable
internal fun SavingsAccountFilters(
    state: AccountsState,
    onAction: (AccountsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isTypeExpanded by rememberSaveable { mutableStateOf(true) }
    var isStatusExpanded by rememberSaveable { mutableStateOf(true) }

    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_account_title),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.large),
        ) {
            FilterTopSection(
                isAnyFilterSelected = state.isAnyFilterSelected,
                resetFilters = {
                    onAction(AccountsAction.ResetFilters)
                },
                onApplyFilter = {
                    onAction(AccountsAction.GetFilterResults)
                },
                dismissDialog = {
                    onAction(AccountsAction.DismissDialog)
                },
            )

            Spacer(Modifier.height(DesignToken.spacing.largeIncreased))

            HorizontalDivider(modifier = Modifier.height(1.dp))

            FilterSection(
                title = "Type",
                filtersSelected = state.accountTypeFiltersCount ?: 0,
                isExpanded = isTypeExpanded,
                onToggle = { isTypeExpanded = !isTypeExpanded },
                filters = state.checkboxOptions.filter { it.type == FilterType.ACCOUNT_TYPE },
                onCheckChanged = { label ->
                    onAction(AccountsAction.ToggleCheckbox(label, FilterType.ACCOUNT_TYPE))
                },
            )

            FilterSection(
                title = "Status",
                filtersSelected = state.accountStatusFiltersCount ?: 0,
                isExpanded = isStatusExpanded,
                onToggle = { isStatusExpanded = !isStatusExpanded },
                filters = state.checkboxOptions.filter { it.type == FilterType.ACCOUNT_STATUS },
                onCheckChanged = { label ->
                    onAction(AccountsAction.ToggleCheckbox(label, FilterType.ACCOUNT_STATUS))
                },
            )
        }
    }
}

@Composable
internal fun AccountScreenContent(
    state: AccountsState,
    onAction: (AccountsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.isRefreshing
    val pullToRefreshState = rememberMifosPullToRefreshState(
        isEnabled = true,
        isRefreshing = isRefreshing,
        onRefresh = {
            onAction(AccountsAction.Refresh)
        },
    )

    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountsAction.OnNavigateBack) },
        topBarTitle = when (state.accountType) {
            AccountType.SAVINGS -> stringResource(Res.string.feature_saving_account_title)
            AccountType.LOAN -> stringResource(Res.string.feature_loan_account_title)
            AccountType.SHARE -> stringResource(Res.string.feature_share_account_title)
        },
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
        val typeFilters = state.selectedFilters.filter { it.type == FilterType.ACCOUNT_TYPE }
        val statusFilters = state.selectedFilters.filter { it.type == FilterType.ACCOUNT_STATUS }
        when (state.accountType) {
            AccountType.SAVINGS -> {
                SavingsAccountScreen(
                    navigateBack = { onAction(AccountsAction.OnNavigateBack) },
                    refreshSignal = state.refreshSignal,
                    onLoadingCompleted = {
                        onAction(AccountsAction.RefreshCompleted)
                    },
                    onAccountClicked = { accountType, accountId ->
                        onAction(AccountsAction.OnAccountClicked(accountId, accountType))
                    },
                    accountTypeFilters = typeFilters.map { it.statusLabel },
                    accountStatusFilters = statusFilters.map { it.statusLabel },
                    filtersClicked = { onAction(AccountsAction.ToggleFilter) },
                )
            }
            AccountType.LOAN -> {
                LoanAccountScreen(
                    navigateBack = { onAction(AccountsAction.OnNavigateBack) },
                    refreshSignal = state.refreshSignal,
                    onLoadingCompleted = {
                        onAction(AccountsAction.RefreshCompleted)
                    },
                    onAccountClicked = { accountType, accountId ->
                        onAction(AccountsAction.OnAccountClicked(accountId, accountType))
                    },
                    accountTypeFilters = typeFilters.map { it.statusLabel },
                    accountStatusFilters = statusFilters.map { it.statusLabel },
                    filtersClicked = { onAction(AccountsAction.ToggleFilter) },
                )
            }
            AccountType.SHARE -> {
                ShareAccountScreen(
                    navigateBack = { onAction(AccountsAction.OnNavigateBack) },
                    refreshSignal = state.refreshSignal,
                    onLoadingCompleted = {
                        onAction(AccountsAction.RefreshCompleted)
                    },
                    onAccountClicked = { accountType, accountId ->
                        onAction(AccountsAction.OnAccountClicked(accountId, accountType))
                    },
                    accountTypeFilters = typeFilters.map { it.statusLabel },
                    accountStatusFilters = statusFilters.map { it.statusLabel },
                    filtersClicked = { onAction(AccountsAction.ToggleFilter) },
                )
            }
        }
    }
}

@DevicePreview
@Composable
internal fun ClientAccountsScreenPreview() {
    MifosMobileTheme {
        AccountScreenContent(
            state = AccountsState(dialogState = null),
            onAction = {},
        )
    }
}
