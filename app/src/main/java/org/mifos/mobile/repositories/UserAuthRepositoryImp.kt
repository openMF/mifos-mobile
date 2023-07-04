package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.models.register.UserVerify
import javax.inject.Inject

class UserAuthRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    UserAuthRepository {

    override fun registerUser(
        accountNumber: String?,
        authenticationMode: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        mobileNumber: String?,
        password: String?,
        username: String?
    ): Observable<ResponseBody?>? {
        val registerPayload = RegisterPayload().apply {
            this.accountNumber = accountNumber
            this.authenticationMode = authenticationMode
            this.email = email
            this.firstName = firstName
            this.lastName = lastName
            this.mobileNumber = mobileNumber
            this.password = password
            this.username = username
        }
        return dataManager.registerUser(registerPayload)
    }

    override fun verifyUser(authenticationToken: String?, requestId: String?): Observable<ResponseBody?>? {
        val userVerify = UserVerify().apply {
            this.authenticationToken = authenticationToken
            this.requestId = requestId
        }
        return dataManager.verifyUser(userVerify)
    }
}