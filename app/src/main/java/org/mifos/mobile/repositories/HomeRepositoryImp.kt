package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(private val dataManager: DataManager) : HomeRepository {

    override fun clientAccounts(): Observable<ClientAccounts> {
        return dataManager.clientAccounts
    }

    override fun currentClient(): Observable<Client> {
        return dataManager.currentClient
    }

    override fun clientImage(): Observable<ResponseBody> {
        return dataManager.clientImage
    }

    override fun unreadNotificationsCount(): Observable<Int> {
        return dataManager.unreadNotificationsCount
    }

}