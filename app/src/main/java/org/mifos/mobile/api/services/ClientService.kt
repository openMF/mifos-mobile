package org.mifos.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Vishwajeet
 * @since 20/6/16.
 */
interface ClientService {
    //This is a default call and Loads client from 0 to 200
    @get:GET(ApiEndPoints.CLIENTS)
    val clients: Observable<Page<Client?>?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}")
    fun getClientForId(@Path(CLIENT_ID) clientId: Long?): Observable<Client?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    fun getClientImage(@Path(CLIENT_ID) clientId: Long?): Observable<ResponseBody?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getClientAccounts(@Path(CLIENT_ID) clientId: Long?): Observable<ClientAccounts?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getAccounts(
            @Path(CLIENT_ID) clientId: Long?,
            @Query("fields") accountType: String?
    ): Observable<ClientAccounts?>?

    companion object {
        const val CLIENT_ID = "clientId"
    }
}