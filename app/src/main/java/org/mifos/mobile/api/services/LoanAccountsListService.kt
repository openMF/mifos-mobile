package org.mifos.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.utils.Constants
import retrofit2.http.*

/**
 * @author Vishwajeet
 * @since 23/6/16.
 */
interface LoanAccountsListService {
    @GET(ApiEndPoints.LOANS + "/{loanId}/")
    fun getLoanAccountsDetail(@Path("loanId") loanId: Long): Observable<LoanAccount?>?

    @GET(ApiEndPoints.LOANS + "/{loanId}")
    suspend fun getLoanWithAssociations(
        @Path("loanId") loanId: Long?,
        @Query("associations") associationType: String?,
    ): LoanWithAssociations

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    suspend fun getLoanTemplate(@Query("clientId") clientId: Long?): LoanTemplate

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    suspend fun getLoanTemplateByProduct(
        @Query("clientId") clientId: Long?,
        @Query("productId") productId: Int?,
    ): LoanTemplate

    @POST(ApiEndPoints.LOANS)
    suspend fun createLoansAccount(@Body loansPayload: LoansPayload?): ResponseBody

    @PUT(ApiEndPoints.LOANS + "/{loanId}/")
    suspend fun updateLoanAccount(
        @Path("loanId") loanId: Long,
        @Body loansPayload: LoansPayload?,
    ): ResponseBody

    @POST(ApiEndPoints.LOANS + "/{loanId}?command=withdrawnByApplicant")
    suspend fun withdrawLoanAccount(
        @Path(Constants.LOAN_ID) loanId: Long?,
        @Body loanWithdraw: LoanWithdraw?,
    ): ResponseBody
}
