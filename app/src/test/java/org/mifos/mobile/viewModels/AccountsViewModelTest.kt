package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repositories.AccountsRepository
import org.mifos.mobile.core.data.repositories.HomeRepositoryImp
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.feature.account.utils.AccountState
import org.mifos.mobile.feature.account.viewmodel.AccountsViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class AccountsViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var accountsRepositoryImp: AccountsRepository

    @Mock
    lateinit var homeRepositoryImp: HomeRepositoryImp

    private lateinit var mockClientAccounts: ClientAccounts
    private lateinit var accountsViewModel: AccountsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accountsViewModel = org.mifos.mobile.feature.account.viewmodel.AccountsViewModel(
            accountsRepositoryImp,
            homeRepositoryImp
        )
        mockClientAccounts = Mockito.mock(ClientAccounts::class.java)
    }

    @Test
    fun loadClientAccounts_Success() = runTest {
        val mockClientAccounts = mock(ClientAccounts::class.java)
        `when`(homeRepositoryImp.clientAccounts()).thenReturn(flowOf(mockClientAccounts))
        accountsViewModel.accountsUiState.test {
            accountsViewModel.loadClientAccounts()
            assertEquals(AccountState.Loading, awaitItem())
            assertEquals(
                AccountState.ShowSavingsAccounts(mockClientAccounts.savingsAccounts), awaitItem()
            )
            assertEquals(
                AccountState.ShowLoanAccounts(mockClientAccounts.loanAccounts), awaitItem()
            )
            assertEquals(
                AccountState.ShowShareAccounts(mockClientAccounts.shareAccounts), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadAccountsUiState_Success() = runTest {
        val mockAccountType = "savingsAccounts"
        val mockClientAccounts = mock(ClientAccounts::class.java)
        `when`(accountsRepositoryImp.loadAccounts(mockAccountType)).thenReturn(
            flowOf(
                mockClientAccounts
            )
        )
        accountsViewModel.accountsUiState.test {
            accountsViewModel.loadAccounts(mockAccountType)
            assertEquals(AccountState.Loading, awaitItem())
            assertEquals(
                AccountState.ShowSavingsAccounts(mockClientAccounts.savingsAccounts), awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = RuntimeException::class)
    fun loadAccounts_Error() = runTest {
        val mockAccountType = "savings"
        `when`(accountsRepositoryImp.loadAccounts(anyString())).thenThrow(RuntimeException())
        accountsViewModel.accountsUiState.test {
            try {
                accountsViewModel.loadAccounts(mockAccountType)
                assertEquals(AccountState.Loading, awaitItem())
            } catch (e: RuntimeException) {
                assertEquals(AccountState.Error, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

}