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
import org.mifos.mobile.core.network.dto.guarantor.GuarantorListResponseDto
import org.mifos.mobile.core.network.dto.guarantor.GuarantorTemplateResponseDto
import org.mifos.mobile.core.network.dto.payloads.GuarantorApplicationPayloadDto

interface GuarantorService {
    @GET("loans/{loanId}/guarantors/template")
    fun getGuarantorTemplate(@Path("loanId") loanId: Long): Flow<GuarantorTemplateResponseDto>

    @GET("loans/{loanId}/guarantors")
    fun getGuarantorList(@Path("loanId") loanId: Long): Flow<List<GuarantorListResponseDto>>

    @POST("loans/{loanId}/guarantors")
    suspend fun createGuarantor(
        @Path("loanId") loanId: Long,
        @Body payload: GuarantorApplicationPayloadDto?,
    ): HttpResponse

    @PUT("loans/{loanId}/guarantors/{guarantorId}")
    suspend fun updateGuarantor(
        @Body payload: GuarantorApplicationPayloadDto?,
        @Path("loanId") loanId: Long,
        @Path("guarantorId") guarantorId: Long,
    ): HttpResponse

    @DELETE("loans/{loanId}/guarantors/{guarantorId}")
    suspend fun deleteGuarantor(
        @Path("loanId") loanId: Long,
        @Path("guarantorId") guarantorId: Long,
    ): HttpResponse
}
