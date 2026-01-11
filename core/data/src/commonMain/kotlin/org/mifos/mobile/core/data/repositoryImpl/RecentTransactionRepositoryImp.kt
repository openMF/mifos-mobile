/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.mapper.toPageModel
import org.mifos.mobile.core.data.mapper.transactions.toRecentTransactionModel
import org.mifos.mobile.core.data.repository.RecentTransactionRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.network.DataManager

class RecentTransactionRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : RecentTransactionRepository {
    override fun recentTransactions(
        clientId: Long?,
        offset: Int?,
        limit: Int?,
    ): Flow<DataState<Page<Transaction>>> {
        return dataManager.recentTransactionsApi.getRecentTransactionsList(
            clientId!!,
            offset,
            limit,
        )
            .map { response ->
                response.toPageModel { dto ->
                    dto.toRecentTransactionModel()
                }
            }
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
