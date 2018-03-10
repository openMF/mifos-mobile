package org.mifos.mobilebanking.api.services;

import org.mifos.mobilebanking.api.ApiEndPoints;
import org.mifos.mobilebanking.models.payload.TransferPayload;
import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by dilpreet on 21/6/17.
 */

public interface ThirdPartyTransferService {

    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template?type=tpt")
    Observable<AccountOptionsTemplate> getAccountTransferTemplate();

    @POST(ApiEndPoints.ACCOUNT_TRANSFER + "?type=\"tpt\"")
    Observable<ResponseBody> makeTransfer(@Body TransferPayload transferPayload);
}
