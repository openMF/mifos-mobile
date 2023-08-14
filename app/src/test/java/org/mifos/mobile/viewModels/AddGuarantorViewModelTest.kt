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
import retrofit2.Response

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
        viewModel.guarantorUiState.observeForever(guarantorUiStateObserver)
    }

    @Test
    fun testGetGuarantorTemplate_Successful() = runBlocking {
        val response = mock(GuarantorTemplatePayload::class.java)

        `when`(guarantorRepositoryImp.getGuarantorTemplate(123L)).thenReturn(
            Response.success(
                response
            )
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
        val error: Response<GuarantorTemplatePayload?> =
            Response.error(404, ResponseBody.create(null, "error"))

        `when`(guarantorRepositoryImp.getGuarantorTemplate(123L)).thenReturn(error)

        viewModel.getGuarantorTemplate(GuarantorState.CREATE, 123L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
    }

    @Test
    fun testCreateGuarantor_Successful() = runBlocking {
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)

        `when`(guarantorRepositoryImp.createGuarantor(123L, payload)).thenReturn(
            Response.success(
                response
            )
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
    fun testCreateGuarantor_Unsuccessful() = runBlocking {
        val error: Response<ResponseBody?> = Response.error(404, ResponseBody.create(null, "error"))
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(guarantorRepositoryImp.createGuarantor(123L, payload)).thenReturn(
            error
        )

        viewModel.createGuarantor(123L, payload)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
    }

    @Test
    fun testUpdateGuarantor_Successful() = runBlocking {
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)

        `when`(
            guarantorRepositoryImp.updateGuarantor(
                payload,
                11L,
                22L
            )
        ).thenReturn(Response.success(response))

        viewModel.updateGuarantor(payload, 11L, 22L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(
            GuarantorUiState.GuarantorUpdatedSuccessfully(
                response.string()
            )
        )
    }

    @Test
    fun testUpdateGuarantor_Unsuccessful() = runBlocking {
        val error: Response<ResponseBody?> = Response.error(404, ResponseBody.create(null, "error"))
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)).thenReturn(error)

        viewModel.updateGuarantor(payload, 11L, 22L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
    }

    @After
    fun tearDown() {
        viewModel.guarantorUiState.removeObserver(guarantorUiStateObserver)
    }

}