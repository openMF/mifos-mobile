package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate

interface ThirdPartyTransferRepository {

    suspend fun thirdPartyTransferTemplate(): Flow<AccountOptionsTemplate>

}