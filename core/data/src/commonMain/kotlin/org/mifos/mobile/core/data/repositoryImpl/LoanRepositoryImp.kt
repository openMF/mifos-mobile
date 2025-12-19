/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.mapper.toDomain
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.extractErrorMessage
import org.mifos.mobile.core.model.entity.TransactionDetails
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.network.DataManager

class LoanRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : LoanRepository {

    override fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?,
    ): Flow<DataState<LoanWithAssociations?>> = flow {
        try {
            dataManager.loanAccountsListApi.getLoanWithAssociations(loanId!!, associationType)
                .collect { response ->
                    emit(DataState.Success(response))
                }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }.flowOn(ioDispatcher)

    override fun getLoanTransactionDetails(
        loanId: Long,
        transactionId: Long,
    ): Flow<DataState<TransactionDetails>> {
        return dataManager.loanAccountsListApi
            .getLoanTransactionDetails(loanId, transactionId)
            .map { it.toDomain() }
            .asDataStateFlow()
            .flowOn(ioDispatcher)
    }

    override suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    dataManager.loanAccountsListApi.withdrawLoanAccount(loanId!!, loanWithdraw)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override fun template(clientId: Long?): Flow<DataState<LoanTemplate?>> {
        return dataManager.loanAccountsListApi.getLoanTemplate(clientId = clientId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override fun getLoanTemplateByProduct(clientId: Long?, productId: Int?): Flow<DataState<LoanTemplate?>> {
        return dataManager.loanAccountsListApi.getLoanTemplateByProduct(clientId, productId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
