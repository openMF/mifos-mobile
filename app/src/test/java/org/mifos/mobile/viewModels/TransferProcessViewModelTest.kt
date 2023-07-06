package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.repositories.TransferRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.TransferUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

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
    fun makeSavingsTransfer_transfer_successful() {
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            transferProcessImp.makeTransfer(
                Mockito.anyInt(),Mockito.anyLong(),Mockito.anyInt(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyLong(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),
                Mockito.anyDouble(),Mockito.anyString(),Mockito.anyString(),
                Mockito.anyString(),Mockito.anyString(),Mockito.anyString()
            )
        ).thenReturn(Observable.just(responseBody))

        viewModel.makeSavingsTransfer(
            1,2,3,
            4,5,6,7,
            8,"06 July 2023 ",100.0,"Transfer",
            "dd MMMM yyyy", "en", "0000001","0000002"
        )
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Loading)
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.TransferSuccess)
        Mockito.verifyNoMoreInteractions(transferUiStateObserver)
    }

    @Test
    fun makeSavings_transfer_unsuccessful() {
        val error = RuntimeException("Registration Failed")
        Mockito.`when`(
            transferProcessImp.makeTransfer(
                Mockito.anyInt(),Mockito.anyLong(),Mockito.anyInt(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyLong(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),
                Mockito.anyDouble(),Mockito.anyString(),Mockito.anyString(),
                Mockito.anyString(),Mockito.anyString(),Mockito.anyString()
            )
        ).thenReturn(Observable.error(error))

        viewModel.makeSavingsTransfer(
            1,2,3,
            4,5,6,7,
            8,"06 July 2023 ",100.0,"Transfer",
            "dd MMMM yyyy", "en", "0000001","0000002"
        )
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Loading)
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Error(error))
        Mockito.verifyNoMoreInteractions(transferUiStateObserver)
    }

    @Test
    fun makeTPTTransfer_transfer_successful() {
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            transferProcessImp.makeTransfer(
                Mockito.anyInt(),Mockito.anyLong(),Mockito.anyInt(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyLong(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),
                Mockito.anyDouble(),Mockito.anyString(),Mockito.anyString(),
                Mockito.anyString(),Mockito.anyString(),Mockito.anyString()
            )
        ).thenReturn(Observable.just(responseBody))

        viewModel.makeTPTTransfer(
            1,2,3,
            4,5,6,7,
            8,"06 July 2023 ",100.0,"Transfer",
            "dd MMMM yyyy", "en", "0000001","0000002"
        )

        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Loading)
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.TransferSuccess)
        Mockito.verifyNoMoreInteractions(transferUiStateObserver)
    }

    @Test
    fun makeTPTTransfer_transfer_unsuccessful() {
        val error = RuntimeException("Registration Failed")
        Mockito.`when`(
            transferProcessImp.makeTransfer(
                Mockito.anyInt(),Mockito.anyLong(),Mockito.anyInt(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyLong(),
                Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),
                Mockito.anyDouble(),Mockito.anyString(),Mockito.anyString(),
                Mockito.anyString(),Mockito.anyString(),Mockito.anyString()
            )
        ).thenReturn(Observable.error(error))

        viewModel.makeTPTTransfer(
            1,2,3,
            4,5,6,7,
            8,"06 July 2023 ",100.0,"Transfer",
            "dd MMMM yyyy", "en", "0000001","0000002"
        )

        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Loading)
        Mockito.verify(transferUiStateObserver).onChanged(TransferUiState.Error(error))
        Mockito.verifyNoMoreInteractions(transferUiStateObserver)
    }

    @After
    fun tearDown() {
        viewModel.transferUiState.removeObserver(transferUiStateObserver)
    }

}