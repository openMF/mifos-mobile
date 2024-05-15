package org.mifos.mobile.api.services

import okhttp3.ResponseBody
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import retrofit2.http.*

/*
* Created by saksham on 23/July/2018
*/ interface GuarantorService {
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
