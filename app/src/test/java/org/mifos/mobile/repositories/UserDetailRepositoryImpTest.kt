package org.mifos.mobile.repositories

import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserDetailRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var userDetailRepositoryImp: UserDetailRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userDetailRepositoryImp = UserDetailRepositoryImp(dataManager)
    }

    @Test
    fun testRegisterNotification_Successful() {
        val success: Observable<ResponseBody?> = Observable.just(mock(ResponseBody::class.java))
        val payload = NotificationRegisterPayload(123L, "Token")

        `when`(dataManager.registerNotification(payload)).thenReturn(success)

        val result = userDetailRepositoryImp.registerNotification(payload)

        assertEquals(result, success)
    }

    @Test
    fun testRegisterNotification_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Register Notification Failed"))
        val payload = NotificationRegisterPayload(123L, "Token")

        `when`(dataManager.registerNotification(payload)).thenReturn(error)

        val result = userDetailRepositoryImp.registerNotification(payload)
        assertEquals(result, error)

    }

    @Test
    fun testGetUserNotificationId_Successful() {
        val success: Observable<NotificationUserDetail?> =
            Observable.just(mock(NotificationUserDetail::class.java))

        `when`(dataManager.getUserNotificationId(123L)).thenReturn(success)

        val result = userDetailRepositoryImp.getUserNotificationId(123L)

        assertEquals(result, success)
    }

    @Test
    fun testGetUserNotificationId_Unsuccessful() {
        val error: Observable<NotificationUserDetail?> =
            Observable.error(Throwable("Unable to get user notification"))

        `when`(dataManager.getUserNotificationId(123L)).thenReturn(error)

        val result = userDetailRepositoryImp.getUserNotificationId(123L)

        assertEquals(result, error)
    }

    @Test
    fun testUpdateRegisterNotification_Successful() {
        val success: Observable<ResponseBody?> = Observable.just(mock(ResponseBody::class.java))
        val payload = NotificationRegisterPayload(123L, "Token")

        `when`(dataManager.updateRegisterNotification(123L, payload)).thenReturn(success)

        val result = userDetailRepositoryImp.updateRegisterNotification(123L, payload)

        assertEquals(success, result)
    }

    @Test
    fun testUpdateRegisterNotification_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Updating Register Notification Failed"))
        val payload = NotificationRegisterPayload(123L, "Token")

        `when`(dataManager.updateRegisterNotification(123L, payload)).thenReturn(error)

        val result = userDetailRepositoryImp.updateRegisterNotification(123L, payload)

        assertEquals(result, error)
    }

}