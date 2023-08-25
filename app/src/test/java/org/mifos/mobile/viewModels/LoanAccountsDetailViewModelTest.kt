package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class LoanAccountsDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var loanRepositoryImp: LoanRepositoryImp

    @Mock
    lateinit var loanUiStateObserver: Observer<LoanUiState>

    lateinit var viewModel: LoanAccountsDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanAccountsDetailViewModel(loanRepositoryImp)
    }

    @Test
    fun testLoadLoanAccountDetails_Successful() = runBlocking {
        val response = mock(LoanWithAssociations::class.java)

        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenReturn(flowOf(response))

        viewModel.loadLoanAccountDetails(1)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(LoanUiState.ShowLoan(response)).test {
            assertEquals(LoanUiState.ShowLoan(response), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testLoadLoanAccountDetails_Unsuccessful() = runBlocking {
        val error = RuntimeException("error")

        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenThrow(error)

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