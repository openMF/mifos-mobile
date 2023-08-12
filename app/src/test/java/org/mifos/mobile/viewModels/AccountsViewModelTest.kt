package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.repositories.AccountsRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.AccountsUiState
import org.mifos.mobile.utils.Constants
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class AccountsViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var accountsRepositoryImp : AccountsRepository

    @Mock
    lateinit var accountsUiStateObserver : Observer<AccountsUiState>

    private lateinit var mockClientAccounts : ClientAccounts
    private lateinit var accountsViewModel : AccountsViewModel
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accountsViewModel = AccountsViewModel(accountsRepositoryImp)
        accountsViewModel.accountsUiState.observeForever(accountsUiStateObserver)
        mockClientAccounts = Mockito.mock(ClientAccounts::class.java)
    }

    @Test
    fun testLoadClientAccounts_ReceivesErrorFromRepository_ReturnsError() {
        val error = RuntimeException("Loading Failed")
        Mockito.`when`(
            accountsRepositoryImp.loadClientAccounts()
        ).thenReturn(Observable.error(error))

        accountsViewModel.loadClientAccounts()

        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Loading)
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Error)
        Mockito.verifyNoMoreInteractions(accountsUiStateObserver)
    }

    @Test
    fun testLoadClientAccounts_ReceivesClientAccountsFromRepository_ReturnsShowAllAccountTypes() {
        Mockito.`when`(
            accountsRepositoryImp.loadClientAccounts()
        ).thenReturn(Observable.just(mockClientAccounts))

        accountsViewModel.loadClientAccounts()

        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Loading)
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.ShowSavingsAccounts(mockClientAccounts.savingsAccounts))
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.ShowLoanAccounts(mockClientAccounts.loanAccounts))
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.ShowShareAccounts(mockClientAccounts.shareAccounts))
        Mockito.verifyNoMoreInteractions(accountsUiStateObserver)
    }

    @Test
    fun testLoadAccounts_ReceivesClientAccountsFromRepository_ReturnsShowSavingsAccounts() {
        val mockAccountType = Constants.SAVINGS_ACCOUNTS
        Mockito.`when`(
            accountsRepositoryImp.loadAccounts(mockAccountType)
        ).thenReturn(Observable.just(mockClientAccounts))

        accountsViewModel.loadAccounts(mockAccountType)

        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Loading)
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.ShowSavingsAccounts(mockClientAccounts.savingsAccounts))
        Mockito.verifyNoMoreInteractions(accountsUiStateObserver)
    }

    @Test
    fun testLoadAccounts_ReceivesClientAccountsFromRepository_ReturnsShowLoanAccounts() {
        val mockAccountType = Constants.LOAN_ACCOUNTS
        Mockito.`when`(
            accountsRepositoryImp.loadAccounts(mockAccountType)
        ).thenReturn(Observable.just(mockClientAccounts))

        accountsViewModel.loadAccounts(mockAccountType)

        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Loading)
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.ShowLoanAccounts(mockClientAccounts.loanAccounts))
        Mockito.verifyNoMoreInteractions(accountsUiStateObserver)

    }

    @Test
    fun testLoadAccounts_ReceivesClientAccountsFromRepository_ReturnsShowShareAccounts() {
        val mockAccountType = Constants.SHARE_ACCOUNTS
        Mockito.`when`(
            accountsRepositoryImp.loadAccounts(mockAccountType)
        ).thenReturn(Observable.just(mockClientAccounts))

        accountsViewModel.loadAccounts(mockAccountType)

        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Loading)
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.ShowShareAccounts(mockClientAccounts.shareAccounts))
        Mockito.verifyNoMoreInteractions(accountsUiStateObserver)
    }

    @Test
    fun testLoadAccounts_ReceivesErrorFromRepository_ReturnsError() {
        val mockAccountType = "mockAccountTypeString"
        val error = RuntimeException("Loading Failed")
        Mockito.`when`(
            accountsRepositoryImp.loadAccounts(mockAccountType)
        ).thenReturn(Observable.error(error))

        accountsViewModel.loadAccounts(mockAccountType)

        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Loading)
        Mockito.verify(accountsUiStateObserver).onChanged(AccountsUiState.Error)
        Mockito.verifyNoMoreInteractions(accountsUiStateObserver)
    }

    @After
    fun tearDown() {
        accountsViewModel.accountsUiState.removeObserver(accountsUiStateObserver)
    }
}