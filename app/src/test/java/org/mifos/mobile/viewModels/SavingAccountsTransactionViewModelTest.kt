package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SavingAccountsTransactionViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var savingsAccountRepositoryImp: SavingsAccountRepository

    private lateinit var savingAccountsTransactionViewModel: SavingAccountsTransactionViewModel
    private val mockAccountId = 1L
    private val mockAssociationType = Constants.TRANSACTIONS
    private val mockSavingsWithAssociations = FakeRemoteDataSource.savingsWithAssociations

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingAccountsTransactionViewModel =
            SavingAccountsTransactionViewModel(savingsAccountRepositoryImp)

    }

    @Test
    fun testLoadSavingsWithAssociations_SuccessReceivedFromRepository_ReturnSuccessLoadingSavingsWithAssociations() =
        runTest{
            Mockito.`when`(
                savingsAccountRepositoryImp.getSavingsWithAssociations(
                    mockAccountId,
                    mockAssociationType
                )
            ).thenReturn(flowOf(mockSavingsWithAssociations))
            savingAccountsTransactionViewModel.savingAccountsTransactionUiState.test {
                savingAccountsTransactionViewModel.loadSavingsWithAssociations(mockAccountId)
                assertEquals(SavingsAccountUiState.Initial,awaitItem())
                assertEquals(SavingsAccountUiState.Loading,awaitItem())
                assertEquals(SavingsAccountUiState.SuccessLoadingSavingsWithAssociations(mockSavingsWithAssociations),awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test(expected = Exception::class)
    fun testLoadSavingsWithAssociations_ErrorResponseFromRepository_ReturnsError() = runTest{
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                mockAccountId,
                mockAssociationType
            )
        ).thenThrow(Exception("Error occurred while fetching data from server"))
        savingAccountsTransactionViewModel.savingAccountsTransactionUiState.test {
            savingAccountsTransactionViewModel.loadSavingsWithAssociations(mockAccountId)
            assertEquals(SavingsAccountUiState.Initial,awaitItem())
            assertEquals(SavingsAccountUiState.Loading,awaitItem())
            assertEquals(SavingsAccountUiState.Error,awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {

    }
}