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
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mifos.mobile.R
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BeneficiaryDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp

    @Mock
    lateinit var beneficiaryUiStateObserver: Observer<BeneficiaryUiState>

    private lateinit var viewModel: BeneficiaryDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BeneficiaryDetailViewModel(beneficiaryRepositoryImp)
        viewModel.beneficiaryUiState.observeForever(beneficiaryUiStateObserver)
    }

    @Test
    fun testDeleteBeneficiary_Successful() {
        val response = mock(ResponseBody::class.java)

        `when`(beneficiaryRepositoryImp.deleteBeneficiary(123L)).thenReturn(Observable.just(response))

        viewModel.deleteBeneficiary(123L)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.DeletedSuccessfully)
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testDeleteBeneficiary_Unsuccessful() {
        val error = RuntimeException("Error Response")

        `when`(beneficiaryRepositoryImp.deleteBeneficiary(123L)).thenReturn(Observable.error(error))

        viewModel.deleteBeneficiary(123L)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.error_deleting_beneficiary))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @After
    fun tearDown() {
        viewModel.beneficiaryUiState.removeObserver(beneficiaryUiStateObserver)
    }
}