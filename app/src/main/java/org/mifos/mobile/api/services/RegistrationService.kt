package org.mifos.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.entity.RegistrationEntity
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.models.register.UserVerify
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationService {
    @POST(ApiEndPoints.PARTY_REGISTRATION)
    fun registerSecondaryIdentifier(
            @Body registrationEntity: RegistrationEntity?): Observable<ResponseBody>
}