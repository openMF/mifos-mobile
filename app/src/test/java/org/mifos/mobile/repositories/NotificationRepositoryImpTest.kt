package org.mifos.mobile.repositories

import io.reactivex.Observable

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NotificationRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var notificationRepositoryImp: NotificationRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationRepositoryImp = NotificationRepositoryImp(dataManager)
    }

    @Test
    fun testLoadNotifications_SuccessResponseReceivedFromDataManager_ReturnsSuccess() {
        val notificationList : List<MifosNotification?> = ArrayList()
        val successResponse: Observable<List<MifosNotification?>?> =
            Observable.just(notificationList)
        Mockito.`when`(
            dataManager.notifications
        ).thenReturn(successResponse)

        val result = notificationRepositoryImp.loadNotifications()

        Mockito.verify(dataManager).notifications
        Assert.assertEquals(result, successResponse)

    }

    @Test
    fun testLoadNotifications_ErrorResponseReceivedFromDataManager_ReturnsError() {
        val errorResponse: Observable<List<MifosNotification?>?> =
            Observable.error(Throwable("LoadNotifications Unsuccessful"))
        Mockito.`when`(
            dataManager.notifications
        ).thenReturn(errorResponse)

        val result = notificationRepositoryImp.loadNotifications()

        Mockito.verify(dataManager).notifications
        Assert.assertEquals(result, errorResponse)
    }

}