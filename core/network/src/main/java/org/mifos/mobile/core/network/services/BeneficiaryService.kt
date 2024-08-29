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
import org.mifos.mobile.core.common.ApiEndPoints
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BeneficiaryService {
    @GET(ApiEndPoints.BENEFICIARIES + "/tpt")
    suspend fun beneficiaryList(): List<Beneficiary>

    @GET(ApiEndPoints.BENEFICIARIES + "/tpt/template")
    suspend fun beneficiaryTemplate(): BeneficiaryTemplate

    @POST(ApiEndPoints.BENEFICIARIES + "/tpt")
    suspend fun createBeneficiary(@Body beneficiaryPayload: BeneficiaryPayload?): ResponseBody

    @PUT(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun updateBeneficiary(
        @Path("beneficiaryId") beneficiaryId: Long?,
        @Body payload: BeneficiaryUpdatePayload?,
    ): ResponseBody

    @DELETE(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun deleteBeneficiary(@Path("beneficiaryId") beneficiaryId: Long?): ResponseBody
}
