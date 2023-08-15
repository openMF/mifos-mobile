package org.mifos.mobile.repositories

import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import retrofit2.Response

interface ClientChargeRepository {

    suspend fun getClientCharges(clientId: Long): Response<Page<Charge?>?>?
    suspend fun getLoanCharges(loanId: Long): Response<List<Charge?>?>?
    suspend fun getSavingsCharges(savingsId: Long): Response<List<Charge?>?>?
    suspend fun clientLocalCharges(): Response<Page<Charge?>?>
}