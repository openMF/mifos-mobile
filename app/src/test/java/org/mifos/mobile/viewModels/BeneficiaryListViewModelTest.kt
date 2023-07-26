package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mifos.mobile.R
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BeneficiaryListViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp

    @Mock
    lateinit var beneficiaryUiStateObserver: Observer<BeneficiaryUiState>

    private lateinit var viewModel: BeneficiaryListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BeneficiaryListViewModel(beneficiaryRepositoryImp)
        viewModel.beneficiaryUiState.observeForever(beneficiaryUiStateObserver)
    }

    @Test
    fun testLoadBeneficiaries_Successful() {
        val list1 = mock(Beneficiary::class.java)
        val list2 = mock(Beneficiary::class.java)
        val list = listOf(list1, list2)

        `when`(beneficiaryRepositoryImp.beneficiaryList()).thenReturn(Observable.just(list))

        viewModel.loadBeneficiaries()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowBeneficiaryList(list))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @Test
    fun testLoadBeneficiaries_Unsuccessful() {
        val error = RuntimeException("Error Response")

        `when`(beneficiaryRepositoryImp.beneficiaryList()).thenReturn(Observable.error(error))

        viewModel.loadBeneficiaries()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.beneficiaries))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
    }

    @After
    fun tearDown() {
        viewModel.beneficiaryUiState.removeObserver(beneficiaryUiStateObserver)
    }
}