package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page

interface ClientChargeRepository {

    suspend fun getClientCharges(clientId: Long): Flow<Page<Charge>>
    suspend fun getLoanCharges(loanId: Long): Flow<List<Charge>>
    suspend fun getSavingsCharges(savingsId: Long): Flow<List<Charge>>
    suspend fun clientLocalCharges(): Flow<Page<Charge?>>
}