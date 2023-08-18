package org.mifos.mobile.repositories

import CoroutineTestRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
    fun testClientAccounts_Successful() = runBlocking {
        val mockClientAccounts: ClientAccounts = mock(ClientAccounts::class.java)

        `when`(dataManager.clientAccounts()).thenReturn(mockClientAccounts)

        val flow = homeRepositoryImp.clientAccounts()

        flow.collect { result ->
            assertEquals(mockClientAccounts, result)
        }
    }

    @Test
    fun testCurrentClient_Successful() = runBlocking {
        val mockClient: Client = mock(Client::class.java)

        `when`(dataManager.currentClient()).thenReturn(mockClient)

        val flow = homeRepositoryImp.currentClient()

        flow.collect { result ->
            assertEquals(mockClient, result)
        }
    }

    @Test
    fun testClientImage_Successful() = runBlocking {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)

        `when`(dataManager.clientImage()).thenReturn(mockResponseBody)

        val flow = homeRepositoryImp.clientImage()

        flow.collect { result ->
            assertEquals(mockResponseBody, result)
        }
    }

    @Test
    fun testUnreadNotificationsCount_Successful() = runBlocking {
        val mockUnreadCount = 5

        `when`(dataManager.unreadNotificationsCount()).thenReturn(mockUnreadCount)

        val flow = homeRepositoryImp.unreadNotificationsCount()

        flow.collect { result ->
            assertEquals(mockUnreadCount, result)
        }
    }

    @Test
    fun testClientAccounts_Error() = runBlocking {
        val errorMessage = "Failed to fetch client accounts"
        val mockErrorResponse: ClientAccounts = mock(ClientAccounts::class.java)

        `when`(dataManager.clientAccounts()).thenReturn(mockErrorResponse)

        val flow = homeRepositoryImp.clientAccounts()

        try {
            flow.collect {
                fail("Expected an exception")
            }
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun testCurrentClient_Error() = runBlocking {
        val errorMessage = "Failed to fetch current client"
        val mockErrorResponse: Client = mock(Client::class.java)

        `when`(dataManager.currentClient()).thenReturn(mockErrorResponse)

        val flow = homeRepositoryImp.currentClient()

        try {
            flow.collect {
                fail("Expected an exception")
            }
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun testClientImage_Error() = runBlocking {
        val errorMessage = "Failed to fetch client image"
        val mockErrorResponse: ResponseBody = mock(ResponseBody::class.java)

        `when`(dataManager.clientImage()).thenReturn(mockErrorResponse)

        val flow = homeRepositoryImp.clientImage()

        try {
            flow.collect {
                fail("Expected an exception")
            }
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun testUnreadNotificationsCount_Error() = runBlocking {
        val errorMessage = "Failed to fetch unread notifications count"

        `when`(dataManager.unreadNotificationsCount()).thenReturn(502)

        val flow = homeRepositoryImp.unreadNotificationsCount()

        try {
            flow.collect {
                fail("Expected an exception")
            }
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
    }
}