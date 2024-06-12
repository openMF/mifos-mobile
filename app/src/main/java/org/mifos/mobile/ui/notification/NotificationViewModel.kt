package org.mifos.mobile.ui.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.repositories.NotificationRepository
import org.mifos.mobile.utils.DateHelper
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepositoryImp: NotificationRepository) :
    ViewModel() {

    private val _notificationUiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val notificationUiState: StateFlow<NotificationUiState> get() = _notificationUiState

    private val _isRefreshing = MutableStateFlow<Boolean>(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        _notificationUiState.value = NotificationUiState.Loading
        viewModelScope.launch {
            notificationRepositoryImp.loadNotifications()
                .catch {
                    _notificationUiState.value = NotificationUiState.Error(errorMessage = it.message)
                }.collect { notifications ->
                    _isRefreshing.emit(false)
                    _notificationUiState.value = NotificationUiState.Success(notifications = notifications)
                }
        }
    }

    fun refreshNotifications() {
        _isRefreshing.value = true
        loadNotifications()
    }

    fun dismissNotification(notification: MifosNotification) {
        notification.setRead(true)
        notification.save()
    }
}


sealed class NotificationUiState {
    data object Loading : NotificationUiState()
    data class Success(val notifications: List<MifosNotification>) : NotificationUiState()
    data class Error(val errorMessage: String?) : NotificationUiState()
}