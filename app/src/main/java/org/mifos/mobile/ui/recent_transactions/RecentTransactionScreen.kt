package org.mifos.mobile.ui.recent_transactions

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.MifosSelfServiceApp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.models.client.Type
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Utils

@Composable
fun RecentTransactionScreen(
    viewModel: RecentTransactionViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.recentTransactionUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isPaginating by viewModel.isPaginating.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadInitialTransactions()
    }

    RecentTransactionScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        onRetry = { viewModel.loadInitialTransactions() },
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() },
        isPaginating = isPaginating,
        loadMore = { offset -> viewModel.loadPaginatedTransactions(offset) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentTransactionScreen(
    uiState: RecentTransactionUiState,
    navigateBack: () -> Unit,
    onRetry: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isPaginating: Boolean,
    loadMore: (offset: Int) -> Unit
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullToRefreshState()

    MFScaffold(
        topBarTitleResId = R.string.recent_transactions,
        navigateBack = navigateBack,
        scaffoldContent = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                when (uiState) {
                    is RecentTransactionUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isRetryEnabled = true,
                            onRetry = onRetry
                        )
                    }

                    is RecentTransactionUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is RecentTransactionUiState.Success -> {
                        if (uiState.transactions.isEmpty()) {
                            EmptyDataView(
                                icon = R.drawable.ic_error_black_24dp,
                                error = R.string.no_transaction,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            RecentTransactionsContent(
                                transactions = uiState.transactions,
                                isPaginating = isPaginating,
                                loadMore = loadMore,
                                canPaginate = uiState.canPaginate
                            )
                        }
                    }
                }
            }
        }
    )

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            onRefresh()
        }
    }
    LaunchedEffect(key1 = isRefreshing) {
        if (isRefreshing)
            pullRefreshState.startRefresh()
        else
            pullRefreshState.endRefresh()
    }

    PullToRefreshContainer(
        state = pullRefreshState,
    )
}

@Composable
fun RecentTransactionsContent(
    transactions: List<Transaction>,
    isPaginating: Boolean,
    canPaginate: Boolean,
    loadMore: (offset: Int) -> Unit
) {
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
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

        if(isPaginating) {
            item {
                MifosProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun RecentTransactionListItem(transaction: Transaction?) {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_local_atm_black_24dp),
            contentDescription = stringResource(id = R.string.atm_icon),
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = Utils.formatTransactionType(transaction?.type?.value),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                Text(
                    text = stringResource(
                        id = R.string.string_and_string,
                        transaction?.currency?.displaySymbol ?: transaction?.currency?.code ?: "",
                        CurrencyUtil.formatCurrency(MifosSelfServiceApp.context, transaction?.amount ?: 0.0,)
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f).alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = DateHelper.getDateAsString(transaction?.submittedOnDate),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }
    }
}


class RecentTransactionScreenPreviewProvider : PreviewParameterProvider<RecentTransactionUiState> {
    override val values: Sequence<RecentTransactionUiState>
        get() = sequenceOf(
            RecentTransactionUiState.Loading,
            RecentTransactionUiState.Error(""),
            RecentTransactionUiState.Success(listOf(), canPaginate = true)
        )
}

@Preview(showSystemUi = true)
@Composable
private fun RecentTransactionScreenPreview(
    @PreviewParameter(RecentTransactionScreenPreviewProvider::class) recentTransactionUiState: RecentTransactionUiState
) {
    MifosMobileTheme {
        RecentTransactionScreen(
            uiState = recentTransactionUiState,
            navigateBack = {},
            onRetry = {},
            isRefreshing = false,
            onRefresh = {},
            isPaginating = false,
            loadMore = {}
        )
    }
}

