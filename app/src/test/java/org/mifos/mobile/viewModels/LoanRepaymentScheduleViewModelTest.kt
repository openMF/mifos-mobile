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
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.ui.loan_repayment_schedule.LoanRepaymentScheduleViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoanRepaymentScheduleViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

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
    fun testLoanLoanWithAssociations_Successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(LoanWithAssociations::class.java)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenReturn(flowOf(response))
        viewModel.loanUiState.test {
            viewModel.loanLoanWithAssociations(1)
            assertEquals(LoanUiState.Loading, awaitItem())
            if (response.repaymentSchedule?.periods?.isNotEmpty() == true) {
                assertEquals(LoanUiState.ShowLoan(response), awaitItem())
            } else {
                assertEquals(LoanUiState.ShowEmpty(response), awaitItem())
            }
        }
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testLoanLoanWithAssociations_Unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenThrow(Exception("Error occurred"))
        viewModel.loanUiState.test {
            viewModel.loanLoanWithAssociations(1)
            assertEquals(LoanUiState.Loading, awaitItem())
            assertEquals(LoanUiState.ShowError(R.string.repayment_schedule), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {

    }


}