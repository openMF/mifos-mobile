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
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.repositories.LoanRepositoryImp
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.loan_account_application.LoanApplicationViewModel
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

    private lateinit var viewModel: LoanApplicationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoanApplicationViewModel(loanRepositoryImp)
      
    }

    @Test
    fun testLoadLoanApplicationTemplate_Successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(LoanTemplate::class.java)
        val mockLoanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.template()).thenReturn(flowOf(response))
        viewModel.loanUiState.test {
            viewModel.loadLoanApplicationTemplate(mockLoanState)
            assertEquals(LoanUiState.Loading, awaitItem())
            if (mockLoanState == LoanState.CREATE) {
                assertEquals(LoanUiState.ShowLoanTemplateByProduct(response), awaitItem())
            } else {
                assertEquals(LoanUiState.ShowUpdateLoanTemplateByProduct(response), awaitItem())
            }
        }
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testLoadLoanApplicationTemplate_Unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val loanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.template())
            .thenThrow(Exception("Error occurred"))
        viewModel.loanUiState.test {
        viewModel.loadLoanApplicationTemplate(loanState)
        assertEquals(LoanUiState.Loading, awaitItem())
        assertEquals(LoanUiState.ShowError(R.string.error_fetching_template), awaitItem())
        cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test
    fun loadLoanApplicationTemplateByProduct_Successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(LoanTemplate::class.java)
        val mockLoanState = mock(LoanState::class.java)

        `when`(loanRepositoryImp.getLoanTemplateByProduct(1)).thenReturn(flowOf(response))
        viewModel.loanUiState.test {
            viewModel.loadLoanApplicationTemplateByProduct(1, mockLoanState)
            assertEquals(LoanUiState.Loading, awaitItem())
            if (mockLoanState == LoanState.CREATE) {
                assertEquals(LoanUiState.ShowLoanTemplate(response), awaitItem())
            } else {
                assertEquals(LoanUiState.ShowUpdateLoanTemplate(response), awaitItem())
            }
        }
        Dispatchers.resetMain()

    }

    @Test(expected = Exception::class)
    fun loadLoanApplicationTemplateByProduct_Unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val mockLoanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.getLoanTemplateByProduct(1))
            .thenThrow(Exception("Error occurred"))
        viewModel.loanUiState.test {
            viewModel.loadLoanApplicationTemplateByProduct(1, mockLoanState)
            assertEquals(LoanUiState.Loading, awaitItem())
            assertEquals(LoanUiState.ShowError(R.string.error_fetching_template), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
        
    }

}