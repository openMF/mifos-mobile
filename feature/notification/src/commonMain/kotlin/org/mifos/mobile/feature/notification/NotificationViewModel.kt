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

    fun refreshNotifications() {
        _isRefreshing.value = true
        loadNotifications()
    }

    fun dismissNotification(notification: MifosNotification) {
        viewModelScope.launch {
            notificationRepositoryImp.saveNotification(notification.copy(read = true))
            notificationRepositoryImp.updateReadStatus(notification, true)
        }
    }

    private fun sortNotifications(notifications: List<MifosNotification>): List<MifosNotification> {
        return notifications.sortedWith(
            compareByDescending<MifosNotification> { !it.isRead() }
                .thenByDescending { it.timeStamp },
        )
    }
}

internal sealed interface NotificationUiState {
    data object Loading : NotificationUiState
    data class Success(val notifications: List<MifosNotification>) : NotificationUiState
    data class Error(val errorMessage: String?) : NotificationUiState
    data object Empty : NotificationUiState
}
