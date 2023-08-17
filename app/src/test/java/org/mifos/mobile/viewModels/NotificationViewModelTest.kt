package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.repositories.NotificationRepository
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

    @Mock
    lateinit var notificationUiStateObserver: Observer<NotificationUiState>

    private lateinit var notificationViewModel: NotificationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationViewModel = NotificationViewModel(notificationRepositoryImp)
        notificationViewModel.notificationUiState.observeForever(notificationUiStateObserver)
    }

    @Test
    fun testLoadNotifications_NotificationsSuccessfullyReceivedFromRepository_ReturnsNotificationsSuccessfully() =
        runBlocking {
            val dummyNotifications = listOf(MifosNotification(), MifosNotification())
            `when`(notificationRepositoryImp.loadNotifications()).thenReturn(
                flowOf(
                    dummyNotifications
                )
            )
            val observer = mock<Observer<NotificationUiState>>()
            notificationViewModel.notificationUiState.observeForever(observer)

            notificationViewModel.loadNotifications()

            verify(observer).onChanged(
                NotificationUiState.LoadNotificationsSuccessful(
                    dummyNotifications
                )
            )
            verifyNoMoreInteractions(notificationUiStateObserver)
        }

    @Test
    fun testLoadNotifications_NotificationsNotReceivedFromRepository_ReturnsError() = runBlocking {
        `when`(notificationRepositoryImp.loadNotifications()).thenThrow(Exception("Dummy error"))
        val observer = mock<Observer<NotificationUiState>>()
        notificationViewModel.notificationUiState.observeForever(observer)

        notificationViewModel.loadNotifications()

        verify(observer).onChanged(NotificationUiState.Error)
        verifyNoMoreInteractions(notificationUiStateObserver)
    }

    @After
    fun tearDown() {
        notificationViewModel.notificationUiState.removeObserver(notificationUiStateObserver)
    }
}