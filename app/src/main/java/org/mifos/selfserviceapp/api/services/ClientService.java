package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.client.Client;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Vishwajeet
 * @since 20/6/16.
 */
public interface ClientService {

    //This is a default call and Loads client from 0 to 200
    @GET(ApiEndPoints.CLIENTS)
    Call<Page<Client>> getClients();

}
