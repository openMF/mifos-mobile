package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.ui.loan_account.LoanAccountDetailUiState
import org.mifos.mobile.ui.loan_account.LoanAccountsDetailViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.core.common.Constants
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LoanAccountsDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var loanRepositoryImp: LoanRepositoryImp

    lateinit var viewModel: LoanAccountsDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanAccountsDetailViewModel(loanRepositoryImp)

    }

    @Test
    fun testLoadLoanAccountDetails_Successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(LoanWithAssociations::class.java)

        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                org.mifos.mobile.core.common.Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenReturn(flowOf(response))
        viewModel.loadLoanAccountDetails(1)
        advanceUntilIdle()
        println(viewModel.loanUiState.value)
        assertEquals(viewModel.loanUiState.value, LoanAccountDetailUiState.Success(response))
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testLoadLoanAccountDetails_Unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                org.mifos.mobile.core.common.Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenThrow(Exception("Error occurred"))
        viewModel.loadLoanAccountDetails(1)
        advanceUntilIdle()
        assertEquals(viewModel.loanUiState.value,
            LoanAccountDetailUiState.Error)
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {

    }
}