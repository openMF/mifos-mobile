package org.mifos.mobile.api.services

import okhttp3.ResponseBody
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import retrofit2.http.*

/**
 * Created by dilpreet on 14/6/17.
 */
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
