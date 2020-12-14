package org.mifos.mobile.api.services

import io.reactivex.Observable
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.entity.Transaction
import org.mifos.mobile.models.entity.TransactionInfo
import org.mifos.mobile.models.entity.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionsService {
    @POST(ApiEndPoints.TRANSFER)
    fun makeTransaction(@Body transaction: Transaction?): Observable<TransactionInfo>

    @GET(ApiEndPoints.TRANSFER + "/{transactionId}")
    fun fetchTransactionInfo(
            @Path("transactionId") transactionId: String?): Observable<TransactionResponse>

    @POST(ApiEndPoints.TRANSACTION_REQUEST)
    fun requestTransaction(@Body transactionRequest: Transaction?): Observable<TransactionInfo>
}