package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.common.ApiEndPoints
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Vishwajeet
 * @since 20/6/16.
 */
interface ClientService {
    // This is a default call and Loads client from 0 to 200
    @GET(ApiEndPoints.CLIENTS)
    suspend fun clients(): Page<Client>

    @GET(ApiEndPoints.CLIENTS + "/{clientId}")
    suspend fun getClientForId(@Path(CLIENT_ID) clientId: Long?): Client

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    suspend fun getClientImage(@Path(CLIENT_ID) clientId: Long?): ResponseBody

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    suspend fun getClientAccounts(@Path(CLIENT_ID) clientId: Long?): ClientAccounts

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    suspend fun getAccounts(
        @Path(CLIENT_ID) clientId: Long?,
        @Query("fields") accountType: String?,
    ): ClientAccounts

    companion object {
        const val CLIENT_ID = "clientId"
    }
}
