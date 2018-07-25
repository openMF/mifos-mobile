package org.mifos.mobilebanking.api.services;

/*
 * Created by saksham on 23/July/2018
 */

import org.mifos.mobilebanking.models.guarantor.GuarantorApplicationPayload;
import org.mifos.mobilebanking.models.guarantor.GuarantorPayload;
import org.mifos.mobilebanking.models.guarantor.GuarantorTemplatePayload;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public interface GuarantorService {

    @GET("/loans/{loanId}/guarantors/template")
    Observable<GuarantorTemplatePayload> getGuarantorTemplate(@Path("loanId") long loanId);

    @GET("/loans/{loanId}/guarantors")
    Observable<List<GuarantorPayload>> getGuarantorList(@Path("loanId") long loanId);

    @POST("/loans/{loanId}/guarantors")
    Observable<ResponseBody> createGuarantor(@Path("loanId") long loanId,
                                             @Body GuarantorApplicationPayload payload);

    @PUT("loans/{loanId}/guarantors/{guarantorId}")
    Observable<ResponseBody> updateGuarantor(@Body GuarantorApplicationPayload payload,
                                             @Path("loanId") long loanId,
                                             @Path("guarantorId") long guarantorId);

    @DELETE("/loans/{loanId}/guarantors/{guarantorId}")
    Observable<ResponseBody> deleteGuarantor(@Path("loanId") long loanId,
                                             @Path("guarantorId") long guarantorId);

}
