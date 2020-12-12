package org.mifos.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.entity.RegistrationEntity
import org.mifos.mobile.api.ApiEndPoints
import retrofit2.http.Body
import retrofit2.http.POST


interface PartyRegistrationService {
    @POST(ApiEndPoints.PARTY_REGISTRATION)
    fun registerSecondaryIdentifier(
            @Body registrationEntity: RegistrationEntity?): Observable<ResponseBody>
}