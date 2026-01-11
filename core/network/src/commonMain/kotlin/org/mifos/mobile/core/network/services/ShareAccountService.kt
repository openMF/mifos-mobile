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
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.dto.payloads.ShareApplicationPayloadDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductDetailsResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareWithAssociationsResponseDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface ShareAccountService {

    @GET("${ApiEndPoints.PRODUCTS}/" + ApiEndPoints.SHARE)
    fun getShareProducts(
        @Query("clientId") clientId: Long?,
    ): Flow<PageResponseDto<ShareProductResponseDto>>

    @GET("${ApiEndPoints.PRODUCTS}/" + ApiEndPoints.SHARE + "/{productId}")
    fun getShareProductById(
        @Path("productId") productId: Long,
        @Query("clientId") clientId: Long?,
    ): Flow<ShareProductDetailsResponseDto>

    @POST(ApiEndPoints.SHARE_ACCOUNTS)
    suspend fun submitShareApplication(
        @Body payload: ShareApplicationPayloadDto?,
    ): HttpResponse

    @GET(ApiEndPoints.SHARE_ACCOUNTS + "/{accountId}")
    fun getShareAccountDetails(
        @Path("accountId") accountId: Long,
        @Query("associations") associations: String = "all",
    ): Flow<ShareWithAssociationsResponseDto>
}
