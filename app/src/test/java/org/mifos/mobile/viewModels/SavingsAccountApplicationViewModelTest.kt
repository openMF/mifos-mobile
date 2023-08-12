package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import okhttp3.ResponseBody

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class SavingsAccountApplicationViewModelTest {
    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var savingsAccountRepositoryImp : SavingsAccountRepository

    @Mock
    lateinit var savingsAccountApplicationUiStateObserver : Observer<SavingsAccountUiState>

    private lateinit var savingsAccountApplicationViewModel : SavingsAccountApplicationViewModel
    private val mockClientId = 1L
    private val mockAccountId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsAccountApplicationViewModel = SavingsAccountApplicationViewModel(savingsAccountRepositoryImp)
        savingsAccountApplicationViewModel.savingsAccountApplicationUiState.observeForever(savingsAccountApplicationUiStateObserver)
    }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_InputCreateStateSuccessResponseFromRepository_ReturnsShowUserInterfaceSavingAccountApplication() {
        val mockState = SavingsAccountState.CREATE
        val mockTemplate = Mockito.mock(SavingsAccountTemplate::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
        ).thenReturn(Observable.just(mockTemplate))

        savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate(mockClientId, mockState)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.ShowUserInterfaceSavingAccountApplication(mockTemplate))
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_InputUpdateStateSuccessResponseFromRepository_ReturnsShowUserInterfaceSavingAccountUpdate() {
        val mockState = SavingsAccountState.UPDATE
        val mockTemplate = Mockito.mock(SavingsAccountTemplate::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
        ).thenReturn(Observable.just(mockTemplate))

        savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate(mockClientId, mockState)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.ShowUserInterfaceSavingAccountUpdate(mockTemplate))
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_ErrorResponseFromRepository_ReturnsErrorMessage() {
        val errorResponse = RuntimeException("Loading Failed")
        val mockState = SavingsAccountState.UPDATE
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
        ).thenReturn(Observable.error(errorResponse))

        savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate(mockClientId, mockState)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @Test
    fun testSubmitSavingsAccountApplication_SuccessResponseFromRepository_ReturnsHideProgress() {
        val responseBody = Mockito.mock(ResponseBody::class.java)
        val mockSavingsAccountApplicationPayload = Mockito.mock(SavingsAccountApplicationPayload::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
        ).thenReturn(Observable.just(responseBody))

        savingsAccountApplicationViewModel.submitSavingsAccountApplication(mockSavingsAccountApplicationPayload)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.HideProgress)
    }

    @Test
    fun testSubmitSavingsAccountApplication_SuccessResponseFromRepository_ReturnsSavingsAccountUpdateSuccess() {
        val mockSavingsAccountApplicationPayload = Mockito.mock(SavingsAccountApplicationPayload::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
        ).thenReturn(Observable.empty())

        savingsAccountApplicationViewModel.submitSavingsAccountApplication(mockSavingsAccountApplicationPayload)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.SavingsAccountApplicationSuccess)
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @Test
    fun testSubmitSavingsAccountApplication_ErrorResponseFromRepository_ReturnsErrorMessage() {
        val errorResponse = RuntimeException("Submitting Failed")
        val mockSavingsAccountApplicationPayload = Mockito.mock(SavingsAccountApplicationPayload::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
        ).thenReturn(Observable.error(errorResponse))

        savingsAccountApplicationViewModel.submitSavingsAccountApplication(mockSavingsAccountApplicationPayload)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsHideProgress() {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        ).thenReturn(Observable.just(responseBody))

        savingsAccountApplicationViewModel.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.HideProgress)
    }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsSavingsAccountUpdateSuccess() {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        ).thenReturn(Observable.empty())

        savingsAccountApplicationViewModel.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.SavingsAccountUpdateSuccess)
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsErrorMessage() {
        val errorResponse = RuntimeException("Update Failed")
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        ).thenReturn(Observable.error(errorResponse))

        savingsAccountApplicationViewModel.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)

        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @After
    fun tearDown() {
        savingsAccountApplicationViewModel.savingsAccountApplicationUiState.removeObserver(savingsAccountApplicationUiStateObserver)
    }
}