package org.mifos.mobile.repositories

import CoroutineTestRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.util.checkForUnsuccessfulOperation
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
    fun loadAccounts_Success() = runTest {
        val mockAccountType = "savings"
        val mockClientAccounts = mock(ClientAccounts::class.java)
        `when`(dataManager.getAccounts(mockAccountType)).thenReturn((mockClientAccounts))

        val resultFlow = accountsRepositoryImp.loadAccounts(mockAccountType)
        resultFlow.test {
            assertEquals(mockClientAccounts, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun loadAccounts_Error() = runTest {
        val mockAccountType = "savings"
        `when`(dataManager.getAccounts(mockAccountType))
            .thenThrow(Exception("Error occurred"))
        val result = accountsRepositoryImp.loadAccounts(mockAccountType)
        result.test {
            assert(Throwable("Error occurred") == awaitError())
        }
    }

}