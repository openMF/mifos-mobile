package org.mifos.mobile.repositories

import okhttp3.Credentials
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.local.PreferencesHelper
import javax.inject.Inject

class ClientRepositoryImp @Inject constructor(private val preferencesHelper: PreferencesHelper) : ClientRepository {

    override fun updateAuthenticationToken(password: String) {
        val authenticationToken = Credentials.basic(preferencesHelper.userName!!, password)
        preferencesHelper.saveToken(authenticationToken)
        BaseApiManager.createService(
            preferencesHelper.baseUrl,
            preferencesHelper.tenant,
            preferencesHelper.token,
        )
    }
}