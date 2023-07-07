package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.utils.Constants
import javax.inject.Inject

class UserAuthRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    UserAuthRepository {

    private val preferencesHelper = dataManager.preferencesHelper

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

    override fun login(username: String, password: String): Observable<User?>? {
        val loginPayload = LoginPayload().apply {
            this.username = username
            this.password = password
        }
        return dataManager.login(loginPayload)
    }

    override fun loadClient(): Observable<Page<Client?>?>? {
        return dataManager.clients
    }

    /**
     * Save the authentication token from the server and the user ID.
     * The authentication token would be used for accessing the authenticated
     * APIs.
     *
     * @param userID    - The userID of the user to be saved.
     * @param authToken - The authentication token to be saved.
     */
    override fun saveAuthenticationTokenForSession(user: User) {
        val authToken = Constants.BASIC + user.base64EncodedAuthenticationKey
        preferencesHelper.userName = user.username
        preferencesHelper.userId = user.userId
        preferencesHelper.saveToken(authToken)
        reInitializeService()
    }

    override fun reInitializeService() {
        BaseApiManager.createService(
            preferencesHelper.baseUrl,
            preferencesHelper.tenant,
            preferencesHelper.token,
        )
    }

    override fun setClientId(clientId: Long?) {
        preferencesHelper.clientId = clientId
        dataManager.clientId = clientId
    }

    override fun clearPrefHelper() {
        preferencesHelper.clear()
    }
}