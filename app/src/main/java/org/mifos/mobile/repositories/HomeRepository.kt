package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts

interface HomeRepository {

    fun clientAccounts(): Flow<ClientAccounts>

    fun currentClient(): Flow<Client>

    fun clientImage(): Flow<ResponseBody>

    fun unreadNotificationsCount(): Flow<Int>

}