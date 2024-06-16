package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.ui.beneficiary_detail.BeneficiaryDetailViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
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

    private lateinit var viewModel: BeneficiaryDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BeneficiaryDetailViewModel(beneficiaryRepositoryImp)
    }
    @Test
    fun testDeleteBeneficiary_Successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response = mock(ResponseBody::class.java)
        `when`(beneficiaryRepositoryImp.deleteBeneficiary(123L))
            .thenReturn(flowOf(response))
        viewModel.beneficiaryUiState.test {
            viewModel.deleteBeneficiary(123L)
            assertEquals(BeneficiaryUiState.Initial, awaitItem())
            assertEquals(BeneficiaryUiState.DeletedSuccessfully, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }
    @Test(expected = Exception::class)
    fun testDeleteBeneficiary_Unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(beneficiaryRepositoryImp.deleteBeneficiary(123L))
            .thenThrow(Exception("Error deleting beneficiary"))
        viewModel.beneficiaryUiState.test {
            viewModel.deleteBeneficiary(123L)
            assertEquals(BeneficiaryUiState.Loading, awaitItem())
            assertEquals(
                BeneficiaryUiState.ShowError(R.string.error_deleting_beneficiary),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }
    @After
    fun tearDown() {
    }
}