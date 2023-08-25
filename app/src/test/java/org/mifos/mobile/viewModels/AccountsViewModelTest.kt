package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.repositories.AccountsRepository
import org.mifos.mobile.repositories.HomeRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.AccountsUiState
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
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
        accountsViewModel = AccountsViewModel(accountsRepositoryImp, homeRepositoryImp)
        mockClientAccounts = Mockito.mock(ClientAccounts::class.java)
    }

    @Test
    fun loadClientAccounts_Success() =
        runTest {
            val mockClientAccounts = mock(ClientAccounts::class.java)
            `when`(homeRepositoryImp.clientAccounts()).thenReturn(flowOf(mockClientAccounts))

            accountsViewModel.loadClientAccounts()

            accountsViewModel.accountsUiState.test {
                assertEquals(
                    AccountsUiState.ShowShareAccounts(mockClientAccounts.shareAccounts),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(homeRepositoryImp).clientAccounts()
        }

    @Test
    fun loadAccountsUiState_Success(): Unit = runBlocking {
        val mockAccountType = "savings"
        val mockClientAccounts = mock(ClientAccounts::class.java)
        `when`(accountsRepositoryImp.loadAccounts(anyString())).thenReturn(flowOf(mockClientAccounts))

        accountsViewModel.loadAccounts(mockAccountType)

        accountsViewModel.accountsUiState.test {
            assertEquals(
                AccountsUiState.Loading, awaitItem()
            )
        }

        flowOf(mockClientAccounts.savingsAccounts).test {
            assertEquals(mockClientAccounts.savingsAccounts, awaitItem())
            awaitComplete()
        }

        flowOf(mockClientAccounts.shareAccounts).test {
            assertEquals(mockClientAccounts.shareAccounts, awaitItem())
            awaitComplete()
        }

        flowOf(mockClientAccounts.loanAccounts).test {
            assertEquals(mockClientAccounts.loanAccounts, awaitItem())
            awaitComplete()
        }

        verify(accountsRepositoryImp).loadAccounts(mockAccountType)

    }

    @Test
    fun loadAccounts_Error(): Unit = runBlocking {
        `when`(accountsRepositoryImp.loadAccounts(anyString())).thenThrow(RuntimeException("error"))

        accountsViewModel.accountsUiState.test {
            assertEquals(
                AccountsUiState.Loading, awaitItem()
            )
        }

        flowOf(AccountsUiState.Error).test {
            assertEquals(AccountsUiState.Error, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

}