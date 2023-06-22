package org.mifos.mobile

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.register.RegisterPayload

class RegistrationRepository(private var dataManager : DataManager?) {

    fun registerUser(registerPayload: RegisterPayload?) : Observable<ResponseBody?>? {
        return dataManager?.registerUser(registerPayload)
    }

    fun createRegisterPayload(
        accountNumber: String,
        authenticationMode: String,
        email: String,
        firstName: String,
        lastName: String,
        mobileNumber: String,
        password: String,
        username: String
    ): RegisterPayload {
        return RegisterPayload().apply {
            this.accountNumber = accountNumber
            this.authenticationMode = authenticationMode
            this.email = email
            this.firstName = firstName
            this.lastName = lastName
            this.mobileNumber = mobileNumber
            this.password = password
            this.username = username
        }
    }
}