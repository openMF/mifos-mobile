package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.models.User
import retrofit2.Response

interface UserAuthRepository {

    suspend fun registerUser(
        accountNumber: String?,
        authenticationMode: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        mobileNumber: String?,
        password: String?,
        username: String?
    ): Response<ResponseBody?>?

    suspend fun login(username: String, password: String): Response<User?>?

    suspend fun verifyUser(authenticationToken: String?, requestId: String?): Response<ResponseBody?>?

    suspend fun updateAccountPassword(
        newPassword: String, confirmPassword: String
    ): Response<ResponseBody?>?

}
