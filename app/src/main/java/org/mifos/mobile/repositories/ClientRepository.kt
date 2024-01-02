package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client

interface ClientRepository {

    suspend fun loadClient(): Flow<Page<Client>>

    fun saveAuthenticationTokenForSession(user: User)

    fun setClientId(clientId: Long?)

    fun clearPrefHelper()

    fun reInitializeService()

    fun updateAuthenticationToken(password: String)
}