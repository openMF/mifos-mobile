package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.datastore.model.MifosNotification
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    NotificationRepository {

    override suspend fun loadNotifications(): Flow<List<MifosNotification>> {
        return flow {
            emit(dataManager.notifications())
        }
    }
}