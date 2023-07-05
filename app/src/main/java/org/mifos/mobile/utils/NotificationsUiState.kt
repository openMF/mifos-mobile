package org.mifos.mobile.utils

import org.mifos.mobile.models.notification.MifosNotification

sealed class NotificationsUiState {
    object Loading : NotificationsUiState()
    data class Success(val notifications: List<MifosNotification?>) : NotificationsUiState()
    data class Error(val errorMessage: Int) : NotificationsUiState()
}
