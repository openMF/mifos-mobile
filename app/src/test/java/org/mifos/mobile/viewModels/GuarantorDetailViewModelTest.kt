package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
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
import java.io.IOException

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

    @Mock
    lateinit var guarantorUiStateObserver: Observer<GuarantorUiState>

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
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(
            GuarantorUiState.GuarantorDeletedSuccessfully(
                response.string()
            )
        )
    }

    @Test
    fun testDeleteGuarantor_Unsuccessful() = runBlocking {
        val error = IOException("Error")
        `when`(guarantorRepositoryImp.deleteGuarantor(1L, 2L)).thenThrow(error)

        viewModel.deleteGuarantor(1L, 2L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.ShowError(Throwable().message))
    }

}