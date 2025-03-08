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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.model.entity.TransferResponse
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.network.DataManager

class TransferRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : TransferRepository {
    override suspend fun makeTransfer(
        payload: TransferPayload,
        transferType: TransferType?,
    ): DataState<TransferResponse> {
        return try {
            val response = withContext(ioDispatcher) {
                when (transferType) {
                    TransferType.SELF -> dataManager.savingAccountsListApi.makeTransfer(payload)
                    else -> dataManager.thirdPartyTransferApi.makeTransfer(payload)
                }
            }

            DataState.Success(response)
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }
}
