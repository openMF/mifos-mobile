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

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface SavingAccountsListService {
    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}")
    fun getSavingsWithAssociations(
        @Path("accountId") accountId: Long,
        @Query("associations") associationType: String?,
    ): Flow<SavingsWithAssociations>

    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template")
    fun accountTransferTemplate(
        @Query("fromAccountId") accountId: Long?,
        @Query("fromAccountType") accountType: Long?,
    ): Flow<AccountOptionsTemplate>

    @POST(ApiEndPoints.ACCOUNT_TRANSFER)
    suspend fun makeTransfer(@Body transferPayload: TransferPayload?): HttpResponse

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/template")
    fun getSavingsAccountApplicationTemplate(
        @Query("clientId") clientId: Long?,
    ): Flow<SavingsAccountTemplate>

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS)
    suspend fun submitSavingAccountApplication(
        @Body payload: SavingsAccountApplicationPayload?,
    ): HttpResponse

    @PUT(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountsId}")
    suspend fun updateSavingsAccountUpdate(
        @Path("accountsId") accountsId: Long,
        @Body payload: SavingsAccountUpdatePayload?,
    ): HttpResponse

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}?command=withdrawnByApplicant")
    suspend fun submitWithdrawSavingsAccount(
        @Path("savingsId") savingsId: String,
        @Body payload: SavingsAccountWithdrawPayload?,
    ): HttpResponse
}
