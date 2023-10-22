package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate

interface ThirdPartyTransferRepository {

    suspend fun thirdPartyTransferTemplate(): Flow<AccountOptionsTemplate>

}