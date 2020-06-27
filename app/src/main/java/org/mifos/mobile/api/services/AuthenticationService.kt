package org.mifos.mobile.api.services

import io.reactivex.Observable
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.User
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by shivangi on 27/June/2020
 */
interface AuthenticationService {
    @POST(ApiEndPoints.AUTHENTICATION)
    fun authenticate(@Query("username") username: String?,
                     @Query("password") password: String?): Observable<User?>?
}