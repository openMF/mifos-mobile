package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class LoanAccountTransactionViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

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
    fun testLoadLoanAccountDetails_Successful_WithEmptyTransaction() = runBlocking {
        val response = mock(LoanWithAssociations::class.java)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenReturn(flowOf(response))

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(LoanUiState.ShowEmpty(response)).test {
            assertEquals(LoanUiState.ShowEmpty(response), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testLoadLoanAccountDetails_Successful_WithNonEmptyTransaction() = runBlocking {
        val response = mock(LoanWithAssociations::class.java)

        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenReturn(flowOf(response))

        viewModel.loadLoanAccountDetails(1)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        if (response.transactions != null && response?.transactions?.isNotEmpty() == true) {
            flowOf(LoanUiState.ShowLoan(response)).test {
                assertEquals(LoanUiState.ShowLoan(response), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun testLoadLoanAccountDetails_Unsuccessful() = runBlocking {
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenThrow(RuntimeException("error"))

        viewModel.loadLoanAccountDetails(1)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(LoanUiState.ShowError(R.string.loan_account_details)).test {
            assertEquals(LoanUiState.ShowError(R.string.loan_account_details), awaitItem())
            awaitComplete()
        }
    }
}