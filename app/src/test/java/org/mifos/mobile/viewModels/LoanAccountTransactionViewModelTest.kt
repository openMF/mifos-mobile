package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.ui.loan_account_transaction.LoanAccountTransactionViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoanAccountTransactionViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var loanRepositoryImp: LoanRepositoryImp


    private lateinit var viewModel: LoanAccountTransactionViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanAccountTransactionViewModel(loanRepositoryImp)
    }

    @Test
    fun testLoadLoanAccountDetails_Successful_WithEmptyTransaction() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(LoanWithAssociations::class.java)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenReturn(flowOf(response))
        viewModel.loanUiState.test {
            viewModel.loadLoanAccountDetails(1)
            assertEquals(LoanUiState.Loading,awaitItem())
            assertEquals(LoanUiState.ShowEmpty(response),awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadLoanAccountDetails_Successful_WithNonEmptyTransaction() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(LoanWithAssociations::class.java).apply {
            `when`(transactions).thenReturn(mutableListOf(mock(Transaction::class.java)))
        }

        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenReturn(flowOf(response))
        viewModel.loanUiState.test {
            viewModel.loadLoanAccountDetails(1)
            assertEquals(LoanUiState.Loading,awaitItem())
            assertEquals(LoanUiState.ShowLoan(response),awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testLoadLoanAccountDetails_Unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenThrow(Exception("Error occurred"))
        viewModel.loanUiState.test {
            viewModel.loadLoanAccountDetails(1)
            assertEquals(LoanUiState.Loading,awaitItem())
            assertEquals(LoanUiState.ShowError(R.string.loan_account_details),awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
    }

}