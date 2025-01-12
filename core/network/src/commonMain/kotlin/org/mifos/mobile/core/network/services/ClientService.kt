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

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface ClientService {

    @GET(ApiEndPoints.CLIENTS)
    fun clients(): Flow<Page<Client>>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}")
    suspend fun getClientForId(@Path(CLIENT_ID) clientId: Long): Client

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    suspend fun getClientImage(@Path(CLIENT_ID) clientId: Long): HttpResponse

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getClientAccounts(@Path(CLIENT_ID) clientId: Long): Flow<ClientAccounts>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    suspend fun getAccounts(
        @Path(CLIENT_ID) clientId: Long,
        @Query("fields") accountType: String?,
    ): ClientAccounts

    companion object {
        const val CLIENT_ID = "clientId"
    }
}
