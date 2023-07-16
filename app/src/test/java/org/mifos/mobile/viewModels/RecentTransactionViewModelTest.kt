package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.models.client.Currency
import org.mifos.mobile.models.client.Type
import org.mifos.mobile.repositories.RecentTransactionRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.RecentTransactionUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mifos.mobile.R

@RunWith(MockitoJUnitRunner::class)
class RecentTransactionViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var recentTransactionRepositoryImp: RecentTransactionRepository

    @Mock
    lateinit var recentTransactionUiStateObserver: Observer<RecentTransactionUiState>

    @Mock
    lateinit var type: Type

    @Mock
    lateinit var currency: Currency

    lateinit var viewModel: RecentTransactionViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = RecentTransactionViewModel(recentTransactionRepositoryImp)
        viewModel.recentTransactionUiState.observeForever(recentTransactionUiStateObserver)
    }

    @Test
    fun loadRecentTransaction_success_with_no_empty_transactions() {
        val offset = 0
        val limit = 50

        val transaction = Transaction(
            id = 1L,
            officeId = 2L,
            officeName = "Office",
            type = type,
            date = listOf(2023, 7, 8),
            currency = currency,
            amount = 10.0,
            submittedOnDate = listOf(2023, 7, 9),
            reversed = false
        )
        val transactions: Page<Transaction?> =
            Page(totalFilteredRecords = 1, pageItems = listOf(transaction))
        `when`(recentTransactionRepositoryImp.recentTransactions(offset, limit))
            .thenReturn(Observable.just(transactions))

        viewModel.loadRecentTransactions(loadmore = false, offset)

        verify(recentTransactionUiStateObserver).onChanged(RecentTransactionUiState.Loading)
        Assert.assertEquals(
            transactions.pageItems.let { RecentTransactionUiState.RecentTransactions(it) },
            viewModel.recentTransactionUiState.value
        )
    }

    @Test
    fun loadRecentTransaction_success_with_empty_transactions() {
        val offset = 0
        val limit = 50

        val transaction = Transaction(
            id = 1L,
            officeId = 2L,
            officeName = "Office",
            type = type,
            date = listOf(2023, 7, 8),
            currency = currency,
            amount = 10.0,
            submittedOnDate = listOf(2023, 7, 9),
            reversed = false
        )
        val transactions: Page<Transaction?> =
            Page(totalFilteredRecords = 0, pageItems = listOf(transaction))
        `when`(recentTransactionRepositoryImp.recentTransactions(offset, limit))
            .thenReturn(Observable.just(transactions))

        viewModel.loadRecentTransactions(loadmore = false, offset)

        verify(recentTransactionUiStateObserver).onChanged(RecentTransactionUiState.Loading)
        Assert.assertEquals(
            RecentTransactionUiState.EmptyTransaction,
            viewModel.recentTransactionUiState.value
        )
    }

    @Test
    fun loadRecentTransaction_success_with_load_more_transactions() {
        val offset = 0
        val limit = 50

        val transaction = Transaction(
            id = 1L,
            officeId = 2L,
            officeName = "Office",
            type = type,
            date = listOf(2023, 7, 8),
            currency = currency,
            amount = 10.0,
            submittedOnDate = listOf(2023, 7, 9),
            reversed = false
        )
        val transactions: Page<Transaction?> =
            Page(totalFilteredRecords = 1, pageItems = listOf(transaction))
        `when`(recentTransactionRepositoryImp.recentTransactions(offset, limit))
            .thenReturn(Observable.just(transactions))

        viewModel.loadRecentTransactions(loadmore = true, offset)

        verify(recentTransactionUiStateObserver).onChanged(RecentTransactionUiState.Loading)
        Assert.assertEquals(
            transactions.pageItems.let { RecentTransactionUiState.LoadMoreRecentTransactions(it) },
            viewModel.recentTransactionUiState.value
        )

    }

    @Test
    fun loadRecentTransaction_unsuccessful() {
        val error = Throwable("Recent Transaction error")
        `when`(recentTransactionRepositoryImp.recentTransactions(anyInt(), anyInt())).thenReturn(
            Observable.error(error)
        )
        viewModel.loadRecentTransactions(false, 0)

        Assert.assertEquals(
            RecentTransactionUiState.Error(R.string.recent_transactions),
            viewModel.recentTransactionUiState.value
        )
    }

    @After
    fun tearDown() {
        viewModel.recentTransactionUiState.removeObserver(recentTransactionUiStateObserver)
    }

}