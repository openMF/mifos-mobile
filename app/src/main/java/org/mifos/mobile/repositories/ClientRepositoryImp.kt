package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.utils.Constants
import javax.inject.Inject

class ClientRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    ClientRepository {

    private val preferencesHelper = dataManager.preferencesHelper

    override fun loadClient(): Observable<Page<Client?>?>? {
        return dataManager.clients
    }

    /**
     * Save the authentication token from the server and the user ID.
     * The authentication token would be used for accessing the authenticated
     * APIs.
     *
     * @param user - The user that is to be saved.
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