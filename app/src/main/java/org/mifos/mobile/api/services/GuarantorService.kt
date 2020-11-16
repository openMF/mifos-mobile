package org.mifos.mobile.api.services

import io.reactivex.Observable

import okhttp3.ResponseBody

import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload

import retrofit2.http.*

/*
* Created by saksham on 23/July/2018
*/   interface GuarantorService {
    @GET("/loans/{loanId}/guarantors/template")
    fun getGuarantorTemplate(@Path("loanId") loanId: Long?): Observable<GuarantorTemplatePayload?>?

    @GET("/loans/{loanId}/guarantors")
    fun getGuarantorList(@Path("loanId") loanId: Long?): Observable<List<GuarantorPayload?>?>?

    @POST("/loans/{loanId}/guarantors")
    fun createGuarantor(
            @Path("loanId") loanId: Long?,
            @Body payload: GuarantorApplicationPayload?
    ): Observable<ResponseBody?>?

    @PUT("loans/{loanId}/guarantors/{guarantorId}")
    fun updateGuarantor(
            @Body payload: GuarantorApplicationPayload?,
            @Path("loanId") loanId: Long?,
            @Path("guarantorId") guarantorId: Long?
    ): Observable<ResponseBody?>?

    @DELETE("/loans/{loanId}/guarantors/{guarantorId}")
    fun deleteGuarantor(
            @Path("loanId") loanId: Long?,
            @Path("guarantorId") guarantorId: Long?
    ): Observable<ResponseBody?>?
}