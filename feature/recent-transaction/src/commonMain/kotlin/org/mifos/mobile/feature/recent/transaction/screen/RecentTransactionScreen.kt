/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.recent_transaction.generated.resources.Res
import mifos_mobile.feature.recent_transaction.generated.resources.atm_icon
import mifos_mobile.feature.recent_transaction.generated.resources.ic_error_black_24dp
import mifos_mobile.feature.recent_transaction.generated.resources.ic_local_atm_black_24dp
import mifos_mobile.feature.recent_transaction.generated.resources.no_transaction
import mifos_mobile.feature.recent_transaction.generated.resources.recent_transactions
import mifos_mobile.feature.recent_transaction.generated.resources.string_and_string
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.Utils
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionState
import org.mifos.mobile.feature.recent.transaction.viewmodel.RecentTransactionViewModel

@Composable
internal fun RecentTransactionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecentTransactionViewModel = koinViewModel(),
) {
    val uiState by viewModel.recentTransactionUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isPaginating by viewModel.isPaginating.collectAsStateWithLifecycle()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    RecentTransactionScreen(
        uiState = uiState,
        isRefreshing = isRefreshing,
        isPaginating = isPaginating,
        navigateBack = navigateBack,
        onRetry = viewModel::loadInitialTransactions,
        onRefresh = viewModel::refresh,
        loadMore = viewModel::loadPaginatedTransactions,
        modifier = modifier,
        isNetworkAvailable = isNetworkAvailable,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentTransactionScreen(
    uiState: RecentTransactionState,
    isRefreshing: Boolean,
    isPaginating: Boolean,
    navigateBack: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    loadMore: (offset: Int) -> Unit,
    isNetworkAvailable: Boolean,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullToRefreshState()

    MifosScaffold(
        topBarTitle = stringResource(Res.string.recent_transactions),
        backPress = navigateBack,
        modifier = modifier,
        content = { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                PullToRefreshBox(
                    state = pullRefreshState,
                    onRefresh = onRefresh,
                    isRefreshing = isRefreshing,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        when (uiState) {
                            is RecentTransactionState.Error -> {
                                MifosErrorComponent(
                                    isNetworkConnected = isNetworkAvailable,
                                    isRetryEnabled = true,
                                    onRetry = onRetry,
                                )
                            }

                            is RecentTransactionState.Loading -> {
                                MifosProgressIndicatorOverlay()
                            }

                            is RecentTransactionState.Empty -> {
                                EmptyDataView(
                                    icon = vectorResource(resource = Res.drawable.ic_error_black_24dp),
                                    error = Res.string.no_transaction,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }

                            is RecentTransactionState.Success -> {
                                RecentTransactionsContent(
                                    transactions = uiState.transactions,
                                    isPaginating = isPaginating,
                                    loadMore = loadMore,
                                    canPaginate = uiState.canPaginate,
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun RecentTransactionsContent(
    transactions: List<Transaction>,
    isPaginating: Boolean,
    canPaginate: Boolean,
    loadMore: (offset: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyColumnState,
    ) {
        val visibleItems = lazyColumnState.layoutInfo.visibleItemsInfo
        val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0
        val isNearBottom = lastVisibleItemIndex >= transactions.size - 5

        if (!isPaginating && canPaginate && isNearBottom && transactions.size > visibleItems.size) {
            loadMore(transactions.size - 1)
        }

        items(items = transactions) { transaction ->
            RecentTransactionListItem(transaction)
        }

        if (isPaginating) {
            item {
                MifosProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun RecentTransactionListItem(
    transaction: Transaction?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_local_atm_black_24dp),
            contentDescription = stringResource(Res.string.atm_icon),
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = Utils.formatTransactionType(transaction?.type?.value),
                style = MaterialTheme.typography.bodyMedium,
            )
            Row {
                Text(
                    text = stringResource(
                        Res.string.string_and_string,
                        transaction?.currency?.displaySymbol ?: transaction?.currency?.code ?: "",
                        CurrencyFormatter.format(
                            transaction?.amount,
                            transaction?.currency?.code,
                            2,
                        ),
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .weight(1f),
                )
                Text(
                    text = DateHelper.getDateAsString(transaction!!.submittedOnDate),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier,
                )
            }
        }
    }
}
