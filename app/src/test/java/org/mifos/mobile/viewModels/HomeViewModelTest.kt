package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.repositories.HomeRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.HomeUiState
import org.mifos.mobile.utils.UserDetailUiState
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
    fun testLoadingClientAccountDetails_Success() = runBlocking {
        val expectedLoanBalance = 100.0
        val expectedSavingBalance = 100.0

        `when`(homeRepositoryImp.clientAccounts()).thenReturn(flowOf(mock(ClientAccounts::class.java)))

        viewModel.loadClientAccountDetails()

        flowOf(HomeUiState.Loading).test {
            assertEquals(HomeUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(
            HomeUiState.ClientAccountDetails(
                expectedLoanBalance, expectedSavingBalance
            )
        ).test {
            assertEquals(
                HomeUiState.ClientAccountDetails(
                    expectedLoanBalance,
                    expectedSavingBalance
                ), awaitItem()
            )
            awaitComplete()
        }
    }

    @Test
    fun testLoadingClientAccountDetails_Error(): Unit = runBlocking {
        val errorMessageResId = R.string.error_fetching_accounts

        `when`(homeRepositoryImp.clientAccounts()).thenThrow(RuntimeException("error"))

        viewModel.loadClientAccountDetails()

        flowOf(HomeUiState.Loading).test {
            assertEquals(HomeUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(HomeUiState.Error(errorMessageResId)).test {
            assertEquals(HomeUiState.Error(errorMessageResId), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testLoadingUserDetails_Success(): Unit = runBlocking {
        val mockClient = mock(Client::class.java)

        `when`(homeRepositoryImp.currentClient()).thenReturn(flowOf(mockClient))

        viewModel.userDetails

        flowOf(UserDetailUiState.Loading).test {
            assertEquals(UserDetailUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(UserDetailUiState.ShowUserDetails(mockClient)).test {
            assertEquals(UserDetailUiState.ShowUserDetails(mockClient), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testLoadingUserDetails_Error(): Unit = runBlocking {
        val errorMessageResId = R.string.error_fetching_client

        `when`(homeRepositoryImp.currentClient()).thenThrow(RuntimeException())

        viewModel.userDetails

        flowOf(UserDetailUiState.Loading).test {
            assertEquals(UserDetailUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(HomeUiState.Error(errorMessageResId)).test {
            assertEquals(HomeUiState.Error(errorMessageResId), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testLoadingUnreadNotificationsCount_Success() = runBlocking {
        val mockUnreadCount = 5

        `when`(homeRepositoryImp.unreadNotificationsCount()).thenReturn(flowOf(mockUnreadCount))

        viewModel.unreadNotificationsCount

        flowOf(HomeUiState.Loading).test {
            assertEquals(HomeUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(HomeUiState.UnreadNotificationsCount(mockUnreadCount)).test {
            assertEquals(HomeUiState.UnreadNotificationsCount(mockUnreadCount), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testLoadingUnreadNotificationsCount_Error(): Unit = runBlocking {
        `when`(homeRepositoryImp.unreadNotificationsCount()).thenThrow(RuntimeException("error"))

        viewModel.unreadNotificationsCount

        flowOf(HomeUiState.Loading).test {
            assertEquals(HomeUiState.Loading, awaitItem())
            awaitComplete()
        }

        flowOf(HomeUiState.Error(0)).test {
            assertEquals(HomeUiState.Error(0), awaitItem())
            awaitComplete()
        }
    }
}