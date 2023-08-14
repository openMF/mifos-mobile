package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.After
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
import retrofit2.Response

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
        viewModel.guarantorUiState.observeForever(guarantorUiStateObserver)
    }

    @Test
    fun testDeleteGuarantor_Successful() = runBlocking {
        val response = mock(ResponseBody::class.java)

        `when`(
            guarantorRepositoryImp.deleteGuarantor(
                1L,
                2L
            )
        ).thenReturn(Response.success(response))

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
        val error: Response<ResponseBody?> = Response.error(404, ResponseBody.create(null, "error"))

        `when`(guarantorRepositoryImp.deleteGuarantor(1L, 2L)).thenReturn(error)

        viewModel.deleteGuarantor(1L, 2L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
    }

    @After
    fun tearDown() {
        viewModel.guarantorUiState.removeObserver(guarantorUiStateObserver)
    }

}