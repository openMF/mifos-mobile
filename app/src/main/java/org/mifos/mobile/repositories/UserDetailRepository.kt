package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail

interface UserDetailRepository {

    fun registerNotification(payload: NotificationRegisterPayload?): Flow<ResponseBody>

    fun getUserNotificationId(id: Long): Flow<NotificationUserDetail>

    fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?,
    ): Flow<ResponseBody>

}