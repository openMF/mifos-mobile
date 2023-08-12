package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import javax.inject.Inject

class UserDetailRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    UserDetailRepository {

    override fun registerNotification(payload: NotificationRegisterPayload?): Observable<ResponseBody> {
        return dataManager.registerNotification(payload)
    }

    override fun getUserNotificationId(id: Long): Observable<NotificationUserDetail> {
        return dataManager.getUserNotificationId(id)
    }

    override fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?
    ): Observable<ResponseBody> {
        return dataManager.updateRegisterNotification(id, payload)
    }

}