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
import de.jensklingenberg.ktorfit.http.Query
import org.mifos.mobile.core.network.dto.pocket.PocketCommandResponse
import org.mifos.mobile.core.network.dto.pocket.PocketDelinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketLinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketResponseDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface PocketService {
    @GET(ApiEndPoints.POCKETS)
    suspend fun getPocketAccounts(): PocketResponseDto

    @POST(ApiEndPoints.POCKETS)
    suspend fun linkAccounts(
        @Query("command") command: String = "linkAccounts",
        @Body request: PocketLinkRequest,
    ): PocketCommandResponse

    @POST(ApiEndPoints.POCKETS)
    suspend fun delinkAccounts(
        @Query("command") command: String = "delinkAccounts",
        @Body request: PocketDelinkRequest,
    ): PocketCommandResponse
}
