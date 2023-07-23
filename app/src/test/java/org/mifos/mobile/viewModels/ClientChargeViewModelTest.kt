package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.repositories.ClientChargeRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.ClientChargeUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mifos.mobile.R
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClientChargeViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

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
    fun testLoadClientCharges_Successful() {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val mockChargePage = Page(1, listOf(charge1, charge2))

        `when`(clientChargeRepositoryImp.getClientCharges(123L)).thenReturn(
            Observable.just(mockChargePage)
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
    fun testLoadClientCharges_Unsuccessful() {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getClientCharges(123L)).thenReturn(
            Observable.error(throwable)
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
    fun testLoadLoanAccountCharges_Successful() {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val list = listOf(charge1, charge2)

        `when`(clientChargeRepositoryImp.getLoanCharges(123L)).thenReturn(Observable.just(list))

        viewModel.loadLoanAccountCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.ShowClientCharges(list))
        verifyNoMoreInteractions(clientChargeUiStateObserver)
    }

    @Test
    fun testLoadLoanAccountCharges_Unsuccessful() {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getLoanCharges(123L)).thenReturn(
            Observable.error(throwable)
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
    fun testLoadSavingsAccountCharges_Successful() {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val list = listOf(charge1, charge2)

        `when`(clientChargeRepositoryImp.getSavingsCharges(123L)).thenReturn(Observable.just(list))

        viewModel.loadSavingsAccountCharges(123L)

        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.Loading)
        verify(clientChargeUiStateObserver).onChanged(ClientChargeUiState.ShowClientCharges(list))
        verifyNoMoreInteractions(clientChargeUiStateObserver)
    }

    @Test
    fun testLoadSavingsAccountCharges_Unsuccessful() {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getSavingsCharges(123L)).thenReturn(
            Observable.error(throwable)
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
    fun loadClientLocalCharges_Successful() {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val mockChargePage = Page(1, listOf(charge1, charge2))

        `when`(clientChargeRepositoryImp.clientLocalCharges()).thenReturn(
            Observable.just(
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
    fun loadClientLocalCharges_Unsuccessful() {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.clientLocalCharges()).thenReturn(
            Observable.error(throwable)
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