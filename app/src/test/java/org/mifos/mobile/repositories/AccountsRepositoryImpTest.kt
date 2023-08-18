package org.mifos.mobile.repositories

import CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.ClientAccounts
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class AccountsRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var accountsRepositoryImp: AccountsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accountsRepositoryImp = AccountsRepositoryImp(dataManager)
    }

    @Test
    fun loadAccounts_Success() = runBlocking {
        val mockAccountType = "savings"
        val mockClientAccounts = mock(ClientAccounts::class.java)
        `when`(dataManager.getAccounts(mockAccountType)).thenReturn((mockClientAccounts))

        val resultFlow = accountsRepositoryImp.loadAccounts(mockAccountType)
        val resultAccounts = resultFlow.first()

        assert(resultAccounts == mockClientAccounts)
    }

    @Test
    fun loadAccounts_Error() = runBlocking {
        val mockAccountType = "savings"
        val mockError = IOException("Network error")
        `when`(dataManager.getAccounts(mockAccountType)).thenThrow(mockError)

        val resultFlow = accountsRepositoryImp.loadAccounts(mockAccountType)
        var isErrorThrown = false
        try {
            resultFlow.first()
        } catch (e: Exception) {
            isErrorThrown = true
            assert(e is IOException)
        }
        assert(isErrorThrown)
    }
}