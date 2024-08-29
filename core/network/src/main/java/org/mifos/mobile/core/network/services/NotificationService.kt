/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.common.ApiEndPoints
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
