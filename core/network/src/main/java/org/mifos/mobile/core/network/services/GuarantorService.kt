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

import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GuarantorService {
    @GET("loans/{loanId}/guarantors/template")
    suspend fun getGuarantorTemplate(@Path("loanId") loanId: Long?): GuarantorTemplatePayload

    @GET("loans/{loanId}/guarantors")
    suspend fun getGuarantorList(@Path("loanId") loanId: Long?): List<GuarantorPayload>

    @POST("loans/{loanId}/guarantors")
    suspend fun createGuarantor(
        @Path("loanId") loanId: Long?,
        @Body payload: GuarantorApplicationPayload?,
    ): ResponseBody

    @PUT("loans/{loanId}/guarantors/{guarantorId}")
    suspend fun updateGuarantor(
        @Body payload: GuarantorApplicationPayload?,
        @Path("loanId") loanId: Long?,
        @Path("guarantorId") guarantorId: Long?,
    ): ResponseBody

    @DELETE("loans/{loanId}/guarantors/{guarantorId}")
    suspend fun deleteGuarantor(
        @Path("loanId") loanId: Long?,
        @Path("guarantorId") guarantorId: Long?,
    ): ResponseBody
}
