package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.models.client.Currency
import org.mifos.mobile.models.client.Type
import org.mifos.mobile.repositories.RecentTransactionRepository
import org.mifos.mobile.ui.recent_transactions.RecentTransactionViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.RecentTransactionUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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
    lateinit var type: Type

    @Mock
    lateinit var currency: Currency

    lateinit var viewModel: RecentTransactionViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = RecentTransactionViewModel(recentTransactionRepositoryImp)
    }

    @Test
    fun loadRecentTransaction_success_with_no_empty_transactions() = runTest {
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
        val transactions: Page<Transaction> =
            Page(totalFilteredRecords = 1, pageItems = listOf(transaction))
        `when`(recentTransactionRepositoryImp.recentTransactions(offset, limit))
            .thenReturn(flowOf(transactions))
        viewModel.recentTransactionUiState.test {
            viewModel.loadRecentTransactions(loadMore = false, offset)
            assertEquals(RecentTransactionUiState.Initial, awaitItem())
            assertEquals(RecentTransactionUiState.Loading, awaitItem())
            assertEquals(transactions.pageItems.let { RecentTransactionUiState.RecentTransactions(it) }, awaitItem())
        }
    }

    @Test
    fun loadRecentTransaction_success_with_empty_transactions() = runTest {
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
        val transactions: Page<Transaction> =
            Page(totalFilteredRecords = 0, pageItems = listOf(transaction))
        `when`(recentTransactionRepositoryImp.recentTransactions(offset, limit))
            .thenReturn(flowOf(transactions))
        viewModel.recentTransactionUiState.test {
            viewModel.loadRecentTransactions(loadMore = false, offset)
            assertEquals(RecentTransactionUiState.Initial, awaitItem())
            assertEquals(RecentTransactionUiState.Loading, awaitItem())
            assertEquals(RecentTransactionUiState.EmptyTransaction, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadRecentTransaction_success_with_load_more_transactions() = runTest {
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
        val transactions: Page<Transaction> =
            Page(totalFilteredRecords = 1, pageItems = listOf(transaction))
        `when`(recentTransactionRepositoryImp.recentTransactions(offset, limit))
            .thenReturn(flowOf(transactions))

        viewModel.recentTransactionUiState.test {
            viewModel.loadRecentTransactions(loadMore = false, offset)
            assertEquals(RecentTransactionUiState.Initial, awaitItem())
            assertEquals(RecentTransactionUiState.Loading, awaitItem())
            assertEquals(transactions.pageItems.let { RecentTransactionUiState.RecentTransactions(it) }, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun loadRecentTransaction_unsuccessful() = runTest {
        `when`(recentTransactionRepositoryImp.recentTransactions(anyInt(), anyInt()))
            .thenThrow(Exception("Error occurred"))
        viewModel.recentTransactionUiState.test {
        viewModel.loadRecentTransactions(false, 0)
        assertEquals(RecentTransactionUiState.Initial, awaitItem())
        assertEquals(RecentTransactionUiState.Loading, awaitItem())
        assertEquals(RecentTransactionUiState.Error(R.string.recent_transactions), awaitItem())
        cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
     }

}