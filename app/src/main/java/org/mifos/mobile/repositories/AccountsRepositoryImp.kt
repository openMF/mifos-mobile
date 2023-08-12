package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.ClientAccounts
import javax.inject.Inject

class AccountsRepositoryImp @Inject constructor(private val dataManager: DataManager): AccountsRepository {
    override fun loadClientAccounts(): Observable<ClientAccounts?>? {
        return dataManager.clientAccounts
    }

    override fun loadAccounts(accountType: String?): Observable<ClientAccounts?>? {
        return dataManager.getAccounts(accountType)
    }
}