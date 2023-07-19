package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody

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

    fun verifyUser(authenticationToken: String?, requestId: String?): Observable<ResponseBody?>?

    fun updateAccountPassword(
        newPassword: String, confirmPassword: String
    ): Observable<ResponseBody?>?

}
