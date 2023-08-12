package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.models.client.ClientAccounts

interface AccountsRepository {
    fun loadClientAccounts() : Observable<ClientAccounts>
    fun loadAccounts(accountType: String?) : Observable<ClientAccounts>
}