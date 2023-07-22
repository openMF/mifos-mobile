package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import javax.inject.Inject

class ClientChargeRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    ClientChargeRepository {

    override fun getClientCharges(clientId: Long): Observable<Page<Charge?>?>? {
        return dataManager.getClientCharges(clientId)
    }

    override fun getLoanCharges(loanId: Long): Observable<List<Charge?>?>? {
        return dataManager.getLoanCharges(loanId)
    }

    override fun getSavingsCharges(savingsId: Long): Observable<List<Charge?>?>? {
        return dataManager.getSavingsCharges(savingsId)
    }

    override fun clientLocalCharges(): Observable<Page<Charge?>?> {
        return dataManager.clientLocalCharges
    }
}