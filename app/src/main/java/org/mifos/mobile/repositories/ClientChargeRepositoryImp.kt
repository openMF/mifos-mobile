package org.mifos.mobile.repositories

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import retrofit2.Response
import javax.inject.Inject

class ClientChargeRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    ClientChargeRepository {

    override suspend fun getClientCharges(clientId: Long): Response<Page<Charge?>?>? {
        return dataManager.getClientCharges(clientId)
    }

    override suspend fun getLoanCharges(loanId: Long): Response<List<Charge?>?>? {
        return dataManager.getLoanCharges(loanId)
    }

    override suspend fun getSavingsCharges(savingsId: Long): Response<List<Charge?>?>? {
        return dataManager.getSavingsCharges(savingsId)
    }

    override suspend fun clientLocalCharges(): Response<Page<Charge?>?> {
        return dataManager.clientLocalCharges()
    }
}