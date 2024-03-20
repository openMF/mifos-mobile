package org.mifos.mobile.repositories

import CoroutineTestRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var homeRepositoryImp: HomeRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeRepositoryImp = HomeRepositoryImp(dataManager)
    }

    @Test
    fun testClientAccounts_Successful() = runTest {
        val mockClientAccounts: ClientAccounts = mock(ClientAccounts::class.java)

        `when`(dataManager.clientAccounts()).thenReturn(mockClientAccounts)

        val flow = homeRepositoryImp.clientAccounts()

        flow.test {
            assertEquals(mockClientAccounts, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testCurrentClient_Successful() = runTest {
        val mockClient: Client = mock(Client::class.java)

        `when`(dataManager.currentClient()).thenReturn(mockClient)

        val flow = homeRepositoryImp.currentClient()

        flow.test {
            assertEquals(mockClient, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testClientImage_Successful() = runTest {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)

        `when`(dataManager.clientImage()).thenReturn(mockResponseBody)

        val flow = homeRepositoryImp.clientImage()

        flow.test {
            assertEquals(mockResponseBody, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUnreadNotificationsCount_Successful() = runTest {
        val mockUnreadCount = 5

        `when`(dataManager.unreadNotificationsCount()).thenReturn(mockUnreadCount)

        val flow = homeRepositoryImp.unreadNotificationsCount()

        flow.test {
            assertEquals(mockUnreadCount, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testClientAccounts_Error() = runTest {
        val errorMessage = "Failed to fetch client accounts"
        `when`(dataManager.clientAccounts()).
        thenThrow(Exception(errorMessage))
        val flow = homeRepositoryImp.clientAccounts()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testCurrentClient_Error() = runTest {
        val errorMessage = "Failed to fetch current client"
        `when`(dataManager.currentClient())
            .thenThrow(Exception(errorMessage))

        val flow = homeRepositoryImp.currentClient()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testClientImage_Error() = runTest {
        val errorMessage = "Failed to fetch client image"
        `when`(dataManager.clientImage())
            .thenThrow(Exception(errorMessage))
        val flow = homeRepositoryImp.clientImage()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testUnreadNotificationsCount_Error() = runTest {
        val errorMessage = "Failed to fetch unread notifications count"

        `when`(dataManager.unreadNotificationsCount())
            .thenThrow(Exception(errorMessage))

        val flow = homeRepositoryImp.unreadNotificationsCount()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }
}