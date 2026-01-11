/*
 * Copyright 2026 Mifos Initiative
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
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto
import org.mifos.mobile.core.network.dto.client.ClientResponseDto
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface ClientService {

    @GET(ApiEndPoints.CLIENTS)
    fun clients(): Flow<PageResponseDto<ClientResponseDto>>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}")
    fun getClientForId(@Path(CLIENT_ID) clientId: Long): Flow<ClientResponseDto>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    fun getClientImage(@Path(CLIENT_ID) clientId: Long): Flow<HttpResponse>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getClientAccounts(@Path(CLIENT_ID) clientId: Long): Flow<AccountsResponseDto>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getAccounts(
        @Path(CLIENT_ID) clientId: Long,
        @Query("fields") accountType: String?,
    ): Flow<AccountsResponseDto>

    companion object {
        const val CLIENT_ID = "clientId"
    }
}
