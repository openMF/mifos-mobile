package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail

interface UserDetailRepository {

    fun registerNotification(payload: NotificationRegisterPayload?): Observable<ResponseBody>

    fun getUserNotificationId(id: Long): Observable<NotificationUserDetail>

    fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?,
    ): Observable<ResponseBody>

}