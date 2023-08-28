package org.mifos.mobile.repositories

import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client
import retrofit2.Response

interface ClientRepository {

    suspend fun loadClient(): Response<Page<Client?>?>?

    fun saveAuthenticationTokenForSession(user: User)

    fun setClientId(clientId: Long?)

    fun clearPrefHelper()

    fun reInitializeService()

    fun updateAuthenticationToken(password: String)
}