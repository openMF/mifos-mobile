package org.mifos.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by dilpreet on 21/6/17.
 */
interface ThirdPartyTransferService {
    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template?type=tpt")
    suspend fun accountTransferTemplate(): Response<AccountOptionsTemplate?>?

    @POST(ApiEndPoints.ACCOUNT_TRANSFER + "?type=tpt")
    fun makeTransfer(@Body transferPayload: TransferPayload?): Observable<ResponseBody?>?
}
