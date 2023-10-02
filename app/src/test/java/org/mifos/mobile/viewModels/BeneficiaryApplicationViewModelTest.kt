package org.mifos.mobile.viewModels

import CoroutineTestRule
import android.view.View
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
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class BeneficiaryApplicationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp

    @Mock
    lateinit var beneficiaryUiStateObserver: Observer<BeneficiaryUiState>

    private lateinit var viewModel: BeneficiaryApplicationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BeneficiaryApplicationViewModel(beneficiaryRepositoryImp)
        viewModel.beneficiaryUiState.observeForever(beneficiaryUiStateObserver)
    }

    @Test
    fun testLoadBeneficiaryTemplate_Successful() = runBlocking {
        val response = mock(BeneficiaryTemplate::class.java)
        `when`(beneficiaryRepositoryImp.beneficiaryTemplate()).thenReturn(Response.success(response))

        viewModel.loadBeneficiaryTemplate()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(
            BeneficiaryUiState.ShowBeneficiaryTemplate(
                response
            )
        )
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.SetVisibility(View.VISIBLE))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testLoadBeneficiaryTemplate_Unsuccessful() = runBlocking {
        `when`(beneficiaryRepositoryImp.beneficiaryTemplate()).thenReturn(
            Response.error(
                404,
                "error".toResponseBody(null)
            )
        )

        viewModel.loadBeneficiaryTemplate()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiary_template))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testCreateBeneficiary_Successful() = runBlocking {
        val response = mock(ResponseBody::class.java)
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)
        `when`(beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)).thenReturn(
            Response.success(
                response
            )
        )

        viewModel.createBeneficiary(beneficiaryPayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.CreatedSuccessfully)
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testCreateBeneficiary_Unsuccessful() = runBlocking {
        val error = RuntimeException("Error Response")
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)
        `when`(beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)).thenReturn(
            Response.error(404, "error".toResponseBody(null))
        )

        viewModel.createBeneficiary(beneficiaryPayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_creating_beneficiary))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testUpdateBeneficiary_Successful() = runBlocking {
        val response = mock(ResponseBody::class.java)
        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)
        `when`(
            beneficiaryRepositoryImp.updateBeneficiary(
                123L,
                beneficiaryUpdatePayload
            )
        ).thenReturn(
            Response.success(response)
        )

        viewModel.updateBeneficiary(123L, beneficiaryUpdatePayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.UpdatedSuccessfully)
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testUpdateBeneficiary_Unsuccessful() = runBlocking {
        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)
        `when`(
            beneficiaryRepositoryImp.updateBeneficiary(
                123L,
                beneficiaryUpdatePayload
            )
        ).thenReturn(
            Response.error(404, "error".toResponseBody(null))
        )

        viewModel.updateBeneficiary(123L, beneficiaryUpdatePayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_updating_beneficiary))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @After
    fun tearDown() {
        viewModel.beneficiaryUiState.removeObserver(beneficiaryUiStateObserver)
    }

}