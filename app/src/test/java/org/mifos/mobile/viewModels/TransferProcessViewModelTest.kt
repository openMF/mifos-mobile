package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.repositories.TransferRepositoryImp
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.TransferUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import org.mifos.mobile.ui.transfer_process.TransferProcessViewModel

@RunWith(MockitoJUnitRunner::class)
class TransferProcessViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var transferProcessImp: TransferRepositoryImp

    private lateinit var viewModel: TransferProcessViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = TransferProcessViewModel(transferProcessImp)
    }

    @Test
    fun makeTransfer_successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            transferProcessImp.makeTransfer(
                1, 2, 3,
                4, 5, 6, 7,
                8, "06 July 2023 ", 100.0, "Transfer",
                "dd MMMM yyyy", "en", "0000001", "0000002",
                TransferType.SELF
            )
        ).thenReturn(flowOf(responseBody))
        viewModel.transferUiState.test {
            viewModel.makeTransfer(
                TransferPayload(
                    1, 2, 3,
                    4, 5, 6, 7,
                    8, "06 July 2023 ", 100.0, "Transfer",
                    "dd MMMM yyyy", "en", "0000001", "0000002"
                )
            )
            assertEquals(TransferUiState.Initial, awaitItem())
            assertEquals(TransferUiState.TransferSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun makeTransfer_unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error = Exception("Savings Transfer Failed")
        Mockito.`when`(
            transferProcessImp.makeTransfer(
                1, 2, 3,
                4, 5, 6, 7,
                8, "06 July 2023 ", 100.0, "Transfer",
                "dd MMMM yyyy", "en", "0000001", "0000002",
                TransferType.SELF
            )
        ).thenThrow(error)
        viewModel.transferUiState.test {

            viewModel.makeTransfer(
                TransferPayload(
                    1, 2, 3,
                    4, 5, 6, 7,
                    8, "06 July 2023 ", 100.0, "Transfer",
                    "dd MMMM yyyy", "en", "0000001", "0000002"
                )
            )
            assertEquals(TransferUiState.Initial, awaitItem())
            assertEquals(TransferUiState.Error(error), awaitItem())
            cancelAndIgnoreRemainingEvents()
            Dispatchers.resetMain()
        }
    }

    @After
    fun tearDown() {
    }

}
