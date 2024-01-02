package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import retrofit2.Response
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