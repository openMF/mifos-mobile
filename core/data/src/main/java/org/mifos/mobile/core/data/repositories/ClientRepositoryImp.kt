package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.SelfServiceOkHttpClient
import org.mifos.mobile.core.datastore.PreferencesHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Credentials
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.client.Client
import retrofit2.Retrofit
import javax.inject.Inject

class ClientRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
    private val preferencesHelper: org.mifos.mobile.core.datastore.PreferencesHelper,
    private var retrofit: Retrofit
) : org.mifos.mobile.core.data.repositories.ClientRepository {


    override suspend fun loadClient(): Flow<Page<Client>> {
        return flow {
            emit(dataManager.clients())
        }
    }

    /**
     * Save the authentication token from the server and the user ID.
     * The authentication token would be used for accessing the authenticated
     * APIs.
     *
     * @param user - The user that is to be saved.
     */
    override fun saveAuthenticationTokenForSession(user: User) {
        val authToken = org.mifos.mobile.core.common.Constants.BASIC + user.base64EncodedAuthenticationKey
        preferencesHelper.userName = user.username
        preferencesHelper.userId = user.userId
        preferencesHelper.saveToken(authToken)
        reInitializeService()
    }

    override fun setClientId(clientId: Long?) {
        preferencesHelper.clientId = clientId
        dataManager.clientId = clientId
    }

    override fun clearPrefHelper() {
        preferencesHelper.clear()
    }

    override fun reInitializeService() {
        retrofit.newBuilder().client(
            SelfServiceOkHttpClient(
                preferencesHelper
            ).mifosOkHttpClient
        )
    }

    override fun updateAuthenticationToken(password: String) {
        val authenticationToken = Credentials.basic(preferencesHelper.userName!!, password)
        preferencesHelper.saveToken(authenticationToken)
        reInitializeService()
    }
}