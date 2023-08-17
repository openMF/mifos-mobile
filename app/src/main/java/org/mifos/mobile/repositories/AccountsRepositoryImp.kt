package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.ClientAccounts
import javax.inject.Inject

class AccountsRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    AccountsRepository {

    override fun loadAccounts(accountType: String?): Flow<ClientAccounts> {
        return flow {
            emit(dataManager.getAccounts(accountType))
        }
    }
}