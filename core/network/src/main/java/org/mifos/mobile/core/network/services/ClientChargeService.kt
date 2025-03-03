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

    /**
     * This service allows to fetch loans and savings charges
     * 2. Saving Charges // https://DomainName/api/v1/self/savingsaccounts/{savingsId}/charges
     * 2. Loan Charges // https://DomainName/api/v1/self/loans/{loanId}/charges
     * @param chargeType is savingsaccounts, loans
     * @param chargeTypeId is savingsId, loanId
     */
    @GET("/{chargeType}/{chargeTypeId}/charges")
    suspend fun getChargeList(
        @Path("chargeType") chargeType: String,
        @Path("chargeTypeId") chargeTypeId: Long?,
    ): List<Charge>

    /**
     * This service allows to fetch client charges
     */
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    suspend fun getClientChargeList(@Path("clientId") clientId: Long?): Page<Charge>
}
