package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
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
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class LoanAccountsDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

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
        viewModel.loanUiState.observeForever(loanUiStateObserver)
    }

    @Test
    fun testLoadLoanAccountDetails_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(LoanWithAssociations::class.java)

        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenReturn(Response.success(response))

        viewModel.loadLoanAccountDetails(1)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        verify(loanUiStateObserver).onChanged(LoanUiState.ShowLoan(response))
        verifyNoMoreInteractions(loanUiStateObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadLoanAccountDetails_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(
            loanRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                1
            )
        ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

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