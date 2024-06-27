package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import javax.inject.Inject

class ThirdPartyTransferRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    ThirdPartyTransferRepository {

    override suspend fun thirdPartyTransferTemplate(): Flow<AccountOptionsTemplate> {
        return flow {
            emit(dataManager.thirdPartyTransferTemplate())
        }
    }

}