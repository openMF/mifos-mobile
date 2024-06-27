package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail

interface UserDetailRepository {

    fun registerNotification(payload: NotificationRegisterPayload?): Flow<ResponseBody>

    fun getUserNotificationId(id: Long): Flow<NotificationUserDetail>

    fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?,
    ): Flow<ResponseBody>

}