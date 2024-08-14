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
import org.mifos.mobile.core.data.repositories.RecentTransactionRepository
import org.mifos.mobile.core.model.entity.Currency
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.Type
import org.mifos.mobile.feature.recent_transaction.utils.RecentTransactionState
import org.mifos.mobile.util.RxSchedulersOverrideRule
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

    lateinit var viewModel: org.mifos.mobile.feature.recent_transaction.viewmodel.RecentTransactionViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel =
            org.mifos.mobile.feature.recent_transaction.viewmodel.RecentTransactionViewModel(
                recentTransactionRepositoryImp
            )
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
            assertEquals(RecentTransactionState.Initial, awaitItem())
            assertEquals(RecentTransactionState.Loading, awaitItem())
            assertEquals(transactions.pageItems.let { RecentTransactionState.RecentTransactions(it) }, awaitItem())
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
            assertEquals(RecentTransactionState.Initial, awaitItem())
            assertEquals(RecentTransactionState.Loading, awaitItem())
            assertEquals(RecentTransactionState.EmptyTransaction, awaitItem())
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
            assertEquals(RecentTransactionState.Initial, awaitItem())
            assertEquals(RecentTransactionState.Loading, awaitItem())
            assertEquals(transactions.pageItems.let { RecentTransactionState.RecentTransactions(it) }, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun loadRecentTransaction_unsuccessful() = runTest {
        `when`(recentTransactionRepositoryImp.recentTransactions(anyInt(), anyInt()))
            .thenThrow(Exception("Error occurred"))
        viewModel.recentTransactionUiState.test {
        viewModel.loadRecentTransactions(false, 0)
        assertEquals(RecentTransactionState.Initial, awaitItem())
        assertEquals(RecentTransactionState.Loading, awaitItem())
        assertEquals(RecentTransactionState.Error(R.string.recent_transactions), awaitItem())
        cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
     }

}