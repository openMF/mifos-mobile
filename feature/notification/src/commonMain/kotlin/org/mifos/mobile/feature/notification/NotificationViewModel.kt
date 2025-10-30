/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.MifosNotification
import org.mifos.mobile.feature.notification.NotificationUiState.Loading

/**
 * The ViewModel for the Notification screen.
 *
 * This class is responsible for the business logic of the Notification screen. It fetches
 * notifications from the [NotificationRepository], manages the UI state, and handles user
 * interactions like refreshing the list and dismissing notifications. It's also aware of the
 * network status, thanks to [NetworkMonitor].
 *
 * @param notificationRepositoryImp The repository that provides access to notification data.
 * @param networkMonitor A utility to monitor the device's network connectivity.
 */
internal class NotificationViewModel(
    private val notificationRepositoryImp: NotificationRepository,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _notificationUiState = MutableStateFlow<NotificationUiState>(Loading)
    val notificationUiState: StateFlow<NotificationUiState> get() = _notificationUiState

    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing

    init {
        viewModelScope.launch {
            notificationRepositoryImp.deleteOldNotifications()
            loadNotifications()
        }
    }

    /**
     * Kicks off the process of loading notifications from the repository. This function updates the
     * UI state to reflect the current status of the operation, such as Loading, Success, or Error.
     */
    fun loadNotifications() {
        _notificationUiState.value = Loading
        viewModelScope.launch {
            notificationRepositoryImp.loadNotifications()
                .catch {
                    _isRefreshing.emit(false)
                    _notificationUiState.value =
                        NotificationUiState.Error(errorMessage = it.message)
                }.collect { notifications ->
                    when (notifications) {
                        is DataState.Error -> {
                            _notificationUiState.value =
                                NotificationUiState.Error(notifications.message)
                        }
                        DataState.Loading -> {
                            Loading
                        }
                        is DataState.Success -> {
                            _isRefreshing.emit(false)
                            _notificationUiState.value = if (notifications.data.isEmpty()) {
                                NotificationUiState.Empty
                            } else {
                                NotificationUiState.Success(notifications = sortNotifications(notifications.data))
                            }
                        }
                    }
                }
        }
    }

    /**
     * Initiates a refresh of the notifications. This is typically triggered by a user action, like
     * a pull-to-refresh gesture. It sets the refreshing state and then calls [loadNotifications]
     * to fetch the latest data.
     */
    fun refreshNotifications() {
        _isRefreshing.value = true
        loadNotifications()
    }

    /**
     * Marks a specific notification as read. This involves updating the notification's state in
     * the local repository to ensure the change is persisted.
     *
     * @param notification The [MifosNotification] to be marked as read.
     */
    fun dismissNotification(notification: MifosNotification) {
        viewModelScope.launch {
            notificationRepositoryImp.saveNotification(notification.copy(read = true))
            notificationRepositoryImp.updateReadStatus(notification, true)
        }
    }

    /**
     * Sorts a list of notifications. The sorting logic prioritizes unread notifications and then
     * sorts them by timestamp, so the most recent ones appear first.
     *
     * @param notifications The list of [MifosNotification]s to be sorted.
     * @return A new list containing the sorted notifications.
     */
    private fun sortNotifications(notifications: List<MifosNotification>): List<MifosNotification> {
        return notifications.sortedWith(
            compareByDescending<MifosNotification> { !it.isRead() }
                .thenByDescending { it.timeStamp },
        )
    }
}

/**
 * A sealed interface that represents the various states the Notification screen can be in. This
 * allows for exhaustive state handling in the UI, ensuring a predictable user experience.
 */
internal sealed interface NotificationUiState {
    /**
     * Indicates that the notifications are currently being loaded. This is the ideal time to show
     * a progress indicator.
     */
    data object Loading : NotificationUiState

    /**
     * Represents a successful fetch of notifications.
     *
     * @param notifications The list of notifications to be displayed on the screen.
     */
    data class Success(val notifications: List<MifosNotification>) : NotificationUiState

    /**
     * Signals that an error occurred while trying to fetch notifications.
     *
     * @param errorMessage A descriptive message about the error that can be shown to the user.
     */
    data class Error(val errorMessage: String?) : NotificationUiState

    /**
     * Used when the notification list is empty. This state allows for showing a user-friendly
     * message indicating that there are no notifications.
     */
    data object Empty : NotificationUiState
}
