package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page

interface ClientChargeRepository {

    fun getClientCharges(clientId: Long): Observable<Page<Charge?>?>?
    fun getLoanCharges(loanId: Long): Observable<List<Charge?>?>?
    fun getSavingsCharges(savingsId: Long): Observable<List<Charge?>?>?
    fun clientLocalCharges(): Observable<Page<Charge?>?>
}