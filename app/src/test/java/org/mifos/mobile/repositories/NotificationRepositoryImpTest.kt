package org.mifos.mobile.repositories

import CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class NotificationRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var notificationRepositoryImp: NotificationRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationRepositoryImp = NotificationRepositoryImp(dataManager)
    }

    @Test
    fun testLoadNotifications_SuccessResponseReceivedFromDataManager_ReturnsSuccess() =
        runBlocking {
            val notificationList: List<MifosNotification?> = ArrayList()
            Mockito.`when`(
                dataManager.notifications()
            ).thenReturn(flowOf(notificationList))

            val notifications = notificationRepositoryImp.loadNotifications()

            notifications.collect { result ->
                assert(result == notificationList)
            }
        }

    @Test
    fun testLoadNotifications_ErrorResponseReceivedFromDataManager_ReturnsError(): Unit =
        runBlocking {
            val dummyError = RuntimeException("Dummy error")
            `when`(dataManager.notifications()).thenThrow(dummyError)

            kotlin.runCatching {
                notificationRepositoryImp.loadNotifications()
            }.exceptionOrNull()
        }
}