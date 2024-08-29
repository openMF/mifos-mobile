/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.services

import okhttp3.ResponseBody
import org.mifos.mobile.core.common.ApiEndPoints
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
