package org.mifos.mobile.api.services

import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import retrofit2.Response
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
