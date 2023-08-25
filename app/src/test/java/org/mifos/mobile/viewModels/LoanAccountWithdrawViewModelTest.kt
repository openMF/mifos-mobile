package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class LoanAccountWithdrawViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

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
    fun testWithdrawLoanAccount_Successful() = runBlocking {
        val response = mock(ResponseBody::class.java)
        val mockLoanWithdraw = mock(LoanWithdraw::class.java)
        `when`(
            loanRepositoryImp.withdrawLoanAccount(
                1,
                mockLoanWithdraw
            )
        ).thenReturn(flowOf(response))
        viewModel.withdrawLoanAccount(1, mockLoanWithdraw)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(LoanUiState.WithdrawSuccess).test {
            assertEquals(LoanUiState.WithdrawSuccess, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testWithdrawLoanAccount_Unsuccessful() = runBlocking {
        val mockLoanWithdraw = mock(LoanWithdraw::class.java)
        `when`(
            loanRepositoryImp.withdrawLoanAccount(
                1,
                mockLoanWithdraw
            )
        ).thenThrow(RuntimeException("error"))
        viewModel.withdrawLoanAccount(1, mockLoanWithdraw)

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