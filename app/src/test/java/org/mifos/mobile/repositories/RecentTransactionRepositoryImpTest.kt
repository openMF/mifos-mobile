package org.mifos.mobile.repositories

import CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
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
    fun recentTransaction_successful_response_from_dataManger() = runBlocking {
        val success: Response<Page<Transaction?>?> =
            Response.success(Mockito.mock(Page<Transaction?>()::class.java))
        val offset = 0
        val limit = 50

        Mockito.`when`(dataManager.getRecentTransactions(offset, limit)).thenReturn(success)

        val result = recentTransactionRepositoryImp.recentTransactions(offset, limit)

        Mockito.verify(dataManager).getRecentTransactions(offset, limit)
        Assert.assertEquals(result, success)
    }

    @Test
    fun recentTransaction_unsuccessful_response_from_dataManger() = runBlocking {
        val error: Response<Page<Transaction?>?> =
            Response.error(404, "error".toResponseBody(null))
        val offset = 0
        val limit = 50

        Mockito.`when`(dataManager.getRecentTransactions(offset, limit)).thenReturn(error)

        val result = recentTransactionRepositoryImp.recentTransactions(offset, limit)

        Mockito.verify(dataManager).getRecentTransactions(offset, limit)
        Assert.assertEquals(result, error)
    }

}