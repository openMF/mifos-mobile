package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mifos.mobile.R
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

    @Mock
    lateinit var loanUiStateObserver: Observer<LoanUiState>

    private lateinit var viewModel: LoanAccountTransactionViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanAccountTransactionViewModel(loanRepositoryImp)
        viewModel.loanUiState.observeForever(loanUiStateObserver)
    }

    @Test
    fun testLoadLoanAccountDetails_Successful_WithEmptyTransaction() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val response = mock(LoanWithAssociations::class.java)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenReturn(Observable.just(response))

        viewModel.loadLoanAccountDetails(1)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        verify(loanUiStateObserver).onChanged(LoanUiState.ShowEmpty(response))
        verifyNoMoreInteractions(loanUiStateObserver)

        Dispatchers.resetMain()
    }

    @Test
    fun testLoadLoanAccountDetails_Successful_WithNonEmptyTransaction() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val response = mock(LoanWithAssociations::class.java)

        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenReturn(Observable.just(response))

        viewModel.loadLoanAccountDetails(1)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        if (response.transactions != null && response?.transactions?.isNotEmpty() == true) {
            verify(loanUiStateObserver).onChanged(LoanUiState.ShowLoan(response))
            verifyNoMoreInteractions(loanUiStateObserver)
        }
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadLoanAccountDetails_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error = RuntimeException("Error Response")
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.TRANSACTIONS,
                1
            )
        ).thenReturn(Observable.error(error))

        viewModel.loadLoanAccountDetails(1)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        verify(loanUiStateObserver).onChanged(LoanUiState.ShowError(R.string.loan_account_details))
        verifyNoMoreInteractions(loanUiStateObserver)
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
        viewModel.loanUiState.removeObserver(loanUiStateObserver)
    }

}