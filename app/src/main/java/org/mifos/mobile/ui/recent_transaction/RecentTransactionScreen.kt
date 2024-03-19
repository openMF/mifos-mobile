package org.mifos.mobile.ui.recent_transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.utils.RecentTransactionUiState

/**
 * @author pratyush
 * @since 19/3/24
 */

@Composable
fun RecentTransactionScreen(
    recentTransactionUiState: RecentTransactionUiState,
    recentTransactionViewModel: RecentTransactionViewModel = hiltViewModel()
) {
    val isRefreshing by recentTransactionViewModel.isRefreshing.collectAsStateWithLifecycle()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { recentTransactionViewModel.refreshTransactionList() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            when (recentTransactionUiState) {
                RecentTransactionUiState.EmptyTransaction -> {
                    EmptyDataView(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        icon = R.drawable.ic_error_black_24dp,
                        error = R.string.recent_transactions
                    )
                }

                is RecentTransactionUiState.Error -> {
                    EmptyDataView(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        icon = R.drawable.ic_error_black_24dp,
                        error = R.string.recent_transactions
                    )
                }

                is RecentTransactionUiState.LoadMoreRecentTransactions -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(recentTransactionUiState.transactions) {
                            RecentTransactionItem(
                                amount = it?.amount.toString(),
                                date = it?.date.toString(),
                                value = it?.type?.value!!
                            )
                        }
                    }
                }

                RecentTransactionUiState.Loading -> {
                    MifosProgressIndicator(modifier = Modifier.fillMaxSize())
                }

                is RecentTransactionUiState.RecentTransactions -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(recentTransactionUiState.transactions) {
                            RecentTransactionItem(
                                amount = it?.amount.toString(),
                                date = it?.date.toString(),
                                value = it?.type?.value!!
                            )
                        }
                    }
                }

                RecentTransactionUiState.Initial -> {}
            }
        }
    }
}

@Preview
@Composable
fun RecentTransactionScreenPreview() {
    RecentTransactionScreen(RecentTransactionUiState.EmptyTransaction)
}