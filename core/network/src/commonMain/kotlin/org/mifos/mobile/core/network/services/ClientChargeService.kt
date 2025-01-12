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
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface ClientChargeService {

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    fun getClientChargeList(@Path("clientId") clientId: Long): Flow<Page<Charge>>

    @GET(ApiEndPoints.LOANS + "/{loanId}/charges")
    suspend fun getLoanAccountChargeList(@Path("loanId") loanId: Long): List<Charge>

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}/charges")
    suspend fun getSavingsAccountChargeList(@Path("savingsId") savingsId: Long): List<Charge>
}
