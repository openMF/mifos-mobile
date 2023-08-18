package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
        accountsViewModel = AccountsViewModel(accountsRepositoryImp, homeRepositoryImp)
        mockClientAccounts = Mockito.mock(ClientAccounts::class.java)
    }

    @Test
    fun loadClientAccounts_Success() =
        runBlocking {
            val mockClientAccounts = mock(ClientAccounts::class.java)
            `when`(homeRepositoryImp.clientAccounts()).thenReturn(flowOf(mockClientAccounts))

            accountsViewModel.loadClientAccounts()

            assertEquals(
                AccountsUiState.ShowSavingsAccounts(mockClientAccounts.savingsAccounts),
                accountsViewModel.accountsUiState.value
            )
            assertEquals(
                AccountsUiState.ShowLoanAccounts(mockClientAccounts.loanAccounts),
                accountsViewModel.accountsUiState.value
            )
            assertEquals(
                AccountsUiState.ShowShareAccounts(mockClientAccounts.shareAccounts),
                accountsViewModel.accountsUiState.value
            )
        }

    @Test
    fun loadAccountsUiState_Success() = runBlocking {
        val mockAccountType = "savings"
        val mockClientAccounts = mock(ClientAccounts::class.java)
        `when`(accountsRepositoryImp.loadAccounts(anyString())).thenReturn(flowOf(mockClientAccounts))

        accountsViewModel.loadAccounts(mockAccountType)

        assertEquals(
            AccountsUiState.ShowSavingsAccounts(mockClientAccounts.savingsAccounts),
            accountsViewModel.accountsUiState.value
        )

    }

    @Test
    fun loadAccounts_Error() = runBlocking {
        val mockAccountType = "savings"
        `when`(accountsRepositoryImp.loadAccounts(anyString())).thenThrow(RuntimeException())

        accountsViewModel.loadAccounts(mockAccountType)

        assertEquals(AccountsUiState.Error, accountsViewModel.accountsUiState.value)
    }

}