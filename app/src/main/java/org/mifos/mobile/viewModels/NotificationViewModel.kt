package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.repositories.NotificationRepository
import org.mifos.mobile.utils.NotificationUiState
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepositoryImp: NotificationRepository) :
    ViewModel() {

    private val _notificationUiState = MutableLiveData<NotificationUiState>()
    val notificationUiState: LiveData<NotificationUiState> get() = _notificationUiState

    fun loadNotifications() {
        _notificationUiState.value = NotificationUiState.Loading
        viewModelScope.launch {
            notificationRepositoryImp.loadNotifications().catch {
                _notificationUiState.value = NotificationUiState.Error
            }.collect { list ->
                _notificationUiState.value =
                    list?.let { NotificationUiState.LoadNotificationsSuccessful(it) }
            }
        }
    }
}
