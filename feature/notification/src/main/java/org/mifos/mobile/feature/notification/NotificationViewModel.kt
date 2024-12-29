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

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.datastore.entity.MifosNotification
import org.mifos.mobile.feature.notification.NotificationUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class NotificationViewModel @Inject constructor(
    private val notificationRepositoryImp: NotificationRepository,
) : ViewModel() {

    private val _notificationUiState = MutableStateFlow<NotificationUiState>(Loading)
    val notificationUiState: StateFlow<NotificationUiState> get() = _notificationUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing

    init {
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepositoryImp.deleteOldNotifications()
            loadNotifications()
        }
    }

    fun loadNotifications() {
        _notificationUiState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepositoryImp.loadNotifications()
                .catch {
                    Log.e("selfServiceDatabase", it.toString())
                    _notificationUiState.value =
                        NotificationUiState.Error(errorMessage = it.message)
                }.collect { notifications ->
                    Log.e("selfServiceDatabase", notifications.toString())
                    _isRefreshing.emit(false)
                    _notificationUiState.value =
                        NotificationUiState.Success(notifications = notifications)
                }
        }
    }

    fun refreshNotifications() {
        _isRefreshing.value = true
        loadNotifications()
    }

    fun dismissNotification(notification: MifosNotification) {
//        notification.updateReadStatus(true)
//        notification.save()
        notification.read = true
        viewModelScope.launch {
            notificationRepositoryImp.saveNotification(notification.copy(read = true))
            notificationRepositoryImp.updateReadStatus(notification, true)
        }
    }
}

internal sealed class NotificationUiState {
    data object Loading : NotificationUiState()
    data class Success(val notifications: List<MifosNotification>) : NotificationUiState()
    data class Error(val errorMessage: String?) : NotificationUiState()
}
