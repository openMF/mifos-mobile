package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client

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

    fun loadClient() : Observable<Page<Client?>?>?

    fun saveAuthenticationTokenForSession(user: User)

    fun reInitializeService()

    fun setClientId(clientId: Long?)

    fun clearPrefHelper()

}
