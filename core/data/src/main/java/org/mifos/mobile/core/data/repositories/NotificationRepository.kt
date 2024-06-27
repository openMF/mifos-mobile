package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.datastore.model.MifosNotification

interface NotificationRepository {

    suspend fun loadNotifications(): Flow<List<MifosNotification>>

}