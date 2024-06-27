package org.mifos.mobile.core.network.services

import org.mifos.mobile.core.datastore.model.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.common.ApiEndPoints
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
interface ClientChargeService {

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    suspend fun getClientChargeList(@Path("clientId") clientId: Long?): Page<Charge>

    @GET(ApiEndPoints.LOANS + "/{loanId}/charges")
    suspend fun getLoanAccountChargeList(@Path("loanId") loanId: Long?): List<Charge>

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}/charges")
    suspend fun getSavingsAccountChargeList(@Path("savingsId") savingsId: Long?): List<Charge>
}
