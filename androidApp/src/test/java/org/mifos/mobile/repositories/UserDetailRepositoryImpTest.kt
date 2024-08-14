package org.mifos.mobile.repositories

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
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
    fun testRegisterNotification_Success() = runTest {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        `when`(mockDataManager.registerNotification(mockPayload)).thenReturn(mockResponseBody)

        val flow = userDetailRepository.registerNotification(mockPayload)

        flow.test {
            assertEquals(mockResponseBody, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testRegisterNotification_Error() = runTest {
        val errorMessage = "Failed to register notification"
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        `when`(mockDataManager.registerNotification(mockPayload)).thenThrow(
             Exception(
                errorMessage
            )
        )

        val flow = userDetailRepository.registerNotification(mockPayload)

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testGetUserNotificationId_Success() = runTest {
        val mockNotificationUserDetail: NotificationUserDetail =
            mock(NotificationUserDetail::class.java)
        val mockId = 123L
        `when`(mockDataManager.getUserNotificationId(mockId)).thenReturn(mockNotificationUserDetail)

        val flow = userDetailRepository.getUserNotificationId(mockId)

        flow.test {
            assertEquals(mockNotificationUserDetail, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testGetUserNotificationId_Error() = runTest {
        val errorMessage = "Failed to get user notification"
        val mockId = 123L
        `when`(mockDataManager.getUserNotificationId(mockId)).thenThrow(
             Exception(
                errorMessage
            )
        )

        val flow = userDetailRepository.getUserNotificationId(mockId)

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUpdateRegisterNotification_Success() = runTest {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        val mockId = 123L
        `when`(mockDataManager.updateRegisterNotification(mockId, mockPayload)).thenReturn(
            mockResponseBody
        )

        val flow = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        flow.test {
            assertEquals(mockResponseBody, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testUpdateRegisterNotificationError() = runTest {
        val errorMessage = "Failed to update register notification"
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        val mockId = 123L
        `when`(mockDataManager.updateRegisterNotification(mockId, mockPayload)).thenThrow(
             Exception(errorMessage)
        )

        val flow = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }

    }
}
