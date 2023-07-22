package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class NotificationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

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
    fun testLoadNotifications_NotificationsSuccessfullyReceivedFromRepository_ReturnsNotificationsSuccessfully() {
        val notificationsReceived: List<MifosNotification> = ArrayList()
        Mockito.`when`(
            notificationRepositoryImp.loadNotifications()
        ).thenReturn(Observable.just(notificationsReceived))

        notificationViewModel.loadNotifications()

        Mockito.verify(notificationUiStateObserver).onChanged(NotificationUiState.Loading)
        Mockito.verify(notificationUiStateObserver).onChanged(NotificationUiState.LoadNotificationsSuccessful(notificationsReceived))
        Mockito.verifyNoMoreInteractions(notificationUiStateObserver)
    }

    @Test
    fun testLoadNotifications_NotificationsNotReceivedFromRepository_ReturnsError() {
        val error = RuntimeException("loadNotifications Unsuccessful")
        Mockito.`when`(
            notificationRepositoryImp.loadNotifications()
        ).thenReturn(Observable.error(error))

        notificationViewModel.loadNotifications()

        Mockito.verify(notificationUiStateObserver).onChanged(NotificationUiState.Loading)
        Mockito.verify(notificationUiStateObserver).onChanged(NotificationUiState.Error)
        Mockito.verifyNoMoreInteractions(notificationUiStateObserver)

    }

    @After
    fun tearDown() {
        notificationViewModel.notificationUiState.removeObserver(notificationUiStateObserver)
    }
}