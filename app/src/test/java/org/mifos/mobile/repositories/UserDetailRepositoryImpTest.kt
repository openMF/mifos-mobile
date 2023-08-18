package org.mifos.mobile.repositories

import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class UserDetailRepositoryImpTest {

    @Mock
    private lateinit var mockDataManager: DataManager

    private lateinit var userDetailRepository: UserDetailRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userDetailRepository = UserDetailRepositoryImp(mockDataManager)
    }

    @Test
    fun testRegisterNotification_Success() = runBlocking {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        `when`(mockDataManager.registerNotification(mockPayload)).thenReturn(mockResponseBody)

        val flow = userDetailRepository.registerNotification(mockPayload)

        flow.collect { result ->
            assertEquals(mockResponseBody, result)
        }
    }

    @Test
    fun testRegisterNotification_Error() = runBlocking {
        val errorMessage = "Failed to register notification"
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        `when`(mockDataManager.registerNotification(mockPayload)).thenThrow(
            RuntimeException(
                errorMessage
            )
        )

        val flow = userDetailRepository.registerNotification(mockPayload)

        try {
            flow.collect {
                fail("Expected an exception")
            }
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun testGetUserNotificationId_Success() = runBlocking {
        val mockNotificationUserDetail: NotificationUserDetail =
            mock(NotificationUserDetail::class.java)
        val mockId = 123L
        `when`(mockDataManager.getUserNotificationId(mockId)).thenReturn(mockNotificationUserDetail)

        val flow = userDetailRepository.getUserNotificationId(mockId)

        flow.collect { result ->
            assertEquals(mockNotificationUserDetail, result)
        }
    }

    @Test
    fun testGetUserNotificationId_Error() = runBlocking {
        val errorMessage = "Failed to get user notification"
        val mockId = 123L
        `when`(mockDataManager.getUserNotificationId(mockId)).thenThrow(
            RuntimeException(
                errorMessage
            )
        )

        val flow = userDetailRepository.getUserNotificationId(mockId)

        try {
            flow.collect {
                fail("Expected an exception")
            }
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun testUpdateRegisterNotification_Success() = runBlocking {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        val mockId = 123L
        `when`(mockDataManager.updateRegisterNotification(mockId, mockPayload)).thenReturn(
            mockResponseBody
        )

        val flow = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        flow.collect { result ->
            assertEquals(mockResponseBody, result)
        }
    }

    @Test
    fun testUpdateRegisterNotificationError() = runBlocking {
        val errorMessage = "Failed to update register notification"
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        val mockId = 123L
        `when`(mockDataManager.updateRegisterNotification(mockId, mockPayload)).thenThrow(
            RuntimeException(errorMessage)
        )

        val flow = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        try {
            flow.collect {
                fail("Expected an exception")
            }
        } catch (e: Exception) {
            assertEquals(errorMessage, e.message)
        }
    }

}
