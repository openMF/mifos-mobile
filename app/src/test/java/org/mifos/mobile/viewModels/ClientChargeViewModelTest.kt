package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.repositories.ClientChargeRepositoryImp
import org.mifos.mobile.ui.client_charge.ClientChargeViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.ClientChargeUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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
    }

    @Test
    fun testLoadClientCharges_Successful() = runTest {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val mockChargePage = Page(1, listOf(charge1, charge2))

        `when`(clientChargeRepositoryImp.getClientCharges(123L)).thenReturn(
            flowOf(mockChargePage)
        )
        
        viewModel.clientChargeUiState.test {
            viewModel.loadClientCharges(123L)
            assertEquals(ClientChargeUiState.Initial, awaitItem())
            assertEquals(ClientChargeUiState.Loading,awaitItem())
            assertEquals(
                ClientChargeUiState.ShowClientCharges(
                    mockChargePage.pageItems
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Throwable::class)
    fun testLoadClientCharges_Unsuccessful() = runTest {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getClientCharges(123L))
            .thenThrow(throwable)
        
        viewModel.clientChargeUiState.test {
            viewModel.loadClientCharges(123L)
            assertEquals(ClientChargeUiState.Loading, awaitItem())
            assertEquals(
                ClientChargeUiState.ShowError(
                    errorMessageResId
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testLoadLoanAccountCharges_Successful() = runTest {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val list = listOf(charge1, charge2)

        `when`(clientChargeRepositoryImp.getLoanCharges(123L))
            .thenReturn(flowOf(list))
        
        viewModel.clientChargeUiState.test {
            viewModel.loadLoanAccountCharges(123L)
            assertEquals(ClientChargeUiState.Initial, awaitItem())
            assertEquals(ClientChargeUiState.Loading, awaitItem())
            assertEquals(
                ClientChargeUiState.ShowClientCharges(
                    list
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test(expected = Throwable::class)
    fun testLoadLoanAccountCharges_Unsuccessful() = runTest {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")
        
        `when`(clientChargeRepositoryImp.getLoanCharges(123L))
            .thenThrow(throwable)
        viewModel.clientChargeUiState.test {
            viewModel.loadLoanAccountCharges(123L)

            assertEquals(ClientChargeUiState.Loading, awaitItem())
            assertEquals(
                ClientChargeUiState.ShowError(
                    errorMessageResId
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testLoadSavingsAccountCharges_Successful() = runTest {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val list = listOf(charge1, charge2)

        `when`(clientChargeRepositoryImp.getSavingsCharges(123L))
            .thenReturn(flowOf(list))
        
        viewModel.clientChargeUiState.test {
            viewModel.loadSavingsAccountCharges(123L)
            assertEquals(ClientChargeUiState.Initial, awaitItem())
            assertEquals(ClientChargeUiState.Loading, awaitItem())
            assertEquals(
                ClientChargeUiState.ShowClientCharges(
                    list
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Throwable::class)
    fun testLoadSavingsAccountCharges_Unsuccessful() = runTest {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")

        `when`(clientChargeRepositoryImp.getSavingsCharges(123L))
            .thenThrow(throwable)
        
        viewModel.clientChargeUiState.test {
            viewModel.loadSavingsAccountCharges(123L)
            assertEquals(ClientChargeUiState.Loading, awaitItem())
            assertEquals(
                ClientChargeUiState.ShowError(
                    errorMessageResId
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadClientLocalCharges_Successful() = runTest {
        val charge1 = mock(Charge::class.java)
        val charge2 = mock(Charge::class.java)
        val mockChargePage = Page(1, listOf(charge1, charge2))

        `when`(clientChargeRepositoryImp.clientLocalCharges()).thenReturn(
            flowOf(mockChargePage)
        )
        
        viewModel.clientChargeUiState.test {
            viewModel.loadClientLocalCharges()
            assertEquals(ClientChargeUiState.Initial, awaitItem())
            assertEquals(ClientChargeUiState.Loading, awaitItem())
            assertEquals(
                ClientChargeUiState.ShowClientCharges(
                    mockChargePage.pageItems
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test(expected = Exception::class)
    fun loadClientLocalCharges_Unsuccessful() = runTest {
        val errorMessageResId = R.string.client_charges
        val throwable = Throwable("Error occurred")
        `when`(clientChargeRepositoryImp.clientLocalCharges()).thenThrow(throwable)
        
        viewModel.clientChargeUiState.test {
            viewModel.loadClientLocalCharges()
            assertEquals(ClientChargeUiState.Loading, awaitItem())
            assertEquals(
                ClientChargeUiState.ShowError(
                    errorMessageResId
                ), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
    }

}