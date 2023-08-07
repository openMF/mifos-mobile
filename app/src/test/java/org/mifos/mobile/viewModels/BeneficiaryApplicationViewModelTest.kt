package org.mifos.mobile.viewModels

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
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
class BeneficiaryApplicationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

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
        Dispatchers.setMain(Dispatchers.Unconfined)
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
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadBeneficiaryTemplate_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(beneficiaryRepositoryImp.beneficiaryTemplate()).thenReturn(
            Response.error(
                404,
                ResponseBody.create(null, "error")
            )
        )

        viewModel.loadBeneficiaryTemplate()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiary_template))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun testCreateBeneficiary_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
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
        Dispatchers.resetMain()
    }

    @Test
    fun testCreateBeneficiary_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error = RuntimeException("Error Response")
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)
        `when`(beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)).thenReturn(
            Response.error(404, ResponseBody.create(null, "error"))
        )

        viewModel.createBeneficiary(beneficiaryPayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_creating_beneficiary))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun testUpdateBeneficiary_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
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
        Dispatchers.resetMain()
    }

    @Test
    fun testUpdateBeneficiary_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)
        `when`(
            beneficiaryRepositoryImp.updateBeneficiary(
                123L,
                beneficiaryUpdatePayload
            )
        ).thenReturn(
            Response.error(404, ResponseBody.create(null, "error"))
        )

        viewModel.updateBeneficiary(123L, beneficiaryUpdatePayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_updating_beneficiary))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
        viewModel.beneficiaryUiState.removeObserver(beneficiaryUiStateObserver)
    }

}