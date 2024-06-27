package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import org.mifos.mobile.core.common.ApiEndPoints
import retrofit2.http.*

/**
 * Created by dilpreet on 26/09/17.
 */
interface NotificationService {
    @GET(ApiEndPoints.DEVICE + "/registration/client/{clientId}")
    suspend fun getUserNotificationId(@Path("clientId") clientId: Long): NotificationUserDetail

    @POST(ApiEndPoints.DEVICE + "/registration")
    suspend fun registerNotification(@Body payload: NotificationRegisterPayload?): ResponseBody

    @PUT(ApiEndPoints.DEVICE + "/registration/{id}")
    suspend fun updateRegisterNotification(
        @Path("id") id: Long,
        @Body payload: NotificationRegisterPayload?,
    ): ResponseBody
}
