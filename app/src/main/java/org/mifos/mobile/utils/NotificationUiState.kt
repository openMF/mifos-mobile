package org.mifos.mobile.utils

import org.mifos.mobile.models.notification.MifosNotification

sealed class NotificationUiState {
    object Initial : NotificationUiState()
    object Loading : NotificationUiState()
    data class LoadNotificationsSuccessful(val notifications : List<MifosNotification?>) : NotificationUiState()
    object Error : NotificationUiState()
}