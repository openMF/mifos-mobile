package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferRequest;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferResponse;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplate;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Vishwajeet
 * @since 22/8/16.
 */

public interface FundTransferService {
    @GET(ApiEndPoints.ACCOUNTTRANSFERS + "/template")
    Call<FundTransferTemplateResponse> getAccountTransferTemplate();

    @POST(ApiEndPoints.ACCOUNTTRANSFERS)
    Call<FundTransferResponse> submitTransfer(@Body FundTransferRequest fundTransferRequest);
}
