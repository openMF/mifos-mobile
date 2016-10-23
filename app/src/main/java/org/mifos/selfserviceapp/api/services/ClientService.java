package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.ClientAccounts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Vishwajeet
 * @since 20/6/16.
 */
public interface ClientService {

    //This is a default call and Loads client from 0 to 200
    @GET(ApiEndPoints.CLIENTS)
    Call<Page<Client>> getClients();

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    Call<ClientAccounts> getClientAccounts(@Path("clientId") long clientId);

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    Call<ClientAccounts> getAccounts(@Path("clientId") long clientId,
            @Query("fields") String accountType);

}
