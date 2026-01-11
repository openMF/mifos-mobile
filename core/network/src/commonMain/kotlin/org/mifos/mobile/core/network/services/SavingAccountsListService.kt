/*
 * Copyright 2026 Mifos Initiative
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
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountUpdatePayloadDto
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountWithdrawPayloadDto
import org.mifos.mobile.core.network.dto.payloads.TransferPayloadDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsAccountApplicationPayloadDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.templates.accounts.AccountOptionsTemplateResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.SavingsAccountTemplateResponseDto
import org.mifos.mobile.core.network.dto.transaction.SavingsTransactionDetailsResponseDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface SavingAccountsListService {
    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}")
    fun getSavingsWithAssociations(
        @Path("accountId") accountId: Long,
        @Query("associations") associationType: String?,
    ): Flow<SavingsWithAssociationsResponseDto>

    @GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template")
    fun accountTransferTemplate(
        @Query("fromAccountId") accountId: Long?,
        @Query("fromAccountType") accountType: Long?,
    ): Flow<AccountOptionsTemplateResponseDto>

    @POST(ApiEndPoints.ACCOUNT_TRANSFER)
    suspend fun makeTransfer(@Body transferPayload: TransferPayloadDto?): HttpResponse

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/template")
    fun getSavingsAccountApplicationTemplate(
        @Query("clientId") clientId: Long?,
    ): Flow<SavingsAccountTemplateResponseDto>

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/template")
    fun getSavingsAccountApplicationTemplateByProduct(
        @Query("clientId") clientId: Long?,
        @Query("productId") productId: Long?,
    ): Flow<SavingsAccountTemplateResponseDto>

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS)
    suspend fun submitSavingAccountApplication(
        @Body payload: SavingsAccountApplicationPayloadDto?,
    ): HttpResponse

    @PUT(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountsId}")
    suspend fun updateSavingsAccountUpdate(
        @Path("accountsId") accountsId: Long,
        @Body payload: SavingsAccountUpdatePayloadDto?,
    ): HttpResponse

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}?command=withdrawnByApplicant")
    suspend fun submitWithdrawSavingsAccount(
        @Path("savingsId") savingsId: Long,
        @Body payload: SavingsAccountWithdrawPayloadDto?,
    ): HttpResponse

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}/transactions/{transactionId}")
    fun getSavingsAccountTransactionDetails(
        @Path("accountId") savingsId: Long,
        @Path("transactionId") transactionId: Long,
    ): Flow<SavingsTransactionDetailsResponseDto>
}
