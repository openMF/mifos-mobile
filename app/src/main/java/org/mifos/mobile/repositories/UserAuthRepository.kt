package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.User

interface UserAuthRepository {

    fun registerUser(
        accountNumber: String?,
        authenticationMode: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        mobileNumber: String?,
        password: String?,
        username: String?
    ): Observable<ResponseBody?>?

    fun login(username: String, password: String): Observable<User?>?
}
