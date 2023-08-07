package org.mifos.mobile.viewModels

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
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

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
    fun testLoadBeneficiaries_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val list1 = mock(Beneficiary::class.java)
        val list2 = mock(Beneficiary::class.java)
        val list = listOf(list1, list2)

        `when`(beneficiaryRepositoryImp.beneficiaryList()).thenReturn(Response.success(list))

        viewModel.loadBeneficiaries()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowBeneficiaryList(list))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadBeneficiaries_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error = RuntimeException("Error Response")

        `when`(beneficiaryRepositoryImp.beneficiaryList()).thenReturn(
            Response.error(
                404,
                ResponseBody.create(null, "error")
            )
        )

        viewModel.loadBeneficiaries()
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.Loading)
        verify(beneficiaryUiStateObserver).onChanged(BeneficiaryUiState.ShowError(R.string.beneficiaries))
        verifyNoMoreInteractions(beneficiaryUiStateObserver)
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
        viewModel.beneficiaryUiState.removeObserver(beneficiaryUiStateObserver)
    }
}