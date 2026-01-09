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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.mapper.toDomain
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.extractErrorMessage
import org.mifos.mobile.core.model.entity.TransactionDetails
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.network.DataManager

class SavingsAccountRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : SavingsAccountRepository {

    override fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Flow<DataState<SavingsWithAssociations>> {
        return dataManager.savingAccountsListApi.getSavingsWithAssociations(
            accountId!!,
            associationType,
        ).asDataStateFlow().flowOn(ioDispatcher)
    }

    override fun getSavingsAccountTransactionDetails(
        accountId: Long,
        transactionId: Long,
    ): Flow<DataState<TransactionDetails>> {
        return dataManager.savingAccountsListApi
            .getSavingsAccountTransactionDetails(accountId, transactionId)
            .map { it.toDomain() }
            .asDataStateFlow()
            .flowOn(ioDispatcher)
    }

    override fun getSavingAccountApplicationTemplate(
        clientId: Long?,
    ): Flow<DataState<SavingsAccountTemplate>> {
        return dataManager.savingAccountsListApi.getSavingsAccountApplicationTemplate(clientId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override fun getSavingAccountApplicationTemplateByProduct(
        clientId: Long?,
        productId: Long?,
    ): Flow<DataState<SavingsAccountTemplate>> {
        return dataManager.savingAccountsListApi.getSavingsAccountApplicationTemplateByProduct(
            clientId,
            productId,
        ).asDataStateFlow().flowOn(ioDispatcher)
    }

    override suspend fun submitSavingAccountApplication(
        payload: SavingsAccountApplicationPayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    dataManager.savingAccountsListApi.submitSavingAccountApplication(payload)
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

    override suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    dataManager.savingAccountsListApi.updateSavingsAccountUpdate(accountId!!, payload)
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

    override suspend fun submitWithdrawSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountWithdrawPayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    dataManager.savingAccountsListApi.submitWithdrawSavingsAccount(accountId!!, payload)
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

    override fun accountTransferTemplate(
        accountId: Long?,
        accountType: Long?,
    ): Flow<DataState<AccountOptionsTemplate>> {
        return dataManager.savingAccountsListApi.accountTransferTemplate(accountId!!, accountType)
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
