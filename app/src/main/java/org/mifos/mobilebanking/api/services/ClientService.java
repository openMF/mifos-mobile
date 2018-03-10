package org.mifos.mobilebanking.api.services;

import org.mifos.mobilebanking.api.ApiEndPoints;
import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.models.client.Client;
import org.mifos.mobilebanking.models.client.ClientAccounts;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Vishwajeet
 * @since 20/6/16.
 */
public interface ClientService {

    String CLIENT_ID = "clientId";

    //This is a default call and Loads client from 0 to 200
    @GET(ApiEndPoints.CLIENTS)
    Observable<Page<Client>> getClients();

    @GET(ApiEndPoints.CLIENTS + "/{clientId}")
    Observable<Client> getClientForId(@Path(CLIENT_ID) long clientId);

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    Observable<ResponseBody> getClientImage(@Path(CLIENT_ID) long clientId);

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    Observable<ClientAccounts> getClientAccounts(@Path(CLIENT_ID) long clientId);

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    Observable<ClientAccounts> getAccounts(@Path(CLIENT_ID) long clientId,
            @Query("fields") String accountType);

}
