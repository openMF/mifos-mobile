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
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface RecentTransactionsService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/transactions")
    fun getRecentTransactionsList(
        @Path("clientId") clientId: Long,
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?,
    ): Flow<Page<Transaction>>
}
