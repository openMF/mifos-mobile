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
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ThirdPartyTransferService {
    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template?type=tpt")
    suspend fun accountTransferTemplate(): AccountOptionsTemplate

    @POST(ApiEndPoints.ACCOUNT_TRANSFER + "?type=tpt")
    suspend fun makeTransfer(@Body transferPayload: TransferPayload?): ResponseBody
}
