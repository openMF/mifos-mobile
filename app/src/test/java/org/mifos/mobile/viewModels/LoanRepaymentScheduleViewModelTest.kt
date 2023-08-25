package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
class LoanRepaymentScheduleViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var loanRepositoryImp: LoanRepositoryImp

    private lateinit var viewModel: LoanRepaymentScheduleViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanRepaymentScheduleViewModel(loanRepositoryImp)
    }

    @Test
    fun testLoanLoanWithAssociations_Successful() = runBlocking {
        val response = mock(LoanWithAssociations::class.java)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenReturn(flowOf(response))

        viewModel.loanLoanWithAssociations(1)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        if (response.repaymentSchedule?.periods?.isNotEmpty() == true) {
            flowOf(LoanUiState.ShowLoan(response)).test {
                assertEquals(LoanUiState.ShowLoan(response), awaitItem())
                awaitComplete()
            }
        } else {
            flowOf(LoanUiState.ShowEmpty(response)).test {
                assertEquals(LoanUiState.ShowEmpty(response), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun testLoanLoanWithAssociations_Unsuccessful() = runBlocking {
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenThrow(
            RuntimeException("error")
        )
        viewModel.loanLoanWithAssociations(1)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(LoanUiState.ShowError(R.string.repayment_schedule)).test {
            assertEquals(LoanUiState.ShowError(R.string.repayment_schedule), awaitItem())
            awaitComplete()
        }
    }
}