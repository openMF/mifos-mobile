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
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.network.dto.beneficiary.BeneficiaryListResponseDto
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryCreatePayloadDto
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryUpdatePayloadDto
import org.mifos.mobile.core.network.dto.templates.beneficiary.BeneficiaryTemplateDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface BeneficiaryService {
    @GET(ApiEndPoints.BENEFICIARIES + "/tpt")
    fun beneficiaryList(): Flow<List<BeneficiaryListResponseDto>>

    @GET(ApiEndPoints.BENEFICIARIES + "/tpt/template")
    fun beneficiaryTemplate(): Flow<BeneficiaryTemplateDto>

    @POST(ApiEndPoints.BENEFICIARIES + "/tpt")
    suspend fun createBeneficiary(@Body beneficiaryPayload: BeneficiaryCreatePayloadDto?): HttpResponse

    @PUT(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun updateBeneficiary(
        @Path("beneficiaryId") beneficiaryId: Long,
        @Body payload: BeneficiaryUpdatePayloadDto?,
    ): HttpResponse

    @DELETE(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun deleteBeneficiary(@Path("beneficiaryId") beneficiaryId: Long): HttpResponse
}
