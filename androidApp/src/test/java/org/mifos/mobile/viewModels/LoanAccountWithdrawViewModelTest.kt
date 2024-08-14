package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.ui.loan_account_withdraw.LoanAccountWithdrawViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoanAccountWithdrawViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var loanRepositoryImp: LoanRepositoryImp

    private lateinit var viewModel: LoanAccountWithdrawViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanAccountWithdrawViewModel(loanRepositoryImp)
       
    }

    @Test
    fun testWithdrawLoanAccount_Successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(ResponseBody::class.java)
        val mockLoanWithdraw = mock(LoanWithdraw::class.java)
        `when`(
            loanRepositoryImp.withdrawLoanAccount(
                1,
                mockLoanWithdraw
            )
        ).thenReturn(flowOf(response))
        viewModel.loanUiState.test {
            viewModel.withdrawLoanAccount(1, mockLoanWithdraw)
            assertEquals( LoanUiState.Loading,awaitItem())
            assertEquals( LoanUiState.WithdrawSuccess,awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testWithdrawLoanAccount_Unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val mockLoanWithdraw = mock(LoanWithdraw::class.java)
        `when`(
            loanRepositoryImp.withdrawLoanAccount(
                1,
                mockLoanWithdraw
            )
        ).thenThrow(Exception("Error occurred"))
        viewModel.loanUiState.test {
            viewModel.withdrawLoanAccount(1, mockLoanWithdraw)
            assertEquals( LoanUiState.Loading,awaitItem())
            assertEquals( LoanUiState.ShowError(R.string.error_loan_account_withdraw),awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
     
    }

}