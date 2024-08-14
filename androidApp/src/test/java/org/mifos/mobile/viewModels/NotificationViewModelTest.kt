package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.repositories.NotificationRepository
import org.mifos.mobile.ui.notification.NotificationViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.NotificationUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class NotificationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var notificationRepositoryImp: NotificationRepository

    private lateinit var notificationViewModel: NotificationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationViewModel = NotificationViewModel(notificationRepositoryImp)
    }

    @Test
    fun testLoadNotifications_NotificationsSuccessfullyReceivedFromRepository_ReturnsNotificationsSuccessfully() =
        runTest {
            val dummyNotifications = listOf(MifosNotification(), MifosNotification())
            `when`(notificationRepositoryImp.loadNotifications()).thenReturn(
                flowOf(
                    dummyNotifications
                )
            )
            notificationViewModel.notificationUiState.test {
                notificationViewModel.loadNotifications()
                assertEquals(NotificationUiState.Initial, awaitItem())
                assertEquals(NotificationUiState.Loading, awaitItem())
                assertEquals(NotificationUiState.LoadNotificationsSuccessful(dummyNotifications), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
}

    @Test(expected = Exception::class)
    fun testLoadNotifications_NotificationsNotReceivedFromRepository_ReturnsError() = runTest {
        `when`(notificationRepositoryImp.loadNotifications()).thenThrow(Exception("Dummy error"))
        notificationViewModel.notificationUiState.test {
            notificationViewModel.loadNotifications()
            assertEquals(NotificationUiState.Initial, awaitItem())
            assertEquals(NotificationUiState.Loading, awaitItem())
            assertEquals(NotificationUiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
    }
}