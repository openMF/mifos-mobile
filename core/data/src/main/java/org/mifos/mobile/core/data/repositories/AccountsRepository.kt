package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.client.ClientAccounts

interface AccountsRepository {

    fun loadAccounts(accountType: String?): Flow<ClientAccounts>

}