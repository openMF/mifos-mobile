package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.repositories.ClientChargeRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.ClientChargeUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ClientChargeViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var clientChargeRepositoryImp: ClientChargeRepositoryImp

    @Mock
    private lateinit var clientChargeUiStateObserver: Observer<ClientChargeUiState>

    private lateinit var viewModel: ClientChargeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = ClientChargeViewModel(clientChargeRepositoryImp)
        viewModel.clientChargeUiState.observeForever(clientChargeUiStateObserver)
    }

    @Test
    fun testLoadClientCharges_Successful() = runBlocking {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val mockChargePage = Page(1, listOf(charge1, charge2))

        `when`(clientChargeRepositoryImp.getClientCharges(123L)).thenReturn(
            Response.success(mockChargePage)
        )

        viewModel.loadClientCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(
            ClientChargeUiState.ShowClientCharges(
                mockChargePage.pageItems
            )
        )
        verifyNoMoreInteractions(clientChargeUiStateObserver)
    }

    @Test
    fun testLoadClientCharges_Unsuccessful() = runBlocking {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getClientCharges(123L)).thenReturn(
            Response.error(404, "error".toResponseBody(null))
        )

        viewModel.loadClientCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(
            ClientChargeUiState.ShowError(
                errorMessageResId
            )
        )
    }

    @Test
    fun testLoadLoanAccountCharges_Successful() = runBlocking {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val list = listOf(charge1, charge2)

        `when`(clientChargeRepositoryImp.getLoanCharges(123L)).thenReturn(Response.success(list))

        viewModel.loadLoanAccountCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.ShowClientCharges(list))
        verifyNoMoreInteractions(clientChargeUiStateObserver)
    }

    @Test
    fun testLoadLoanAccountCharges_Unsuccessful() = runBlocking {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getLoanCharges(123L)).thenReturn(
            Response.error(404, "error".toResponseBody(null))
        )

        viewModel.loadLoanAccountCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(
            ClientChargeUiState.ShowError(
                errorMessageResId
            )
        )
    }

    @Test
    fun testLoadSavingsAccountCharges_Successful() = runBlocking {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val list = listOf(charge1, charge2)

        `when`(clientChargeRepositoryImp.getSavingsCharges(123L)).thenReturn(Response.success(list))

        viewModel.loadSavingsAccountCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.ShowClientCharges(list))
        verifyNoMoreInteractions(clientChargeUiStateObserver)
    }

    @Test
    fun testLoadSavingsAccountCharges_Unsuccessful() = runBlocking {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getSavingsCharges(123L)).thenReturn(
            Response.error(404, "error".toResponseBody(null))
        )

        viewModel.loadSavingsAccountCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(
            ClientChargeUiState.ShowError(
                errorMessageResId
            )
        )
    }

    @Test
    fun loadClientLocalCharges_Successful() = runBlocking {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val mockChargePage = Page(1, listOf(charge1, charge2))

        `when`(clientChargeRepositoryImp.clientLocalCharges()).thenReturn(
            Response.success(
                mockChargePage
            )
        )

        viewModel.loadClientLocalCharges()

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(
            ClientChargeUiState.ShowClientCharges(
                mockChargePage.pageItems
            )
        )
        verifyNoMoreInteractions(clientChargeUiStateObserver)
    }

    @Test
    fun loadClientLocalCharges_Unsuccessful() = runBlocking {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.clientLocalCharges()).thenReturn(
            Response.error(404, "error".toResponseBody(null))
        )

        viewModel.loadClientLocalCharges()

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(
            ClientChargeUiState.ShowError(
                errorMessageResId
            )
        )
    }

    @After
    fun tearDown() {
        viewModel.clientChargeUiState.removeObserver(clientChargeUiStateObserver)
    }

}