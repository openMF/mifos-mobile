package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.repositories.GuarantorRepositoryImp
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.guarantor.guarantor_add.AddGuarantorViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.GuarantorUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class AddGuarantorViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var guarantorRepositoryImp: GuarantorRepositoryImp

    @Mock
    lateinit var guarantorUiStateObserver: Observer<GuarantorUiState>

    private lateinit var viewModel: AddGuarantorViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AddGuarantorViewModel(guarantorRepositoryImp)
    }
    fun TestScope.obserrveUiState(): MutableList<GuarantorUiState> {
        val uiStates = mutableListOf<GuarantorUiState>()
        viewModel.guarantorUiState.onEach {
            println(it)
            uiStates.add(it)
        }.launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        return uiStates
    }
    @Test
    fun testGetGuarantorTemplate_Successful() = runTest {
        val response = mock(GuarantorTemplatePayload::class.java)

        `when`(guarantorRepositoryImp.getGuarantorTemplate(123L)).thenReturn(
            flowOf(response)
        )
        viewModel.guarantorUiState.test {
            viewModel.getGuarantorTemplate(GuarantorState.UPDATE, 123L)
            assertEquals(GuarantorUiState.Loading, awaitItem())
            assertEquals(
                GuarantorUiState.ShowGuarantorUpdation(
                    response
                ),
                awaitItem()
            )
            viewModel.getGuarantorTemplate(GuarantorState.CREATE, 123L)
            assertEquals(GuarantorUiState.Loading, awaitItem())
            assertEquals(
                    GuarantorUiState.ShowGuarantorApplication(
                        response
                    ),
                    awaitItem()
                )
       }
    }

    @Test(expected =  Exception::class)
    fun testGetGuarantorTemplate_Unsuccessful() = runTest {
        `when`(guarantorRepositoryImp.getGuarantorTemplate(123L))
            .thenThrow( Exception("Error occurred"))
         viewModel.guarantorUiState.test {
            try {
                viewModel.getGuarantorTemplate(GuarantorState.UPDATE, 123L)
                assertEquals(GuarantorUiState.Loading, awaitItem())
            } catch (e: Exception) {
                assertEquals(
                    GuarantorUiState.ShowError(Throwable().message),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun testCreateGuarantor_Successful() = runTest {
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)
        `when`(guarantorRepositoryImp.createGuarantor(123L, payload)).thenReturn(
            flowOf(response)
        )
         viewModel.guarantorUiState.test {
            viewModel.createGuarantor(123L, payload)
            assertEquals(GuarantorUiState.Loading,awaitItem())
            assertEquals(
                GuarantorUiState.SubmittedSuccessfully(
                    response.string(),
                    payload
                ),
                awaitItem()
            )
             cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected =  Exception::class)
    fun testCreateGuarantor_Unsuccessful() = runTest {
        val payload = mock(GuarantorApplicationPayload::class.java)
        `when`(guarantorRepositoryImp.createGuarantor(123L, payload))
            .thenThrow( Exception("Error occurred"))
         viewModel.guarantorUiState.test {
                viewModel.createGuarantor(123L, payload)
                assertEquals(GuarantorUiState.Loading, awaitItem())
                assertEquals(GuarantorUiState.ShowError(Throwable().message), awaitItem())
             cancelAndIgnoreRemainingEvents()
         }
    }

    @Test
    fun testUpdateGuarantor_Successful() = runTest{
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)
        `when`(
            guarantorRepositoryImp.updateGuarantor(
                payload,
                11L,
                22L
            )
        ).thenReturn(flowOf(response))
         viewModel.guarantorUiState.test {
            viewModel.updateGuarantor(payload, 11L, 22L)
            assertEquals(GuarantorUiState.Loading, awaitItem())
            assertEquals(
                GuarantorUiState.GuarantorUpdatedSuccessfully(
                    response.string()
                ),
                awaitItem()
            )
             cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected =  Exception::class)
    fun testUpdateGuarantor_Unsuccessful() = runTest {
        val payload = mock(GuarantorApplicationPayload::class.java)
        `when`(guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L))
            .thenThrow( Exception("Error occurred"))
         viewModel.guarantorUiState.test {
                viewModel.updateGuarantor(payload, 11L, 22L)
                assertEquals(GuarantorUiState.Loading, awaitItem())
                assertEquals(GuarantorUiState.ShowError(Throwable().message), awaitItem())
             cancelAndIgnoreRemainingEvents()
         }
    }

}