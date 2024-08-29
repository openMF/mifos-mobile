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

import org.mifos.mobile.core.common.ApiEndPoints
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecentTransactionsService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/transactions")
    suspend fun getRecentTransactionsList(
        @Path("clientId") clientId: Long?,
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?,
    ): Page<Transaction>
}
