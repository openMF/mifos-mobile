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
 * ViewModel for the Notification screen.
 *
 * This ViewModel is responsible for loading notifications, handling refreshing,
 * and managing the UI state of the notification screen. It interacts with the
 * [NotificationRepository] to fetch and update notification data. It also
 * monitors network availability via [NetworkMonitor].
 *
 * @param notificationRepositoryImp The repository for accessing notification data.
 * @param networkMonitor The utility to monitor network connectivity.
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
     * Loads the notifications from the repository and updates the UI state.
     * It handles loading, success, and error states.
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
     * Refreshes the notifications by setting the refreshing state and calling [loadNotifications].
     */
    fun refreshNotifications() {
        _isRefreshing.value = true
        loadNotifications()
    }

    /**
     * Marks a notification as read and saves the updated status in the repository.
     * @param notification The notification to be dismissed.
     */
    fun dismissNotification(notification: MifosNotification) {
        viewModelScope.launch {
            notificationRepositoryImp.saveNotification(notification.copy(read = true))
            notificationRepositoryImp.updateReadStatus(notification, true)
        }
    }

    /**
     * Sorts the notifications based on their read status and timestamp.
     * Unread notifications are shown first, followed by the most recent ones.
     * @param notifications The list of notifications to be sorted.
     * @return The sorted list of notifications.
     */
    private fun sortNotifications(notifications: List<MifosNotification>): List<MifosNotification> {
        return notifications.sortedWith(
            compareByDescending<MifosNotification> { !it.isRead() }
                .thenByDescending { it.timeStamp },
        )
    }
}

/**
 * Represents the different UI states for the Notification screen.
 */
internal sealed interface NotificationUiState {
    /**
     * Represents the loading state where notifications are being fetched.
     */
    data object Loading : NotificationUiState

    /**
     * Represents the success state with a list of notifications.
     * @param notifications The list of notifications to display.
     */
    data class Success(val notifications: List<MifosNotification>) : NotificationUiState

    /**
     * Represents the error state with an error message.
     * @param errorMessage The error message to display.
     */
    data class Error(val errorMessage: String?) : NotificationUiState

    /**
     * Represents the empty state when no notifications are available.
     */
    data object Empty : NotificationUiState
}
