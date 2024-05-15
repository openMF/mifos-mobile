package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.repositories.GuarantorRepositoryImp
import org.mifos.mobile.ui.guarantor.guarantor_details.GuarantorDetailViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.GuarantorUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GuarantorDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var guarantorRepositoryImp: GuarantorRepositoryImp


    lateinit var viewModel: GuarantorDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = GuarantorDetailViewModel(guarantorRepositoryImp)
    }

    @Test
    fun testDeleteGuarantor_Successful() = runTest {
        val response = mock(ResponseBody::class.java)

        `when`(guarantorRepositoryImp.deleteGuarantor(1L, 2L)).thenReturn(flowOf(response))
        viewModel.guarantorDeleteState.test {
            viewModel.deleteGuarantor(1L, 2L)
            assertEquals(GuarantorUiState.Loading, awaitItem())
            assertEquals(GuarantorUiState.GuarantorDeletedSuccessfully(response.string()), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testDeleteGuarantor_Unsuccessful() = runTest {
        val error = Exception("Error")
        `when`(guarantorRepositoryImp.deleteGuarantor(1L, 2L))
            .thenThrow(error)
        viewModel.guarantorDeleteState.test {
            viewModel.deleteGuarantor(1L, 2L)
            assertEquals(GuarantorUiState.Loading, awaitItem())
            assertEquals(GuarantorUiState.ShowError(error.message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

    }

}