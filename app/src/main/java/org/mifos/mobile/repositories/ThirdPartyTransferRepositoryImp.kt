package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import retrofit2.Response
import javax.inject.Inject

class ThirdPartyTransferRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    ThirdPartyTransferRepository {

    override suspend fun thirdPartyTransferTemplate(): Flow<AccountOptionsTemplate> {
        return flow {
            emit(dataManager.thirdPartyTransferTemplate())
        }
    }

}