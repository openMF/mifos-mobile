package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.datastore.model.Charge
import org.mifos.mobile.core.model.entity.Page
import javax.inject.Inject

class ClientChargeRepositoryImp @Inject constructor(private val dataManager: DataManager) :
   ClientChargeRepository {

    override suspend fun getClientCharges(clientId: Long): Flow<Page<Charge>> {
        return flow {
            emit(dataManager.getClientCharges(clientId))
        }
    }

    override suspend fun getLoanCharges(loanId: Long): Flow<List<Charge>> {
        return flow {
            emit(dataManager.getLoanCharges(loanId))
        }
    }

    override suspend fun getSavingsCharges(savingsId: Long): Flow<List<Charge>> {
        return flow {
            emit(dataManager.getSavingsCharges(savingsId))
        }
    }

    override suspend fun clientLocalCharges(): Flow<Page<Charge?>> {
        return flow {
            emit(dataManager.clientLocalCharges())
        }
    }
}