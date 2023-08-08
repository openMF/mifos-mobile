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
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.repositories.GuarantorRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.GuarantorUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class GuarantorListViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var guarantorRepositoryImp: GuarantorRepositoryImp

    @Mock
    lateinit var guarantorUiStateObserver: Observer<GuarantorUiState>

    private lateinit var viewModel: GuarantorListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = GuarantorListViewModel(guarantorRepositoryImp)
        viewModel.guarantorUiState.observeForever(guarantorUiStateObserver)
    }

    @Test
    fun testGetGuarantorList_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val list1 = mock(GuarantorPayload::class.java)
        val list2 = mock(GuarantorPayload::class.java)
        val list = listOf(list1, list2)

        `when`(guarantorRepositoryImp.getGuarantorList(1L)).thenReturn(Response.success(list))

        viewModel.getGuarantorList(1L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        verify(guarantorUiStateObserver).onChanged(
            GuarantorUiState.ShowGuarantorListSuccessfully(
                list
            )
        )
        Dispatchers.resetMain()
    }

    @Test
    fun testGetGuarantorList_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error: Response<List<GuarantorPayload?>?> =
            Response.error(404, ResponseBody.create(null, "error"))

        `when`(guarantorRepositoryImp.getGuarantorList(1L)).thenReturn(error)

        viewModel.getGuarantorList(1L)
        verify(guarantorUiStateObserver).onChanged(GuarantorUiState.Loading)
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
        viewModel.guarantorUiState.removeObserver(guarantorUiStateObserver)
    }

}