/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.accountTransactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_duration
import mifos_mobile.feature.accounts.generated.resources.feature_no__filtered_transactions_found
import mifos_mobile.feature.accounts.generated.resources.feature_no_transactions_found
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_icon_description
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_transaction_history
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_type
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.rememberMifosPullToRefreshState
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.FilterTopSection
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.TransactionScreenItem
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.accounts.component.FilterSection
import org.mifos.mobile.feature.accounts.model.TransactionFilterType
/**
 * Composable function for the Account Transactions Screen.
 *
 * @param navigateBack The function to be called when the back button is pressed.
 * @param viewModel The ViewModel for the Account Transactions Screen.
 */
@Composable
internal fun TransactionScreen(
    navigateBack: () -> Unit,
    viewModel: AccountsTransactionViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            AccountTransactionEvent.OnNavigateBack -> {
                navigateBack.invoke()
            }
        }
    }

    TransactionScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    AccountTransactionsDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * Composable function for the Account Transactions Screen Content.
 *
 * @param state The current state of the Account Transactions Screen.
 * @param onAction The function to be called when an action is performed.
 * @param modifier Optional modifier for the content.
 */
@Composable
internal fun TransactionScreenContent(
    state: AccountTransactionState,
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullToRefreshState = rememberMifosPullToRefreshState(
        isEnabled = true,
        isRefreshing = state.isRefreshing,
        onRefresh = {
            onAction(AccountTransactionAction.Refresh)
        },
    )

    MifosElevatedScaffold(
        onNavigateBack = {
            onAction(AccountTransactionAction.OnNavigateBackClick)
        },
        topBarTitle = stringResource(Res.string.feature_transaction_transaction_history),
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
        when (state.uiState) {
            ScreenUiState.Empty -> {
                EmptyDataView(
                    error = Res.string.feature_no_transactions_found,
                    icon = MifosIcons.Info,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(AccountTransactionAction.Refresh) },
                )
            }
            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(AccountTransactionAction.Refresh) },
                )
            }

            ScreenUiState.Success -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(DesignToken.padding.large),
                ) {
                    if (state.data.isNotEmpty()) {
                        ActionBar(
                            onAction = onAction,
                        )
                    }

                    if (state.isFilteredRecordsEmpty) {
                        EmptyDataView(
                            error = Res.string.feature_no__filtered_transactions_found,
                            icon = MifosIcons.Info,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        LazyColumn {
                            state.filteredData.forEach { (date, transactions) ->
                                item {
                                    Text(
                                        text = date,
                                        style = MifosTypography.labelLargeEmphasized,
                                        modifier = Modifier.padding(vertical = DesignToken.padding.medium),
                                    )
                                }

                                items(transactions.size) { index ->
                                    val transaction = transactions[index]
                                    TransactionScreenItem(
                                        title = transaction.typeValue ?: "",
                                        date = DateHelper.getDateAsString(transaction.date),
                                        time = "",
                                        transactionAmount = CurrencyFormatter.format(
                                            balance = transaction.amount,
                                            currencyCode = transaction.currency,
                                            maximumFractionDigits = 3,
                                        ),
                                        isCredited = transaction.isCredit == true,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> { }
        }
    }
}

/**
 * Composable function for the Account Transactions Dialog.
 *
 * @param state The current state of the Account Transactions Screen.
 * @param onAction The function to be called when an action is performed.
 */
@Composable
internal fun AccountTransactionsDialog(
    state: AccountTransactionState,
    onAction: (AccountTransactionAction) -> Unit,
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

        AccountTransactionState.DialogState.Filters -> {
            TransactionFilters(
                state = state,
                onAction = onAction,
            )
        }

        null -> {}
    }
}

/**
 * Composable function for the Account Transactions Action Bar.
 *
 * @param onAction The function to be called when an action is performed.
 * @param modifier Optional modifier for the action bar.
 */
@Composable
internal fun ActionBar(
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = DesignToken.padding.medium),
        horizontalArrangement = Arrangement.End,
    ) {
        // TODO : commenting because this feature is not currently available, uncomment and implement later
//        Row(
//            modifier = Modifier.clickable {
//            },
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
//        ) {
//            Text(
//                text = stringResource(Res.string.feature_transaction_statement),
//                color = MaterialTheme.colorScheme.primary,
//                style = MifosTypography.bodySmallEmphasized,
//            )
//
//            Icon(
//                modifier = Modifier.size(DesignToken.sizes.iconSmall),
//                imageVector = MifosIcons.Download,
//                contentDescription = stringResource(Res.string.feature_transaction_download_icon_description),
//                tint = MaterialTheme.colorScheme.primary,
//            )
//        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.largeIncreased))

        Row(
            modifier = Modifier.clickable {
                onAction(AccountTransactionAction.ToggleFilter)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            Text(
                text = stringResource(Res.string.feature_transaction_filter),
                color = MaterialTheme.colorScheme.primary,
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.Filter,
                contentDescription = stringResource(Res.string.feature_transaction_filter_icon_description),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * Composable function for the Account Transactions Filters.
 *
 * @param state The current state of the Account Transactions Screen.
 * @param onAction The function to be called when an action is performed.
 * @param modifier Optional modifier for the filters.
 */
@Composable
internal fun TransactionFilters(
    state: AccountTransactionState,
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isTypeExpanded by rememberSaveable { mutableStateOf(true) }
    var isStatusExpanded by rememberSaveable { mutableStateOf(true) }

    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountTransactionAction.OnNavigateBackClick) },
        topBarTitle = stringResource(Res.string.feature_transaction_transaction_history),
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
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.large),
        ) {
            FilterTopSection(
                isAnyFilterSelected = state.isAnyFilterSelected,
                resetFilters = {
                    onAction(AccountTransactionAction.ResetFilters)
                },
                onApplyFilter = {
                    onAction(AccountTransactionAction.GetFilterResults)
                },
                dismissDialog = {
                    onAction(AccountTransactionAction.DismissDialog)
                },
            )

            Spacer(Modifier.height(DesignToken.spacing.largeIncreased))

            HorizontalDivider(modifier = Modifier.height(1.dp))

            FilterSection(
                title = stringResource(Res.string.feature_transaction_type),
                filtersSelected = state.accountTypeFiltersCount ?: 0,
                isExpanded = isTypeExpanded,
                onToggle = { isTypeExpanded = !isTypeExpanded },
                filters = state.checkboxOptions.filter { it.type == TransactionFilterType.TRANSACTION_TYPE },
                onCheckChanged = { label ->
                    onAction(AccountTransactionAction.ToggleCheckbox(label, TransactionFilterType.TRANSACTION_TYPE))
                },
            )

            FilterSection(
                title = stringResource(Res.string.feature_duration),
                filtersSelected = if (state.selectedRadioButton == null) 0 else 1,
                isExpanded = isStatusExpanded,
                onToggle = { isStatusExpanded = !isStatusExpanded },
                filters = state.checkboxOptions.filter { it.type == TransactionFilterType.DURATION },
                onCheckChanged = { label ->
                    onAction(AccountTransactionAction.ToggleRadioButton(label))
                },
                isRadio = true,
                selectedRadioButton = state.selectedRadioButton,
            )
        }
    }
}
