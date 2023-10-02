package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.repositories.TransferRepositoryImp
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.TransferUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class TransferProcessViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var transferProcessImp: TransferRepositoryImp

    @Mock
    lateinit var transferUiStateObserver: Observer<TransferUiState>

    private lateinit var viewModel: TransferProcessViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = TransferProcessViewModel(transferProcessImp)
        viewModel.transferUiState.observeForever(transferUiStateObserver)
    }

    @Test
    fun makeTransfer_successful() = runBlocking {
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
        ).thenReturn(Response.success(responseBody))

        viewModel.makeTransfer(
            1, 2, 3,
            4, 5, 6, 7,
            8, "06 July 2023 ", 100.0, "Transfer",
            "dd MMMM yyyy", "en", "0000001", "0000002",
            TransferType.SELF
        )
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Loading)
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.TransferSuccess)
        Mockito.verifyNoMoreInteractions(transferUiStateObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun makeTransfer_unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error = RuntimeException("Savings Transfer Failed")
        Mockito.`when`(
            transferProcessImp.makeTransfer(
                1, 2, 3,
                4, 5, 6, 7,
                8, "06 July 2023 ", 100.0, "Transfer",
                "dd MMMM yyyy", "en", "0000001", "0000002",
                TransferType.SELF
            )
        ).thenReturn(Response.error(404, "error".toResponseBody(null)))

        viewModel.makeTransfer(
            1, 2, 3,
            4, 5, 6, 7,
            8, "06 July 2023 ", 100.0, "Transfer",
            "dd MMMM yyyy", "en", "0000001", "0000002",
            TransferType.SELF
        )
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Loading)
        Mockito.verifyNoMoreInteractions(transferUiStateObserver)
        Dispatchers.resetMain()
    }

    @After
    fun tearDown() {
        viewModel.transferUiState.removeObserver(transferUiStateObserver)
    }

}