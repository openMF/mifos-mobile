package org.mifos.mobile.api.services

import okhttp3.ResponseBody
import org.mifos.mobile.api.ApiEndPoints
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import retrofit2.Response
import retrofit2.http.*

/**
 * @author Vishwajeet
 * @since 21/6/16.
 */
interface SavingAccountsListService {
    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}")
    suspend fun getSavingsWithAssociations(
        @Path("accountId") accountId: Long?,
        @Query("associations") associationType: String?,
    ): SavingsWithAssociations

    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template")
    suspend fun accountTransferTemplate(
        @Query("fromAccountId") accountId: Long?,
        @Query("fromAccountType") accountType: Long?,
    ): AccountOptionsTemplate

    @POST(ApiEndPoints.ACCOUNT_TRANSFER)
    suspend fun makeTransfer(@Body transferPayload: TransferPayload?): ResponseBody

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/template")
    suspend fun getSavingsAccountApplicationTemplate(
        @Query("clientId") clientId: Long?,
    ): SavingsAccountTemplate

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS)
    suspend fun submitSavingAccountApplication(
        @Body payload: SavingsAccountApplicationPayload?,
    ): ResponseBody

    @PUT(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountsId}")
    suspend fun updateSavingsAccountUpdate(
        @Path("accountsId") accountsId: Long?,
        @Body payload: SavingsAccountUpdatePayload?,
    ): ResponseBody

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}?command=withdrawnByApplicant")
    suspend fun submitWithdrawSavingsAccount(
        @Path("savingsId") savingsId: String?,
        @Body payload: SavingsAccountWithdrawPayload?,
    ): ResponseBody
}
