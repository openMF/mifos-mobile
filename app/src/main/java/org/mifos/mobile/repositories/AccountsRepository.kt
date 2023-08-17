package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.models.client.ClientAccounts

interface AccountsRepository {

    fun loadAccounts(accountType: String?): Flow<ClientAccounts>

}