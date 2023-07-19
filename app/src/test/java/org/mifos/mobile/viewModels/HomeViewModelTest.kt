package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.repositories.HomeRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.HomeUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.client.Client
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var homeRepositoryImp: HomeRepositoryImp

    @Mock
    lateinit var preferencesHelper: PreferencesHelper

    @Mock
    lateinit var homeUiStateObserver: Observer<HomeUiState>

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(homeRepositoryImp)
        homeViewModel.preferencesHelper = preferencesHelper
        homeViewModel.homeUiState.observeForever(homeUiStateObserver)
    }

    @Test
    fun loadClientAccountDetails_Success() {
        val clientAccounts = mock(ClientAccounts::class.java)

        `when`(homeRepositoryImp.clientAccounts()).thenReturn(Observable.just(clientAccounts))

        homeViewModel.loadClientAccountDetails()

        val expectedUiState = HomeUiState.ClientAccountDetails(
            homeViewModel.getLoanAccountDetails(clientAccounts.loanAccounts),
            homeViewModel.getSavingAccountDetails(clientAccounts.savingsAccounts)
        )
        verify(homeUiStateObserver).onChanged(HomeUiState.Loading)
        assertEquals(expectedUiState, homeViewModel.homeUiState.value)
    }

    @Test
    fun loadClientAccountDetails_Unsuccessful() {
        `when`(homeRepositoryImp.clientAccounts()).thenReturn(Observable.error(Throwable()))
        homeViewModel.loadClientAccountDetails()
        verify(homeUiStateObserver).onChanged(HomeUiState.Loading)
        assertEquals(
            HomeUiState.Error(R.string.error_fetching_accounts),
            homeViewModel.homeUiState.value
        )
    }

    @Test
    fun userDetails_Success() {
        val client = mock(Client::class.java)
        `when`(homeRepositoryImp.currentClient()).thenReturn(Observable.just(client))
        homeViewModel.userDetails
        assertEquals(HomeUiState.UserDetails(client), homeViewModel.homeUiState.value)
    }

    @Test
    fun userDetails_Unsuccessful() {
        `when`(homeRepositoryImp.currentClient()).thenReturn(Observable.error(Throwable()))
        homeViewModel.userDetails
        assertEquals(
            HomeUiState.Error(R.string.error_fetching_client),
            homeViewModel.homeUiState.value
        )
    }

    @Test
    fun unreadNotificationsCount_Success() {
        val unreadCount = 5
        `when`(homeRepositoryImp.unreadNotificationsCount()).thenReturn(Observable.just(unreadCount))
        homeViewModel.unreadNotificationsCount
        assertEquals(
            HomeUiState.UnreadNotificationsCount(unreadCount),
            homeViewModel.homeUiState.value
        )
    }


    @Test
    fun unreadNotificationsCount_Unsuccessful() {
        `when`(homeRepositoryImp.unreadNotificationsCount()).thenReturn(Observable.error(Throwable()))
        homeViewModel.unreadNotificationsCount
        assertEquals(HomeUiState.UnreadNotificationsCount(0), homeViewModel.homeUiState.value)
    }

    @Test
    fun getLoanAccountDetails_ReturnsCorrectTotalAmount() {
        val loanAccount1 = mock(LoanAccount::class.java)
        `when`(loanAccount1.loanBalance).thenReturn(100.0)

        val loanAccount2 = mock(LoanAccount::class.java)
        `when`(loanAccount2.loanBalance).thenReturn(200.0)

        val loanAccountList = listOf(loanAccount1, loanAccount2)
        val result = homeViewModel.getLoanAccountDetails(loanAccountList)

        assertEquals(300.0, result, 0.001)
    }

    @Test
    fun getLoanAccountDetails_ReturnsInCorrectTotalAmount() {
        val loanAccount1 = mock(LoanAccount::class.java)
        `when`(loanAccount1.loanBalance).thenReturn(100.0)

        val loanAccount2 = mock(LoanAccount::class.java)
        `when`(loanAccount2.loanBalance).thenReturn(200.0)

        val loanAccountList = listOf(loanAccount1, loanAccount2)
        val result = homeViewModel.getLoanAccountDetails(loanAccountList)

        assertNotEquals(500.0, result, 0.001)
    }

    @Test
    fun getSavingsAccountDetails_ReturnsCorrectTotalAmount() {
        val savingAccount1 = mock(SavingAccount::class.java)
        `when`(savingAccount1.accountBalance).thenReturn(100.0)

        val savingAccount2 = mock(SavingAccount::class.java)
        `when`(savingAccount2.accountBalance).thenReturn(200.0)

        val savingAccountList = listOf(savingAccount1, savingAccount2)
        val result = homeViewModel.getSavingAccountDetails(savingAccountList)

        assertEquals(300.0, result, 0.001)
    }

    @Test
    fun getSavingsAccountDetails_ReturnsInCorrectTotalAmount() {
        val savingAccount1 = mock(SavingAccount::class.java)
        `when`(savingAccount1.accountBalance).thenReturn(100.0)

        val savingAccount2 = mock(SavingAccount::class.java)
        `when`(savingAccount2.accountBalance).thenReturn(200.0)

        val savingAccountList = listOf(savingAccount1, savingAccount2)
        val result = homeViewModel.getSavingAccountDetails(savingAccountList)

        assertNotEquals(800.0, result, 0.001)
    }

    @After
    fun tearDown() {
        homeViewModel.homeUiState.removeObserver(homeUiStateObserver)
    }

}