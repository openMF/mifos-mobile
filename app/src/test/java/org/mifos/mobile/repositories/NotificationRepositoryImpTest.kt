package org.mifos.mobile.repositories

import CoroutineTestRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
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
    fun testLoadNotifications_SuccessResponseReceivedFromDataManager_ReturnsSuccess() = runTest {
        val notification = mock(MifosNotification::class.java)
        val notificationList = List(5){ notification}
        Mockito.`when`(
            dataManager.notifications()
        ).thenReturn(notificationList)

        val notifications = notificationRepositoryImp.loadNotifications()

        notifications.test {
            assertEquals(notificationList, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testLoadNotifications_ErrorResponseReceivedFromDataManager_ReturnsError() = runTest {
        val dummyError = Exception("Dummy error")
        `when`(dataManager.notifications()).thenThrow(dummyError)

        val notifications = notificationRepositoryImp.loadNotifications()

        notifications.test {
            assert(Throwable("Dummy error") == awaitItem())
        }
    }

}