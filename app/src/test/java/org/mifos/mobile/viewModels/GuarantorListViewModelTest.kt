package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GuarantorListViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var guarantorRepositoryImp: GuarantorRepositoryImp

    private lateinit var viewModel: GuarantorListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = GuarantorListViewModel(guarantorRepositoryImp)
    }

    @Test
    fun testGetGuarantorList_Successful() = runBlocking {
        val list1 = mock(GuarantorPayload::class.java)
        val list2 = mock(GuarantorPayload::class.java)
        val list = listOf(list1, list2)

        `when`(guarantorRepositoryImp.getGuarantorList(1L)).thenReturn(flowOf(list))

        viewModel.getGuarantorList(1L)
        flowOf(GuarantorUiState.Loading).test {
            assertEquals(GuarantorUiState.Loading, awaitItem())
            awaitComplete()
        }
        flowOf(list).test {
            assertEquals(list,awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testGetGuarantorList_Unsuccessful() = runBlocking {
        val error = RuntimeException("Error")
        `when`(guarantorRepositoryImp.getGuarantorList(1L)).thenThrow(error)

        viewModel.getGuarantorList(1L)

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