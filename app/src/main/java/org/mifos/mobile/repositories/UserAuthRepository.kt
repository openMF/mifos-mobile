package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.User
import retrofit2.Response

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

    suspend fun login(username: String, password: String): Response<User?>?

    fun verifyUser(authenticationToken: String?, requestId: String?): Observable<ResponseBody?>?

    fun updateAccountPassword(
        newPassword: String, confirmPassword: String
    ): Observable<ResponseBody?>?

}
