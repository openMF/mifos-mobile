package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.repositories.GuarantorRepositoryImp
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.GuarantorUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

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

    @Test
    fun testGetGuarantorTemplate_Successful() = runBlocking {
        val response = mock(GuarantorTemplatePayload::class.java)

        `when`(guarantorRepositoryImp.getGuarantorTemplate(123L)).thenReturn(
            flowOf(response)
        )

        viewModel.getGuarantorTemplate(GuarantorState.UPDATE, 123L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(
            GuarantorUiState.ShowGuarantorUpdation(
                response
            )
        )

        viewModel.getGuarantorTemplate(GuarantorState.CREATE, 123L)
        verify(guarantorUiStateObserver).onChanged(
            GuarantorUiState.ShowGuarantorApplication(
                response
            )
        )
    }

    @Test
    fun testGetGuarantorTemplate_Unsuccessful() = runBlocking {
        val error = IOException("Error")

        `when`(guarantorRepositoryImp.getGuarantorTemplate(123L)).thenThrow(error)

        viewModel.getGuarantorTemplate(GuarantorState.CREATE, 123L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.ShowError(Throwable().message))

    }

    @Test
    fun testCreateGuarantor_Successful() = runBlocking {
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)

        `when`(guarantorRepositoryImp.createGuarantor(123L, payload)).thenReturn(
            flowOf(response)
        )

        viewModel.createGuarantor(123L, payload)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(
            GuarantorUiState.SubmittedSuccessfully(
                response.string(),
                payload
            )
        )
    }

    @Test
    fun testCreateGuarantor_Unsuccessful() {
        val error = IOException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(guarantorRepositoryImp.createGuarantor(123L, payload)).thenThrow(
            error
        )

        viewModel.createGuarantor(123L, payload)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.ShowError(Throwable().message))
    }

    @Test
    fun testUpdateGuarantor_Successful() {
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)

        `when`(
            guarantorRepositoryImp.updateGuarantor(
                payload,
                11L,
                22L
            )
        ).thenReturn(flowOf(response))

        viewModel.updateGuarantor(payload, 11L, 22L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(
            GuarantorUiState.GuarantorUpdatedSuccessfully(
                response.string()
            )
        )
    }

    @Test
    fun testUpdateGuarantor_Unsuccessful() {
        val error = IOException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)).thenThrow(error)

        viewModel.updateGuarantor(payload, 11L, 22L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.ShowError(Throwable().message))
    }

}