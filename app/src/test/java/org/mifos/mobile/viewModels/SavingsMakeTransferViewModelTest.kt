package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
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
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class SavingsMakeTransferViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var savingsAccountRepositoryImp : SavingsAccountRepository

    @Mock
    lateinit var savingsMakeTransferUiStateObserver : Observer<SavingsAccountUiState>

    private lateinit var savingsMakeTransferViewModel : SavingsMakeTransferViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsMakeTransferViewModel = SavingsMakeTransferViewModel(savingsAccountRepositoryImp)
        savingsMakeTransferViewModel.savingsMakeTransferUiState.observeForever(savingsMakeTransferUiStateObserver)
    }

    @Test
    fun testLoanAccountTransferTemplate_SuccessResponseFromRepository_ReturnsShowSavingsAccountTemplate() {
        val mockAccountOptionsTemplate = Mockito.mock(AccountOptionsTemplate::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.loanAccountTransferTemplate()
        ).thenReturn(Observable.just(mockAccountOptionsTemplate))

        savingsMakeTransferViewModel.loanAccountTransferTemplate()

        Mockito.verify(savingsMakeTransferUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsMakeTransferUiStateObserver).onChanged(SavingsAccountUiState.ShowSavingsAccountTemplate(mockAccountOptionsTemplate))
        Mockito.verifyNoMoreInteractions(savingsMakeTransferUiStateObserver)
    }

    @Test
    fun testLoanAccountTransferTemplate_ErrorResponseFromRepository_ReturnsErrorMessage() {
        val errorResponse = RuntimeException("Loading Failed")
        Mockito.`when`(
            savingsAccountRepositoryImp.loanAccountTransferTemplate()
        ).thenReturn(Observable.error(errorResponse))

        savingsMakeTransferViewModel.loanAccountTransferTemplate()

        Mockito.verify(savingsMakeTransferUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsMakeTransferUiStateObserver).onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
        Mockito.verifyNoMoreInteractions(savingsMakeTransferUiStateObserver)
    }

    @After
    fun tearDown() {
        savingsMakeTransferViewModel.savingsMakeTransferUiState.removeObserver(savingsMakeTransferUiStateObserver)
    }
}