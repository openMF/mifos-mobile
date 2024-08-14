package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.ui.savings_make_transfer.SavingsMakeTransferViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SavingsMakeTransferViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var savingsAccountRepositoryImp: SavingsAccountRepository

    private lateinit var savingsMakeTransferViewModel: SavingsMakeTransferViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsMakeTransferViewModel = SavingsMakeTransferViewModel(savingsAccountRepositoryImp)
        
    }

    @Test
    fun testLoanAccountTransferTemplate_SuccessResponseFromRepository_ReturnsShowSavingsAccountTemplate() =
        runTest {
            val mockAccountOptionsTemplate = Mockito.mock(AccountOptionsTemplate::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.loanAccountTransferTemplate()
            ).thenReturn(flowOf( mockAccountOptionsTemplate))

            savingsMakeTransferViewModel.savingsMakeTransferUiState.test {

                savingsMakeTransferViewModel.loanAccountTransferTemplate()
                assertEquals(SavingsAccountUiState.Initial, awaitItem())
                assertEquals(SavingsAccountUiState.Loading, awaitItem())
                assertEquals(SavingsAccountUiState.ShowSavingsAccountTemplate(mockAccountOptionsTemplate), awaitItem())
                cancelAndIgnoreRemainingEvents()

            }
        }

    @Test(expected = Exception::class)
    fun testLoanAccountTransferTemplate_ErrorResponseFromRepository_ReturnsErrorMessage() =
        runTest {
            val errorResponse = Exception("Loading Failed")
            Mockito.`when`(
                savingsAccountRepositoryImp.loanAccountTransferTemplate()
            ).thenThrow(errorResponse)
            savingsMakeTransferViewModel.savingsMakeTransferUiState.test {
                savingsMakeTransferViewModel.loanAccountTransferTemplate()
                assertEquals(SavingsAccountUiState.Initial, awaitItem())
                assertEquals(SavingsAccountUiState.Loading, awaitItem())
                assertEquals(SavingsAccountUiState.ErrorMessage(errorResponse), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @After
    fun tearDown() {
        
    }
}