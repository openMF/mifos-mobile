package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
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

@RunWith(MockitoJUnitRunner.Silent::class)
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

        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(GuarantorUiState.ShowGuarantorUpdation(response)).test {
            assertEquals(GuarantorUiState.ShowGuarantorUpdation(response), awaitItem())
            awaitComplete()
        }

        flowOf(GuarantorUiState.ShowGuarantorApplication(response)).test {
            assertEquals(GuarantorUiState.ShowGuarantorApplication(response), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testGetGuarantorTemplate_Unsuccessful() = runBlocking {
        val error = RuntimeException("Error")

        `when`(guarantorRepositoryImp.getGuarantorTemplate(123L)).thenThrow(error)

        viewModel.getGuarantorTemplate(GuarantorState.CREATE, 123L)
        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(GuarantorUiState.ShowError("error")).test {
            assertEquals(GuarantorUiState.ShowError("error"), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testCreateGuarantor_Successful() = runBlocking {
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)

        `when`(guarantorRepositoryImp.createGuarantor(123L, payload)).thenReturn(
            flowOf(response)
        )

        viewModel.createGuarantor(123L, payload)
        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(GuarantorUiState.SubmittedSuccessfully("response", payload)).test {
            assertEquals(GuarantorUiState.SubmittedSuccessfully("response", payload), awaitItem())
            awaitComplete()
        }

    }

    @Test
    fun testCreateGuarantor_Unsuccessful() = runBlocking {
        val error = RuntimeException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(guarantorRepositoryImp.createGuarantor(123L, payload)).thenThrow(
            error
        )

        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(GuarantorUiState.ShowError("error")).test {
            assertEquals(GuarantorUiState.ShowError("error"), awaitItem())
            awaitComplete()
        }

    }

    @Test
    fun testUpdateGuarantor_Successful() = runBlocking {
        val payload = mock(GuarantorApplicationPayload::class.java)
        val response = mock(ResponseBody::class.java)
        val mockTemplatePayload = mock(GuarantorTemplatePayload::class.java)

        `when`(
            guarantorRepositoryImp.updateGuarantor(
                payload,
                11L,
                22L
            )
        ).thenReturn(flowOf(response))

        viewModel.updateGuarantor(payload, 11L, 22L)

        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(GuarantorUiState.ShowGuarantorUpdation(mockTemplatePayload)).test {
            assertEquals(
                GuarantorUiState.ShowGuarantorUpdation(mockTemplatePayload),
                awaitItem()
            )
            awaitComplete()
        }
    }

    @Test
    fun testUpdateGuarantor_Unsuccessful() = runBlocking {
        val error = RuntimeException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)).thenThrow(error)

        viewModel.updateGuarantor(payload, 11L, 22L)

        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(GuarantorUiState.ShowError("error")).test {
            assertEquals(GuarantorUiState.ShowError("error"), awaitItem())
            awaitComplete()
        }
    }

}