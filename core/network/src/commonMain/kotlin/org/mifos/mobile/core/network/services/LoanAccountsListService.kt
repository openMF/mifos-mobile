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
import org.mifos.mobile.core.network.dto.loanAccount.LoanAccountResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.payloads.LoanAccountApplicationPayloadDto
import org.mifos.mobile.core.network.dto.payloads.LoanWithdrawPayloadDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanTemplateResponseDto
import org.mifos.mobile.core.network.dto.transaction.LoanTransactionDetailsResponseDto
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface LoanAccountsListService {
    @GET(ApiEndPoints.LOANS + "/{loanId}/")
    fun getLoanAccountsDetail(@Path("loanId") loanId: Long): Flow<LoanAccountResponseDto>?

    @GET(ApiEndPoints.LOANS + "/{loanId}")
    fun getLoanWithAssociations(
        @Path("loanId") loanId: Long,
        @Query("associations") associationType: String?,
    ): Flow<LoanWithAssociationsResponseDto>

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    fun getLoanTemplate(@Query("clientId") clientId: Long?): Flow<LoanTemplateResponseDto>

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    fun getLoanTemplateByProduct(
        @Query("clientId") clientId: Long?,
        @Query("productId") productId: Int?,
    ): Flow<LoanTemplateResponseDto>

    @POST(ApiEndPoints.LOANS)
    suspend fun createLoansAccount(@Body loansPayload: LoanAccountApplicationPayloadDto?): HttpResponse

    @PUT(ApiEndPoints.LOANS + "/{loanId}/")
    suspend fun updateLoanAccount(
        @Path("loanId") loanId: Long,
        @Body loansPayload: LoanAccountApplicationPayloadDto?,
    ): HttpResponse

    @POST(ApiEndPoints.LOANS + "/{loanId}?command=withdrawnByApplicant")
    suspend fun withdrawLoanAccount(
        @Path("loanId") loanId: Long,
        @Body loanWithdraw: LoanWithdrawPayloadDto?,
    ): HttpResponse

    @GET(ApiEndPoints.LOANS + "/{loanId}/transactions/{transactionId}")
    fun getLoanTransactionDetails(
        @Path("loanId") loanId: Long,
        @Path("transactionId") transactionId: Long,
    ): Flow<LoanTransactionDetailsResponseDto>
}
