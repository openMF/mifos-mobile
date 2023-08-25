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
@ExperimentalCoroutinesApi
class LoanApplicationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

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
    fun testLoadLoanApplicationTemplate_Successful() = runBlocking {
        val response = mock(LoanTemplate::class.java)
        val mockLoanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.template()).thenReturn(flowOf(response))

        viewModel.loadLoanApplicationTemplate(mockLoanState)

        if (mockLoanState == LoanState.CREATE) {
            flowOf(LoanUiState.Loading).test {
                assertEquals(LoanUiState.Loading, awaitItem())
                awaitComplete()
            }
        } else {
            flowOf(LoanUiState.ShowUpdateLoanTemplateByProduct(response)).test {
                assertEquals(LoanUiState.ShowUpdateLoanTemplateByProduct(response), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun testLoadLoanApplicationTemplate_Unsuccessful() = runBlocking {
        val loanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.template()).thenThrow(
            RuntimeException("error")
        )
        viewModel.loadLoanApplicationTemplate(loanState)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(LoanUiState.ShowError(R.string.error_fetching_template)).test {
            assertEquals(LoanUiState.ShowError(R.string.error_fetching_template), awaitItem())
            awaitComplete()
        }

    }

    @Test
    fun loadLoanApplicationTemplateByProduct_Successful() = runBlocking {
        val response = mock(LoanTemplate::class.java)
        val mockLoanState = mock(LoanState::class.java)

        `when`(loanRepositoryImp.getLoanTemplateByProduct(1)).thenReturn(flowOf(response))

        viewModel.loadLoanApplicationTemplateByProduct(1, mockLoanState)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        if (mockLoanState == LoanState.CREATE) {
            flowOf(LoanUiState.ShowLoanTemplate(response)).test {
                assertEquals(LoanUiState.ShowLoanTemplate(response), awaitItem())
                awaitComplete()
            }
        } else {
            flowOf(LoanUiState.ShowUpdateLoanTemplate(response)).test {
                assertEquals(LoanUiState.ShowUpdateLoanTemplate(response), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun loadLoanApplicationTemplateByProduct_Unsuccessful() = runBlocking {
        val mockLoanState = mock(LoanState::class.java)
        `when`(loanRepositoryImp.getLoanTemplateByProduct(1)).thenThrow(
            RuntimeException("error")
        )
        viewModel.loadLoanApplicationTemplateByProduct(1, mockLoanState)

        flowOf(LoanUiState.Loading).test {
            assertEquals(LoanUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(LoanUiState.ShowError(R.string.error_fetching_template)).test {
            assertEquals(LoanUiState.ShowError(R.string.error_fetching_template), awaitItem())
            awaitComplete()
        }
    }
}