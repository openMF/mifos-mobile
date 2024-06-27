package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.common.ApiEndPoints
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by dilpreet on 21/6/17.
 */
interface ThirdPartyTransferService {
    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template?type=tpt")
    suspend fun accountTransferTemplate(): AccountOptionsTemplate

    @POST(ApiEndPoints.ACCOUNT_TRANSFER + "?type=tpt")
    suspend fun makeTransfer(@Body transferPayload: TransferPayload?): ResponseBody
}
