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
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.network.dto.charges.ChargeResponseDto
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface ClientChargeService {

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    fun getClientChargeList(@Path("clientId") clientId: Long): Flow<PageResponseDto<ChargeResponseDto>>

    @GET("{chargeType}/{chargeTypeId}/charges")
    fun getChargeList(
        @Path("chargeType") chargeType: String,
        @Path("chargeTypeId") chargeTypeId: Long,
    ): Flow<List<ChargeResponseDto>>
}
