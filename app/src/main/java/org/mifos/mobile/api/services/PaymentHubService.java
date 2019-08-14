package org.mifos.mobile.api.services;

import org.mifos.mobile.api.ApiEndPoints;
import org.mifos.mobile.models.payload.PaymentHubTransferPayload;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by dilpreet on 21/6/17.
 */

public interface PaymentHubService {

    @POST(ApiEndPoints.PAYMENT_HUB_CHANNEL_TRANSACTIONS)
    Observable<ResponseBody> makeTransfer(@Body PaymentHubTransferPayload transferPayload);
}
