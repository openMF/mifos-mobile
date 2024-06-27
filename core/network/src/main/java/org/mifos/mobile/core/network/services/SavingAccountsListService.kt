package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.common.ApiEndPoints
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
