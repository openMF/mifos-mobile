package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    NotificationRepository {

    override suspend fun loadNotifications(): Flow<List<MifosNotification?>?> {
        return dataManager.notifications()
    }
}