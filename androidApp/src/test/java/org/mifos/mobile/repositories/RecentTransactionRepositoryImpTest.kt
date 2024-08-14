package org.mifos.mobile.repositories

import CoroutineTestRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class RecentTransactionRepositoryImpTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var recentTransactionRepositoryImp: RecentTransactionRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        recentTransactionRepositoryImp = RecentTransactionRepositoryImp(dataManager)
    }

    @Test
    fun recentTransaction_successful_response_from_dataManger() = runTest {
        val success= mock(Page<Transaction>()::class.java)
        val offset = 0
        val limit = 50

        Mockito.`when`(dataManager.getRecentTransactions(offset, limit)).thenReturn(success)

        val result = recentTransactionRepositoryImp.recentTransactions(offset, limit)
        result.test {
            assertEquals(success, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).getRecentTransactions(offset, limit)
    }

    @Test(expected = Exception::class)
    fun recentTransaction_unsuccessful_response_from_dataManger() = runTest {
        val offset = 0
        val limit = 50

        Mockito.`when`(dataManager.getRecentTransactions(offset, limit))
            .thenThrow(Exception("Error Occured in fetching recent transactions"))

        val result = recentTransactionRepositoryImp.recentTransactions(offset, limit)
        result.test {
            assertEquals(Throwable("Error Occured in fetching recent transactions"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).getRecentTransactions(offset, limit)
    }

}