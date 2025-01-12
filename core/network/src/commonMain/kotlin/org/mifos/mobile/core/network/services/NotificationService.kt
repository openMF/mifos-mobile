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

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface NotificationService {
    @GET(ApiEndPoints.DEVICE + "/registration/client/{clientId}")
    fun getUserNotificationId(@Path("clientId") clientId: Long): Flow<NotificationUserDetail>

    @POST(ApiEndPoints.DEVICE + "/registration")
    suspend fun registerNotification(@Body payload: NotificationRegisterPayload?): HttpResponse

    @PUT(ApiEndPoints.DEVICE + "/registration/{id}")
    suspend fun updateRegisterNotification(
        @Path("id") id: Long,
        @Body payload: NotificationRegisterPayload?,
    ): HttpResponse
}
