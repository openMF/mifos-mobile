package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.User

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
    ): Flow<ResponseBody>

    suspend fun login(username: String, password: String): Flow<User>

    suspend fun verifyUser(authenticationToken: String?, requestId: String?): Flow<ResponseBody>

    suspend fun updateAccountPassword(
        newPassword: String, confirmPassword: String
    ): Flow<ResponseBody>

}
