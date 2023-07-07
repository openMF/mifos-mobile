package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts

interface HomeRepository {

    fun clientAccounts(): Observable<ClientAccounts?>?
    fun currentClient(): Observable<Client?>?
    fun clientImage(): Observable<ResponseBody?>?
    fun unreadNotificationsCount(): Observable<Int>

}