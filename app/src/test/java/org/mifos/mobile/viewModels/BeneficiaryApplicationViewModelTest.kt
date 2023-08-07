package org.mifos.mobile.viewModels

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mifos.mobile.R
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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
    fun testLoadBeneficiaryTemplate_Successful() {
        val response = mock(BeneficiaryTemplate::class.java)
        `when`(beneficiaryRepositoryImp.beneficiaryTemplate()).thenReturn(Observable.just(response))

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
    fun testLoadBeneficiaryTemplate_Unsuccessful() {
        val error = RuntimeException("Error Response")
        `when`(beneficiaryRepositoryImp.beneficiaryTemplate()).thenReturn(Observable.error(error))

        viewModel.loadBeneficiaryTemplate()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiary_template))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testCreateBeneficiary_Successful() {
        val response = mock(ResponseBody::class.java)
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)
        `when`(beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)).thenReturn(
            Observable.just(
                response
            )
        )

        viewModel.createBeneficiary(beneficiaryPayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.CreatedSuccessfully)
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testCreateBeneficiary_Unsuccessful() {
        val error = RuntimeException("Error Response")
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)
        `when`(beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)).thenReturn(
            Observable.error(
                error
            )
        )

        viewModel.createBeneficiary(beneficiaryPayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_creating_beneficiary))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testUpdateBeneficiary_Successful() {
        val response = mock(ResponseBody::class.java)
        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)
        `when`(
            beneficiaryRepositoryImp.updateBeneficiary(
                123L,
                beneficiaryUpdatePayload
            )
        ).thenReturn(
            Observable.just(response)
        )

        viewModel.updateBeneficiary(123L, beneficiaryUpdatePayload)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.UpdatedSuccessfully)
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testUpdateBeneficiary_Unsuccessful() {
        val error = RuntimeException("Error Response")
        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)
        `when`(
            beneficiaryRepositoryImp.updateBeneficiary(
                123L,
                beneficiaryUpdatePayload
            )
        ).thenReturn(
            Observable.error(error)
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