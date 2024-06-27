package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import javax.inject.Inject

class UserDetailRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    UserDetailRepository {

    override fun registerNotification(payload: NotificationRegisterPayload?): Flow<ResponseBody> {
        return flow {
            emit(dataManager.registerNotification(payload))
        }
    }

    override fun getUserNotificationId(id: Long): Flow<NotificationUserDetail> {
        return flow {
            emit(dataManager.getUserNotificationId(id))
        }
    }

    override fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?
    ): Flow<ResponseBody> {
        return flow {
            emit(dataManager.updateRegisterNotification(id, payload))
        }
    }

}