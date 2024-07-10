package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.repositories.HomeRepositoryImp
import org.mifos.mobile.feature.home.viewmodels.HomeState
import org.mifos.mobile.feature.home.viewmodels.HomeUiState
import org.mifos.mobile.feature.home.viewmodels.HomeViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var homeRepositoryImp: HomeRepositoryImp

    @Mock
    private lateinit var mockPreferencesHelper: PreferencesHelper
    

    private lateinit var viewModel: org.mifos.mobile.feature.home.viewmodels.HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = org.mifos.mobile.feature.home.viewmodels.HomeViewModel(homeRepositoryImp)
        viewModel.preferencesHelper = mockPreferencesHelper
    }

    @Test
    fun testLoadingClientAccountDetails_Success(): Unit = runTest{
        val mockLoanAccounts = listOf(
            mock(LoanAccount::class.java).apply {
                `when`(loanBalance).thenReturn(100.0)
            }
        )
        val mockSavingsAccounts = listOf(
            mock(SavingAccount::class.java).apply {
                `when`(accountBalance).thenReturn(100.0)
            }
        )
        val expectedLoanBalance = 100.0
        val expectedSavingBalance = 100.0
        val mockClient = ClientAccounts(mockLoanAccounts, mockSavingsAccounts)

        `when`(homeRepositoryImp.clientAccounts()).thenReturn(flowOf(mockClient))

        viewModel.loadClientAccountDetails()

        assertEquals(viewModel.homeUiState.value,
            org.mifos.mobile.feature.home.viewmodels.HomeUiState.Success(
                org.mifos.mobile.feature.home.viewmodels.HomeState(
                    loanAmount = expectedLoanBalance,
                    savingsAmount = expectedSavingBalance
                )
            )
            )

    }

    @Test(expected = Exception::class)
    fun testLoadingClientAccountDetails_Error(): Unit = runTest{
        val errorMessageResId = R.string.error_fetching_accounts

        `when`(homeRepositoryImp.clientAccounts()).thenThrow( Exception())

        viewModel.loadClientAccountDetails()

        assertTrue(viewModel.homeUiState.value is org.mifos.mobile.feature.home.viewmodels.HomeUiState.Error)
        assertEquals(errorMessageResId, (viewModel.homeUiState.value as org.mifos.mobile.feature.home.viewmodels.HomeUiState.Error).errorMessage)
    }

    @Test
    fun testLoadingUserDetails_Success(): Unit = runTest{
        val mockClient = mock(Client::class.java)

        `when`(homeRepositoryImp.currentClient()).thenReturn(flowOf(mockClient))

        viewModel.getUserDetails()
        assertTrue(viewModel.homeUiState.value is org.mifos.mobile.feature.home.viewmodels.HomeUiState.Success)
        assertEquals(mockClient.officeName,
            (viewModel.homeUiState.value as org.mifos.mobile.feature.home.viewmodels.HomeUiState.Success).homeState.username)
    }

    @Test(expected = Exception::class)
    fun testLoadingUserDetails_Error(): Unit = runTest{
        val errorMessageResId = R.string.error_fetching_client

        `when`(homeRepositoryImp.currentClient()).thenThrow( Exception())

        viewModel.getUserDetails()
            assertTrue(viewModel.homeUiState.value is org.mifos.mobile.feature.home.viewmodels.HomeUiState.Error)
            assertEquals(errorMessageResId, org.mifos.mobile.feature.home.viewmodels.HomeUiState.Error(R.string.error_fetching_client))

    }

    @Test
    fun testLoadingUnreadNotificationsCount_Success(): Unit = runTest{
        val mockUnreadCount = 5

        `when`(homeRepositoryImp.unreadNotificationsCount()).thenReturn(flowOf(mockUnreadCount))

        viewModel.unreadNotificationsCount

        assertEquals(mockUnreadCount,viewModel.notificationsCount.value)

    }

    @Test(expected = Exception::class)
    fun testLoadingUnreadNotificationsCount_Error(): Unit = runTest{
        `when`(homeRepositoryImp.unreadNotificationsCount()).
        thenThrow( Exception("Error message "))

        viewModel.unreadNotificationsCount

       assertEquals(0, viewModel.notificationsCount.value)
    }
}