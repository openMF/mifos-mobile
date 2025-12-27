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
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.data.util.extractErrorMessage
import org.mifos.mobile.core.model.entity.TransferResponse
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.network.DataManagerProvider

class TransferRepositoryImp(
    private val dataManager: DataManagerProvider,
    private val ioDispatcher: CoroutineDispatcher,
) : TransferRepository {

    val savingAccountsListApi = requireNotNull(dataManager.savingAccountsListApi) {
        "SavingAccountsListService must be provided"
    }

    val thirdPartyTransferApi = requireNotNull(dataManager.thirdPartyTransferApi) {
        "ThirdPartyTransferService must be provided"
    }

    override suspend fun makeTransfer(
        payload: TransferPayload,
        transferType: TransferType?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = when (transferType) {
                    TransferType.SELF -> savingAccountsListApi.makeTransfer(payload)
                    else -> thirdPartyTransferApi.makeTransfer(payload)
                }

                val transferResponse = Json.decodeFromString<TransferResponse>(response.bodyAsText())
                DataState.Success(transferResponse.resourceId.toString())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Something went wrong on our end. Please try again later."), null)
            }
        }
    }
}
