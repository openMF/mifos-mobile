package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mifos.mobile.R
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

    @Mock
    lateinit var loanUiStateObserver: Observer<LoanUiState>

    private lateinit var viewModel: LoanAccountWithdrawViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanAccountWithdrawViewModel(loanRepositoryImp)
        viewModel.loanUiState.observeForever(loanUiStateObserver)
    }

    @Test
    fun testWithdrawLoanAccount_Successful() {
        val response = mock(ResponseBody::class.java)
        val mockLoanWithdraw = mock(LoanWithdraw::class.java)
        `when`(
            loanRepositoryImp.withdrawLoanAccount(
                1,
                mockLoanWithdraw
            )
        ).thenReturn(Observable.just(response))
        viewModel.withdrawLoanAccount(1, mockLoanWithdraw)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        verify(loanUiStateObserver).onChanged(LoanUiState.WithdrawSuccess)
        verifyNoMoreInteractions(loanUiStateObserver)
    }

    @Test
    fun testWithdrawLoanAccount_Unsuccessful() {
        val error = RuntimeException("Error Response")
        val mockLoanWithdraw = mock(LoanWithdraw::class.java)
        `when`(
            loanRepositoryImp.withdrawLoanAccount(
                1,
                mockLoanWithdraw
            )
        ).thenReturn(Observable.error(error))
        viewModel.withdrawLoanAccount(1, mockLoanWithdraw)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        verify(loanUiStateObserver).onChanged(LoanUiState.ShowError(R.string.error_loan_account_withdraw))
        verifyNoMoreInteractions(loanUiStateObserver)
    }

    @After
    fun tearDown() {
        viewModel.loanUiState.removeObserver(loanUiStateObserver)
    }

}