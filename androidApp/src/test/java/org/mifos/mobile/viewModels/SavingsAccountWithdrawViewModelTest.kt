package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.ui.savings_account_withdraw.SavingsAccountWithdrawViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SavingsAccountWithdrawViewModelTest {
    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var savingsAccountRepositoryImp: SavingsAccountRepository

    private lateinit var savingsAccountWithdrawViewModel: SavingsAccountWithdrawViewModel
    private val mockAccountId = "1"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsAccountWithdrawViewModel =
            SavingsAccountWithdrawViewModel(savingsAccountRepositoryImp)
        
    }

    @Test
    fun testSubmitWithdrawSavingsAccount_SuccessReceivedFromRepository_ReturnsSavingsAccountWithdrawSuccess() =
        runTest{
            val mockSavingsAccountWithdrawPayload =
                Mockito.mock(SavingsAccountWithdrawPayload::class.java)
            val responseBody = Mockito.mock(ResponseBody::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountWithdrawPayload
                )
            ).thenReturn(flowOf(responseBody))
            savingsAccountWithdrawViewModel.savingsAccountWithdrawUiState.test {
                savingsAccountWithdrawViewModel.submitWithdrawSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountWithdrawPayload
                )
                assertEquals(SavingsAccountUiState.Initial, awaitItem())
                assertEquals(SavingsAccountUiState.Loading, awaitItem())
                assertEquals(SavingsAccountUiState.SavingsAccountWithdrawSuccess, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test(expected = Exception::class)
    fun testSubmitWithdrawSavingsAccount_ErrorResponseFromRepository_ReturnsErrorMessage() =
        runTest{
            val mockSavingsAccountWithdrawPayload =
                Mockito.mock(SavingsAccountWithdrawPayload::class.java)
            val errorResponse =  Exception("Submit Failed")
            Mockito.`when`(
                savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountWithdrawPayload
                )
            ).thenThrow(errorResponse )
            savingsAccountWithdrawViewModel.savingsAccountWithdrawUiState.test { 
                savingsAccountWithdrawViewModel.submitWithdrawSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountWithdrawPayload
                )
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