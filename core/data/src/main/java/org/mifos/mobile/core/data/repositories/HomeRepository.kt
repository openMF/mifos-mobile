package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts

interface HomeRepository {

    fun clientAccounts(): Flow<ClientAccounts>

    fun currentClient(): Flow<Client>

    fun clientImage(): Flow<ResponseBody>

    fun unreadNotificationsCount(): Flow<Int>

}