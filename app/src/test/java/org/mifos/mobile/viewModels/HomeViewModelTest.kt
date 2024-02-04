package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
import org.mifos.mobile.ui.home.HomeViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.HomeUiState
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

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(homeRepositoryImp)
        viewModel.preferencesHelper = mockPreferencesHelper
    }

    @Test
    fun testLoadingClientAccountDetails_Success(): Unit = runBlocking {
        val mockLoanAccounts = listOf(mock(LoanAccount::class.java))
        val mockSavingsAccounts = listOf(mock(SavingAccount::class.java))
        val expectedLoanBalance = 100.0
        val expectedSavingBalance = 100.0

        `when`(homeRepositoryImp.clientAccounts()).thenReturn(flowOf(mock(ClientAccounts::class.java)))

        viewModel.loadClientAccountDetails()

        viewModel.homeUiState.collect { value ->
            assertEquals(
                HomeUiState.ClientAccountDetails(
                    expectedLoanBalance,
                    expectedSavingBalance
                ), value
            )
        }
    }

    @Test
    fun testLoadingClientAccountDetails_Error(): Unit = runBlocking {
        val errorMessageResId = R.string.error_fetching_accounts

        `when`(homeRepositoryImp.clientAccounts()).thenThrow(RuntimeException())

        viewModel.loadClientAccountDetails()

        viewModel.homeUiState.collect { value ->
            assertTrue(value is HomeUiState.Error)
            assertEquals(errorMessageResId, (value as HomeUiState.Error))
        }
    }

    @Test
    fun testLoadingUserDetails_Success(): Unit = runBlocking {
        val mockClient = mock(Client::class.java)

        `when`(homeRepositoryImp.currentClient()).thenReturn(flowOf(mockClient))

        viewModel.userDetails

        viewModel.homeUiState.collect { value ->
            assertTrue(value is HomeUiState.UserDetails)
            assertEquals(mockClient, (value as HomeUiState.UserDetails).client)
        }
    }

    @Test
    fun testLoadingUserDetails_Error(): Unit = runBlocking {
        val errorMessageResId = R.string.error_fetching_client

        `when`(homeRepositoryImp.currentClient()).thenThrow(RuntimeException())

        viewModel.userDetails

        viewModel.homeUiState.collect { value ->
            assertTrue(value is HomeUiState.Error)
            assertEquals(errorMessageResId, (value as HomeUiState.Error))
        }
    }

    @Test
    fun testLoadingUnreadNotificationsCount_Success(): Unit = runBlocking {
        val mockUnreadCount = 5

        `when`(homeRepositoryImp.unreadNotificationsCount()).thenReturn(flowOf(mockUnreadCount))

        viewModel.unreadNotificationsCount

        viewModel.homeUiState.collect { value ->
            assertTrue(value is HomeUiState.UnreadNotificationsCount)
            assertEquals(mockUnreadCount, (value as HomeUiState.UnreadNotificationsCount).count)
        }
    }

    @Test
    fun testLoadingUnreadNotificationsCount_Error(): Unit = runBlocking {
        `when`(homeRepositoryImp.unreadNotificationsCount()).thenThrow(RuntimeException())

        viewModel.unreadNotificationsCount

        viewModel.homeUiState.collect { value ->
            assertTrue(value is HomeUiState.UnreadNotificationsCount)
            assertEquals(0, (value as HomeUiState.UnreadNotificationsCount).count)
        }
    }
}