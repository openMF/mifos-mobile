package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.R
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
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RecentTransactionViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

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
    fun loadRecentTransaction_success_with_no_empty_transactions() = runBlocking {
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
            .thenReturn(Response.success(transactions))

        viewModel.loadRecentTransactions(loadmore = false, offset)

        verify(recentTransactionUiStateObserver).onChanged(RecentTransactionUiState.Loading)
        Assert.assertEquals(
            transactions.pageItems.let { RecentTransactionUiState.RecentTransactions(it) },
            viewModel.recentTransactionUiState.value
        )
    }

    @Test
    fun loadRecentTransaction_success_with_empty_transactions() = runBlocking {
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
            .thenReturn(Response.success(transactions))

        viewModel.loadRecentTransactions(loadmore = false, offset)

        verify(recentTransactionUiStateObserver).onChanged(RecentTransactionUiState.Loading)
        Assert.assertEquals(
            RecentTransactionUiState.EmptyTransaction,
            viewModel.recentTransactionUiState.value
        )
    }

    @Test
    fun loadRecentTransaction_success_with_load_more_transactions() = runBlocking {
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
            .thenReturn(Response.success(transactions))

        viewModel.loadRecentTransactions(loadmore = true, offset)

        verify(recentTransactionUiStateObserver).onChanged(RecentTransactionUiState.Loading)
        Assert.assertEquals(
            transactions.pageItems.let { RecentTransactionUiState.LoadMoreRecentTransactions(it) },
            viewModel.recentTransactionUiState.value
        )
    }

    @Test
    fun loadRecentTransaction_unsuccessful() = runBlocking {
        `when`(recentTransactionRepositoryImp.recentTransactions(anyInt(), anyInt())).thenReturn(
            Response.error(404, ResponseBody.create(null, "error"))
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