/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transaction.screens

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.common.utils.CurrencyUtil
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.common.utils.Utils
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.recent_transaction.R
import org.mifos.mobile.feature.transaction.utils.RecentTransactionState
import org.mifos.mobile.feature.transaction.viewmodel.RecentTransactionViewModel

@Composable
internal fun RecentTransactionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecentTransactionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.recentTransactionUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isPaginating by viewModel.isPaginating.collectAsStateWithLifecycle()

    RecentTransactionScreen(
        uiState = uiState,
        isRefreshing = isRefreshing,
        isPaginating = isPaginating,
        navigateBack = navigateBack,
        onRetry = viewModel::loadInitialTransactions,
        onRefresh = viewModel::refresh,
        loadMore = viewModel::loadPaginatedTransactions,
        modifier = modifier,
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
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullToRefreshState()

    MifosScaffold(
        topBarTitleResId = R.string.recent_transactions,
        navigateBack = navigateBack,
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
                                    isNetworkConnected = Network.isConnected(context),
                                    isRetryEnabled = true,
                                    onRetry = onRetry,
                                )
                            }

                            is RecentTransactionState.Loading -> {
                                MifosProgressIndicatorOverlay()
                            }

                            is RecentTransactionState.Success -> {
                                if (uiState.transactions.isEmpty()) {
                                    EmptyDataView(
                                        icon = R.drawable.ic_error_black_24dp,
                                        error = R.string.no_transaction,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                } else {
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

        if (!isPaginating && canPaginate && isNearBottom) {
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
    val context = LocalContext.current

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_local_atm_black_24dp),
            contentDescription = stringResource(id = R.string.atm_icon),
            modifier = Modifier.size(40.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = Utils.formatTransactionType(transaction?.type?.value),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row {
                Text(
                    text = stringResource(
                        id = R.string.string_and_string,
                        transaction?.currency?.displaySymbol ?: transaction?.currency?.code ?: "",
                        CurrencyUtil.formatCurrency(
                            context,
                            transaction?.amount ?: 0.0,
                        ),
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = DateHelper.getDateAsString(transaction?.submittedOnDate),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

internal class RecentTransactionScreenPreviewProvider :
    PreviewParameterProvider<RecentTransactionState> {
    override val values: Sequence<RecentTransactionState>
        get() = sequenceOf(
            RecentTransactionState.Loading,
            RecentTransactionState.Error(""),
            RecentTransactionState.Success(listOf(), canPaginate = true),
        )
}

@DevicePreviews
@Composable
private fun RecentTransactionScreenPreview(
    @PreviewParameter(RecentTransactionScreenPreviewProvider::class)
    recentTransactionUiState: RecentTransactionState,
) {
    MifosMobileTheme {
        RecentTransactionScreen(
            uiState = recentTransactionUiState,
            isRefreshing = false,
            isPaginating = false,
            navigateBack = {},
            onRetry = {},
            onRefresh = {},
            loadMore = {},
        )
    }
}
