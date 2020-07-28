package org.mifos.mobile.api.services

import org.mifos.mobile.api.ApiEndPoints.ACCOUNTS
import org.mifos.mobile.api.ApiEndPoints.INTEROPERATION
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface FineractPaymentHubService {

    @GET("$INTEROPERATION/$ACCOUNTS/{accountExternalId}/identifiers")
    fun fetchSecondaryIdentifiers(@Path("accountExternalId") accountExternalId: String):
            Observable<PartyIdentifiers>
}