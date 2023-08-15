package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SavingsAccountApplicationViewModelTest {
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
    lateinit var savingsAccountApplicationUiStateObserver: Observer<SavingsAccountUiState>

    private lateinit var savingsAccountApplicationViewModel: SavingsAccountApplicationViewModel
    private val mockClientId = 1L
    private val mockAccountId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsAccountApplicationViewModel =
            SavingsAccountApplicationViewModel(savingsAccountRepositoryImp)
        savingsAccountApplicationViewModel.savingsAccountApplicationUiState.observeForever(
            savingsAccountApplicationUiStateObserver
        )
    }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_InputCreateStateSuccessResponseFromRepository_ReturnsShowUserInterfaceSavingAccountApplication() =
        runBlocking {
            val mockState = SavingsAccountState.CREATE
            val mockTemplate = Mockito.mock(SavingsAccountTemplate::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(Response.success(mockTemplate))

            savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate(
                mockClientId,
                mockState
            )

            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountApplicationUiStateObserver).onChanged(
                SavingsAccountUiState.ShowUserInterfaceSavingAccountApplication(mockTemplate)
            )
            Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
        }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_InputUpdateStateSuccessResponseFromRepository_ReturnsShowUserInterfaceSavingAccountUpdate() =
        runBlocking {
            val mockState = SavingsAccountState.UPDATE
            val mockTemplate = Mockito.mock(SavingsAccountTemplate::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(Response.success(mockTemplate))

            savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate(
                mockClientId,
                mockState
            )

            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.ShowUserInterfaceSavingAccountUpdate(mockTemplate))
            Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
        }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_ErrorResponseFromRepository_ReturnsErrorMessage() =
        runBlocking {
            val errorResponse = RuntimeException("Loading Failed")
            val mockState = SavingsAccountState.UPDATE
            Mockito.`when`(
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

            savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate(
                mockClientId,
                mockState
            )

            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
            Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
        }

    @Test
    fun testSubmitSavingsAccountApplication_SuccessResponseFromRepository_ReturnsHideProgress() =
        runBlocking {
            val responseBody = Mockito.mock(ResponseBody::class.java)
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitSavingAccountApplication(
                    mockSavingsAccountApplicationPayload
                )
            ).thenReturn(Response.success(responseBody))

            savingsAccountApplicationViewModel.submitSavingsAccountApplication(
                mockSavingsAccountApplicationPayload
            )

            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.HideProgress)
        }

    @Test
    fun testSubmitSavingsAccountApplication_SuccessResponseFromRepository_ReturnsSavingsAccountUpdateSuccess() =
        runBlocking {
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitSavingAccountApplication(
                    mockSavingsAccountApplicationPayload
                )
            ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

            savingsAccountApplicationViewModel.submitSavingsAccountApplication(
                mockSavingsAccountApplicationPayload
            )

            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.SavingsAccountApplicationSuccess)
            Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
        }

    @Test
    fun testSubmitSavingsAccountApplication_ErrorResponseFromRepository_ReturnsErrorMessage() =
        runBlocking {
            val errorResponse = RuntimeException("Submitting Failed")
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitSavingAccountApplication(
                    mockSavingsAccountApplicationPayload
                )
            ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

            savingsAccountApplicationViewModel.submitSavingsAccountApplication(
                mockSavingsAccountApplicationPayload
            )

            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
            Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
        }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsHideProgress() = runBlocking {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.updateSavingsAccount(
                mockAccountId,
                mockSavingsAccountUpdatePayload
            )
        ).thenReturn(Response.success(responseBody))

        savingsAccountApplicationViewModel.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )

        Mockito.verify(savingsAccountApplicationUiStateObserver)
            .onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver)
            .onChanged(SavingsAccountUiState.HideProgress)
    }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsSavingsAccountUpdateSuccess() =
        runBlocking {
            val mockSavingsAccountUpdatePayload =
                Mockito.mock(SavingsAccountUpdatePayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.updateSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountUpdatePayload
                )
            ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

            savingsAccountApplicationViewModel.updateSavingsAccount(
                mockAccountId,
                mockSavingsAccountUpdatePayload
            )

            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.Loading)
            Mockito.verify(savingsAccountApplicationUiStateObserver)
                .onChanged(SavingsAccountUiState.SavingsAccountUpdateSuccess)
            Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
        }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsErrorMessage() = runBlocking {
        val errorResponse = RuntimeException("Update Failed")
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.updateSavingsAccount(
                mockAccountId,
                mockSavingsAccountUpdatePayload
            )
        ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

        savingsAccountApplicationViewModel.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )

        Mockito.verify(savingsAccountApplicationUiStateObserver)
            .onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingsAccountApplicationUiStateObserver)
            .onChanged(SavingsAccountUiState.ErrorMessage(errorResponse))
        Mockito.verifyNoMoreInteractions(savingsAccountApplicationUiStateObserver)
    }

    @After
    fun tearDown() {
        savingsAccountApplicationViewModel.savingsAccountApplicationUiState.removeObserver(
            savingsAccountApplicationUiStateObserver
        )
    }
}