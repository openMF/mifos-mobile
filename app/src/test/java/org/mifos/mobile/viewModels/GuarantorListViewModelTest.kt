package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.repositories.GuarantorRepositoryImp
import org.mifos.mobile.ui.guarantor.guarantor_list.GuarantorListViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.GuarantorUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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
    fun testGetGuarantorList_Successful() = runTest {
        val list1 = mock(GuarantorPayload::class.java)
        val list2 = mock(GuarantorPayload::class.java)
        val list = listOf(list1, list2)

        `when`(guarantorRepositoryImp.getGuarantorList(1L)).thenReturn(flowOf(list))
         viewModel.guarantorUiState.test {
            viewModel.getGuarantorList(1L)
            assertEquals(GuarantorUiState.Loading, awaitItem())
            assertEquals(GuarantorUiState.ShowGuarantorListSuccessfully(list), awaitItem())
             cancelAndIgnoreRemainingEvents()
         }
    }

    @Test(expected = Exception::class)
    fun testGetGuarantorList_Unsuccessful() = runTest {
        val error = Exception("Error")
        `when`(guarantorRepositoryImp.getGuarantorList(1L)).thenThrow(error)
        viewModel.guarantorUiState.test {
            viewModel.getGuarantorList(1L)
            assertEquals(GuarantorUiState.Loading, awaitItem())
            assertEquals(GuarantorUiState.ShowError(error.message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}