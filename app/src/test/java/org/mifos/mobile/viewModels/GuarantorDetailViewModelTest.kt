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
import org.mifos.mobile.repositories.GuarantorRepositoryImp
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
    fun testDeleteGuarantor_Successful() = runBlocking {
        val response = mock(ResponseBody::class.java)

        `when`(guarantorRepositoryImp.deleteGuarantor(1L, 2L)).thenReturn(flowOf(response))

        viewModel.deleteGuarantor(1L, 2L)
        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }
        flowOf(GuarantorUiState.GuarantorDeletedSuccessfully("deleted")).test {
            assertEquals(GuarantorUiState.GuarantorDeletedSuccessfully("deleted"), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testDeleteGuarantor_Unsuccessful() = runBlocking {
        val error = RuntimeException("Error")
        `when`(guarantorRepositoryImp.deleteGuarantor(1L, 2L)).thenThrow(error)

        viewModel.deleteGuarantor(1L, 2L)

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