package org.mifos.mobile.api.services

import io.reactivex.Observable
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
    @get:GET(ApiEndPoints.BENEFICIARIES + "/tpt")
    val beneficiaryList: Observable<List<Beneficiary?>?>?

    @get:GET(ApiEndPoints.BENEFICIARIES + "/tpt/template")
    val beneficiaryTemplate: Observable<BeneficiaryTemplate?>?

    @POST(ApiEndPoints.BENEFICIARIES + "/tpt")
    fun createBeneficiary(@Body beneficiaryPayload: BeneficiaryPayload?): Observable<ResponseBody?>?

    @PUT(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    fun updateBeneficiary(
            @Path("beneficiaryId") beneficiaryId: Long?,
            @Body payload: BeneficiaryUpdatePayload?
    ): Observable<ResponseBody?>?

    @DELETE(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    fun deleteBeneficiary(@Path("beneficiaryId") beneficiaryId: Long?): Observable<ResponseBody?>?
}