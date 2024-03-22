package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class BeneficiaryListViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp


    private lateinit var viewModel: BeneficiaryListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BeneficiaryListViewModel(beneficiaryRepositoryImp)
    }



    @Test
    fun testLoadBeneficiaries_Successful() = runTest {
        val list1 = mock(Beneficiary::class.java)
        val list2 = mock(Beneficiary::class.java)
        val list = listOf(list1, list2)

        `when`(beneficiaryRepositoryImp.beneficiaryList()).thenReturn(flowOf(list))
        viewModel.beneficiaryUiState.test {
            viewModel.loadBeneficiaries()
            assertEquals(BeneficiaryUiState.Initial, awaitItem())
            assertEquals(BeneficiaryUiState.Loading, awaitItem())
            assertEquals(BeneficiaryUiState.ShowBeneficiaryList(list), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testLoadBeneficiaries_Unsuccessful() = runTest {
        `when`(beneficiaryRepositoryImp.beneficiaryList()).thenThrow(Exception("Error occurred"))
        viewModel.beneficiaryUiState.test {
            viewModel.loadBeneficiaries()
            assertEquals(BeneficiaryUiState.Loading, awaitItem())
            assertEquals(BeneficiaryUiState.ShowError(R.string.beneficiaries), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {

    }
}