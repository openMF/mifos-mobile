package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.client.Client

interface ClientRepository {

    suspend fun loadClient(): Flow<Page<Client>>

    fun saveAuthenticationTokenForSession(user: User)

    fun setClientId(clientId: Long?)

    fun clearPrefHelper()

    fun reInitializeService()

    fun updateAuthenticationToken(password: String)
}