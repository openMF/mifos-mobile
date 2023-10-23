package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.models.notification.MifosNotification

interface NotificationRepository {

    suspend fun loadNotifications(): Flow<List<MifosNotification>>

}