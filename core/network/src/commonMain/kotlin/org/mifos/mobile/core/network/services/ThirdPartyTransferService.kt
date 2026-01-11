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

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.network.dto.payloads.TransferPayloadDto
import org.mifos.mobile.core.network.dto.templates.accounts.AccountOptionsTemplateResponseDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface ThirdPartyTransferService {
    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template?type=tpt")
    fun accountTransferTemplate(): Flow<AccountOptionsTemplateResponseDto>

    @POST(ApiEndPoints.ACCOUNT_TRANSFER + "?type=tpt")
    suspend fun makeTransfer(@Body transferPayload: TransferPayloadDto?): HttpResponse
}
