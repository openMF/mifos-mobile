package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import javax.inject.Inject

class AccountsRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    AccountsRepository {

    override fun loadAccounts(accountType: String?): Flow<ClientAccounts> {
        return flow {
            emit(dataManager.getAccounts(accountType))
        }
    }
}