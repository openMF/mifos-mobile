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
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import retrofit2.http.GET
import retrofit2.http.Path

interface ClientChargeService {

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    suspend fun getClientChargeList(@Path("clientId") clientId: Long?): Page<Charge>

    @GET(ApiEndPoints.LOANS + "/{loanId}/charges")
    suspend fun getLoanAccountChargeList(@Path("loanId") loanId: Long?): List<Charge>

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}/charges")
    suspend fun getSavingsAccountChargeList(@Path("savingsId") savingsId: Long?): List<Charge>
}
