package org.mifos.mobile.api.services

import android.database.Observable
import org.mifos.mobile.api.ApiEndPoints.ACCOUNTS
import org.mifos.mobile.api.ApiEndPoints.INTEROPERATION
import org.mifos.mobile.models.entity.PartyIdentifiers
import retrofit2.http.GET
import retrofit2.http.Path

interface FineractPaymentHubService {
    @GET("$INTEROPERATION/$ACCOUNTS/{accountExternalId}/identifiers")
    fun fetchSecondaryIdentifiers(@Path("accountExternalId") accountExternalId: String):
            Observable<PartyIdentifiers>
}