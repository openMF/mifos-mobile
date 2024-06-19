package org.mifos.mobile.viewModels

import CoroutineTestRule
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.ui.beneficiary_application.BeneficiaryApplicationViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class BeneficiaryApplicationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp

    @Mock
    lateinit var beneficiaryUiStateObserver: Observer<BeneficiaryUiState>

    private lateinit var viewModel: BeneficiaryApplicationViewModel

    // Test coroutine dispatcher and scope
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = BeneficiaryApplicationViewModel(beneficiaryRepositoryImp)
    }

    @Test
    fun testLoadBeneficiaryTemplate_Successful() = runTest {
        val response = mock(BeneficiaryTemplate::class.java)
        `when`(beneficiaryRepositoryImp.beneficiaryTemplate()).thenReturn(flowOf(response))
         
        viewModel.beneficiaryUiState.test {
            viewModel.loadBeneficiaryTemplate()
            assertEquals(BeneficiaryUiState.Initial, awaitItem())
            assertEquals(BeneficiaryUiState.Loading, awaitItem())
            assertEquals(
                BeneficiaryUiState.ShowBeneficiaryTemplate(response),
                awaitItem()
            )
            assertEquals(
                BeneficiaryUiState.SetVisibility(View.VISIBLE),
                awaitItem()
            )
            verifyNoMoreInteractions(beneficiaryUiStateObserver)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun TestScope.obserrveUiState(): MutableList<BeneficiaryUiState> {
        val uiStates = mutableListOf<BeneficiaryUiState>()
        viewModel.beneficiaryUiState.onEach {
            println(it)
            uiStates.add(it)
        }
            .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        return uiStates
    }

    @Test(expected =  Exception::class)
    fun testLoadBeneficiaryTemplate_Unsuccessful() = runTest {
        val exception =  Exception("Test exception")
        `when`(beneficiaryRepositoryImp.beneficiaryTemplate()).thenThrow(exception as Throwable)

        viewModel.beneficiaryUiState.test {
              viewModel.loadBeneficiaryTemplate()
                assertEquals(BeneficiaryUiState.Initial, awaitItem())
                assertEquals(BeneficiaryUiState.Loading, awaitItem())
            assertEquals(
                    BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiary_template),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testCreateBeneficiary_Successful() = runTest {
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)
        `when`(beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)).thenReturn(
            flowOf(
                ResponseBody.create(null, "success")
            )
        )

        viewModel.beneficiaryUiState.test {
            viewModel.createBeneficiary(beneficiaryPayload)
            assertEquals(BeneficiaryUiState.Initial, awaitItem())
            assertEquals(BeneficiaryUiState.Loading, awaitItem())
            assertEquals(BeneficiaryUiState.CreatedSuccessfully, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected =  Exception::class)
    fun testCreateBeneficiary_Unsuccessful() = runTest {
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)
        `when`(beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload))
            .thenThrow( Exception("Error Response"))
         
        viewModel.beneficiaryUiState.test {
                viewModel.createBeneficiary(beneficiaryPayload)
                assertEquals(BeneficiaryUiState.Initial, awaitItem())
                assertEquals(BeneficiaryUiState.Loading, awaitItem())
                assertEquals(
                    BeneficiaryUiState.ShowError(R.string.error_creating_beneficiary),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun testUpdateBeneficiary_Successful() = runTest {
        val response = mock(ResponseBody::class.java)
        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)
        `when`(
            beneficiaryRepositoryImp.updateBeneficiary(
                123L,
                beneficiaryUpdatePayload
            )
        ).thenReturn(
            flowOf(response)
        )
         
        viewModel.beneficiaryUiState.test {

            viewModel.updateBeneficiary(123L, beneficiaryUpdatePayload)

            assertEquals(BeneficiaryUiState.Initial, awaitItem())
            assertEquals(BeneficiaryUiState.Loading, awaitItem())
            assertEquals(BeneficiaryUiState.UpdatedSuccessfully, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected =  Exception::class)
    fun testUpdateBeneficiary_Unsuccessful() = runTest {
        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)
        `when`(
            beneficiaryRepositoryImp.updateBeneficiary(
                123L,
                beneficiaryUpdatePayload
            )
        ).thenThrow( Exception("Error updating beneficiary") as Throwable)
         
        viewModel.beneficiaryUiState.test {
                viewModel.updateBeneficiary(123L, beneficiaryUpdatePayload)
                assertEquals(BeneficiaryUiState.Initial, awaitItem())
                assertEquals(BeneficiaryUiState.Loading, awaitItem())
              assertEquals(
                    BeneficiaryUiState.ShowError(R.string.error_updating_beneficiary),
                    awaitItem()
                )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
//        viewModel.beneficiaryUiState.removeObserver(beneficiaryUiStateObserver)
    }

}