package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import javax.inject.Inject

class UserDetailRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    UserDetailRepository {

    override fun registerNotification(payload: NotificationRegisterPayload?): Flow<ResponseBody> {
        return flow {
            dataManager.registerNotification(payload)
        }
    }

    override fun getUserNotificationId(id: Long): Flow<NotificationUserDetail> {
        return flow {
            dataManager.getUserNotificationId(id)
        }
    }

    override fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?
    ): Flow<ResponseBody> {
        return flow {
            dataManager.updateRegisterNotification(id, payload)
        }
    }

}