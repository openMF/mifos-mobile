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
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
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

    @Mock
    lateinit var savingsMakeTransferUiStateObserver: Observer<SavingsAccountUiState>

    private lateinit var savingsMakeTransferViewModel: SavingsMakeTransferViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsMakeTransferViewModel = SavingsMakeTransferViewModel(savingsAccountRepositoryImp)
        savingsMakeTransferViewModel.savingsMakeTransferUiState.observeForever(
            savingsMakeTransferUiStateObserver
        )
    }

    @Test
    fun testLoanAccountTransferTemplate_SuccessResponseFromRepository_ReturnsShowSavingsAccountTemplate() =
        runBlocking {
            val mockAccountOptionsTemplate = Mockito.mock(AccountOptionsTemplate::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.loanAccountTransferTemplate()
            ).thenReturn(Response.success(mockAccountOptionsTemplate))

            savingsMakeTransferViewModel.loanAccountTransferTemplate()

            Mockito.verify(savingsMakeTransferUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsMakeTransferUiStateObserver).onChanged(
                SavingsAccountUiState.ShowSavingsAccountTemplate(mockAccountOptionsTemplate)
            )
            Mockito.verifyNoMoreInteractions(savingsMakeTransferUiStateObserver)
        }

    @Test
    fun testLoanAccountTransferTemplate_ErrorResponseFromRepository_ReturnsErrorMessage() =
        runBlocking {
            val errorResponse = RuntimeException("Loading Failed")
            Mockito.`when`(
                savingsAccountRepositoryImp.loanAccountTransferTemplate()
            ).thenReturn(Response.error(404, "error".toResponseBody(null)))

            savingsMakeTransferViewModel.loanAccountTransferTemplate()

            Mockito.verify(savingsMakeTransferUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsMakeTransferUiStateObserver)
                .onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
            Mockito.verifyNoMoreInteractions(savingsMakeTransferUiStateObserver)
        }

    @After
    fun tearDown() {
        savingsMakeTransferViewModel.savingsMakeTransferUiState.removeObserver(
            savingsMakeTransferUiStateObserver
        )
    }
}