package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(private val dataManager: DataManager) : HomeRepository {

    override fun clientAccounts(): Flow<ClientAccounts> {
        return flow {
            emit(dataManager.clientAccounts())
        }
    }

    override fun currentClient(): Flow<Client> {
        return flow {
            emit(dataManager.currentClient())
        }
    }

    override fun clientImage(): Flow<ResponseBody> {
        return flow {
            emit(dataManager.clientImage())
        }
    }

    override fun unreadNotificationsCount(): Flow<Int> {
        return flow {
            dataManager.unreadNotificationsCount()
        }
    }

}