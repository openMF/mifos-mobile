package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client

interface ClientRepository {

    fun loadClient() : Observable<Page<Client?>?>?

    fun saveAuthenticationTokenForSession(user: User)

    fun reInitializeService()

    fun setClientId(clientId: Long?)

    fun clearPrefHelper()
}