package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

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

    @Mock
    lateinit var savingsAccountWithdrawUiStateObserver: Observer<SavingsAccountUiState>

    private lateinit var savingsAccountWithdrawViewModel: SavingsAccountWithdrawViewModel
    private val mockAccountId = "1"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsAccountWithdrawViewModel =
            SavingsAccountWithdrawViewModel(savingsAccountRepositoryImp)
        savingsAccountWithdrawViewModel.savingsAccountWithdrawUiState.observeForever(
            savingsAccountWithdrawUiStateObserver
        )
    }

    @Test
    fun testSubmitWithdrawSavingsAccount_SuccessReceivedFromRepository_ReturnsSavingsAccountWithdrawSuccess() =
        runBlocking {
            val mockSavingsAccountWithdrawPayload =
                Mockito.mock(SavingsAccountWithdrawPayload::class.java)
            val responseBody = Mockito.mock(ResponseBody::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountWithdrawPayload
                )
            ).thenReturn(Response.success(responseBody))

            savingsAccountWithdrawViewModel.submitWithdrawSavingsAccount(
                mockAccountId,
                mockSavingsAccountWithdrawPayload
            )

            Mockito.verify(savingsAccountWithdrawUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountWithdrawUiStateObserver)
                .onChanged(SavingsAccountUiState.SavingsAccountWithdrawSuccess)
            Mockito.verifyNoMoreInteractions(savingsAccountWithdrawUiStateObserver)
        }

    @Test
    fun testSubmitWithdrawSavingsAccount_ErrorResponseFromRepository_ReturnsErrorMessage() =
        runBlocking {
            val mockSavingsAccountWithdrawPayload =
                Mockito.mock(SavingsAccountWithdrawPayload::class.java)
            val errorResponse = RuntimeException("Submit Failed")
            Mockito.`when`(
                savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountWithdrawPayload
                )
            ).thenReturn(Response.error(404, "error".toResponseBody(null)))

            savingsAccountWithdrawViewModel.submitWithdrawSavingsAccount(
                mockAccountId,
                mockSavingsAccountWithdrawPayload
            )

            Mockito.verify(savingsAccountWithdrawUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountWithdrawUiStateObserver)
                .onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
            Mockito.verifyNoMoreInteractions(savingsAccountWithdrawUiStateObserver)
        }

    @After
    fun tearDown() {
        savingsAccountWithdrawViewModel.savingsAccountWithdrawUiState.removeObserver(
            savingsAccountWithdrawUiStateObserver
        )
    }
}