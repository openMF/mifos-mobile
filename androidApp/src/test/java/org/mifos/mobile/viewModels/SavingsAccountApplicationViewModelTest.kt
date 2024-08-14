package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.ui.savings_account_application.SavingsAccountApplicationUiState
import org.mifos.mobile.ui.savings_account_application.SavingsAccountApplicationViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
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
    lateinit var preferenseHelper : PreferencesHelper


    private lateinit var savingsAccountApplicationViewModel: SavingsAccountApplicationViewModel
    private val mockClientId = 1L
    private val mockAccountId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsAccountApplicationViewModel =
            SavingsAccountApplicationViewModel(savingsAccountRepositoryImp,
                preferenseHelper)
       
    }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_InputCreateStateSuccessResponseFromRepository_ReturnsShowUserInterfaceSavingAccountApplication() =
       runTest {
            val mockState = org.mifos.mobile.core.model.enums.SavingsAccountState.CREATE
            val clientId = 1L
            val mockTemplate = Mockito.mock(SavingsAccountTemplate::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(flowOf(mockTemplate))
           `when`(preferenseHelper.clientId).thenReturn(clientId)

           savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate()
            advanceUntilIdle()
           assertEquals(
               SavingsAccountApplicationUiState.ShowUserInterface(mockTemplate, mockState),
                savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
            )
       }

    @Test
    fun testLoadSavingsAccountApplicationTemplate_InputUpdateStateSuccessResponseFromRepository_ReturnsShowUserInterfaceSavingAccountUpdate() =
       runTest {
            val mockState = org.mifos.mobile.core.model.enums.SavingsAccountState.UPDATE
            val mockTemplate = Mockito.mock(SavingsAccountTemplate::class.java)
            val mockPayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
            val mockResponse = Mockito.mock(ResponseBody::class.java)
            val mockSavingsWithAssociations = Mockito.mock(SavingsWithAssociations::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(flowOf(mockTemplate))
              `when`(preferenseHelper.clientId).thenReturn(mockClientId)
           `when`(savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId))
               .thenReturn(flow { emit(mockTemplate) })
          // Set state to UPDATE
           savingsAccountApplicationViewModel.setSavingsAccountState(org.mifos.mobile.core.model.enums.SavingsAccountState.UPDATE)
           savingsAccountApplicationViewModel.setSavingsWithAssociations(mockSavingsWithAssociations)
            savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate()
            advanceUntilIdle()

           assertEquals(
                SavingsAccountApplicationUiState.ShowUserInterface(mockTemplate,mockState),
                savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
            )
       }

    @Test(expected =  Exception::class)
    fun testLoadSavingsAccountApplicationTemplate_ErrorResponseFromRepository_ReturnsErrorMessage() =
       runTest {
            val errorResponse =  Exception("Loading Failed")
            val mockState = org.mifos.mobile.core.model.enums.SavingsAccountState.UPDATE
            Mockito.`when`(
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            ).thenThrow(errorResponse)
           savingsAccountApplicationViewModel.loadSavingsAccountApplicationTemplate()
            advanceUntilIdle()
            assertEquals(
                SavingsAccountUiState.ErrorMessage(errorResponse),
                savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
            )
       }

    @Test
    fun testSubmitSavingsAccountApplication_SuccessResponseFromRepository_ReturnsHideProgress() =
       runTest {
            val responseBody = Mockito.mock(ResponseBody::class.java)
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitSavingAccountApplication(
                    mockSavingsAccountApplicationPayload
                )
            ).thenReturn(flowOf(responseBody))


            savingsAccountApplicationViewModel.submitSavingsAccountApplication(
                mockSavingsAccountApplicationPayload
            )
            advanceUntilIdle()
            assertEquals(
                SavingsAccountApplicationUiState.Success(org.mifos.mobile.core.model.enums.SavingsAccountState.CREATE),
                savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
            )

       }

    @Test(expected = Exception::class)
    fun testSubmitSavingsAccountApplication_SuccessResponseFromRepository_ReturnsSavingsAccountUpdateSuccess() =
       runTest {
            val errorResponse =  Exception("Submitting Failed")
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitSavingAccountApplication(
                    mockSavingsAccountApplicationPayload
                )
            ).thenThrow(errorResponse)
            savingsAccountApplicationViewModel.submitSavingsAccountApplication(
                mockSavingsAccountApplicationPayload
            )
            advanceUntilIdle()
            assertEquals(
                SavingsAccountUiState.ErrorMessage(errorResponse),
                savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
            )
       }

    @Test(expected = Exception::class)
    fun testSubmitSavingsAccountApplication_ErrorResponseFromRepository_ReturnsErrorMessage() =
       runTest {
            val errorResponse =  Exception("Submitting Failed")
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.submitSavingAccountApplication(
                    mockSavingsAccountApplicationPayload
                )
            ).thenThrow(errorResponse)
            savingsAccountApplicationViewModel.submitSavingsAccountApplication(
                mockSavingsAccountApplicationPayload
            )
           advanceUntilIdle()
            assertEquals(
                SavingsAccountUiState.ErrorMessage(errorResponse),
                savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
            )
        }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsHideProgress() =runTest {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.updateSavingsAccount(
                mockAccountId,
                mockSavingsAccountUpdatePayload
            )
        ).thenReturn(flowOf(responseBody))
        savingsAccountApplicationViewModel.setSavingsAccountState(org.mifos.mobile.core.model.enums.SavingsAccountState.UPDATE)
        savingsAccountApplicationViewModel.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )
        advanceUntilIdle()
        assertEquals(
            SavingsAccountApplicationUiState.Success(org.mifos.mobile.core.model.enums.SavingsAccountState.UPDATE),
            savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
        )
    }

    @Test(expected = Exception::class)
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsSavingsAccountUpdateSuccess() =
       runTest {
            val errorResponse =  Exception("Update Failed")
            val mockSavingsAccountUpdatePayload =
                Mockito.mock(SavingsAccountUpdatePayload::class.java)
            Mockito.`when`(
                savingsAccountRepositoryImp.updateSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountUpdatePayload
                )
            ).thenThrow(errorResponse)
            savingsAccountApplicationViewModel.updateSavingsAccount(
                mockAccountId,
                mockSavingsAccountUpdatePayload
            )
            advanceUntilIdle()
            assertEquals(
                SavingsAccountUiState.ErrorMessage(errorResponse),
                savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
            )
       }

    @Test(expected = Exception::class)
    fun testUpdateSavingsAccount_SuccessResponseFromRepository_ReturnsErrorMessage() =runTest {
        val errorResponse =  Exception("Update Failed")
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        Mockito.`when`(
            savingsAccountRepositoryImp.updateSavingsAccount(
                mockAccountId,
                mockSavingsAccountUpdatePayload
            )
        ).thenThrow(errorResponse)
        savingsAccountApplicationViewModel.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )
        advanceUntilIdle()
        assertEquals(
            SavingsAccountUiState.ErrorMessage(errorResponse),
            savingsAccountApplicationViewModel.savingsAccountApplicationUiState.value
        )
    }

    @After
    fun tearDown() {
        
    }
}