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
import org.mifos.mobile.R
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.LoanUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoanApplicationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var loanRepositoryImp: LoanRepositoryImp

    @Mock
    lateinit var loanUiStateObserver: Observer<LoanUiState>

    private lateinit var viewModel: LoanApplicationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanApplicationViewModel(loanRepositoryImp)
        viewModel.loanUiState.observeForever(loanUiStateObserver)
    }

    @Test
    fun testLoadLoanApplicationTemplate_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val response = mock(LoanTemplate::class.java)
        val mockLoanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.template()).thenReturn(Observable.just(response))
        viewModel.loadLoanApplicationTemplate(mockLoanState)

        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        if (mockLoanState == LoanState.CREATE) {
            verify(loanUiStateObserver).onChanged(LoanUiState.ShowLoanTemplateByProduct(response))
            verifyNoMoreInteractions(loanUiStateObserver)
        } else {
            verify(loanUiStateObserver).onChanged(
                LoanUiState.ShowUpdateLoanTemplateByProduct(
                    response
                )
            )
            verifyNoMoreInteractions(loanUiStateObserver)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun testLoadLoanApplicationTemplate_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val error = RuntimeException("Error Response")
        val loanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.template()).thenReturn(Observable.error(error))
        viewModel.loadLoanApplicationTemplate(loanState)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        verify(loanUiStateObserver).onChanged(LoanUiState.ShowError(R.string.error_fetching_template))
        verifyNoMoreInteractions(loanUiStateObserver)

        Dispatchers.resetMain()
    }

    @Test
    fun loadLoanApplicationTemplateByProduct_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val response = mock(LoanTemplate::class.java)
        val mockLoanState = mock(LoanState::class.java)

        `when`(loanRepositoryImp.getLoanTemplateByProduct(1)).thenReturn(Observable.just(response))
        viewModel.loadLoanApplicationTemplateByProduct(1, mockLoanState)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        if (mockLoanState == LoanState.CREATE) {
            verify(loanUiStateObserver).onChanged(LoanUiState.ShowLoanTemplate(response))
            verifyNoMoreInteractions(loanUiStateObserver)
        } else {
            verify(loanUiStateObserver).onChanged(
                LoanUiState.ShowUpdateLoanTemplate(
                    response
                )
            )
            verifyNoMoreInteractions(loanUiStateObserver)
        }

        Dispatchers.resetMain()
    }

    @Test
    fun loadLoanApplicationTemplateByProduct_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val error = RuntimeException("Error Response")
        val mockLoanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.getLoanTemplateByProduct(1)).thenReturn(Observable.error(error))
        viewModel.loadLoanApplicationTemplateByProduct(1, mockLoanState)
        verify(loanUiStateObserver).onChanged(LoanUiState.Loading)
        verify(loanUiStateObserver).onChanged(LoanUiState.ShowError(R.string.error_fetching_template))
        verifyNoMoreInteractions(loanUiStateObserver)

        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
        viewModel.loanUiState.removeObserver(loanUiStateObserver)
    }

}