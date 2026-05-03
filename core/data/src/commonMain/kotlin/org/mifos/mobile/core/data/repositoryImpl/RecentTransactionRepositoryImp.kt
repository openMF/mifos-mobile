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
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.RecentTransactionRepository
import org.mifos.mobile.core.data.util.asMifosDataStateFlow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mobilenativefoundation.store.store5.Store
import template.core.base.store.mapData
import template.core.base.store.streamData

class RecentTransactionRepositoryImp(
    private val transactionStore: Store<Long, List<Transaction>>,
    private val ioDispatcher: CoroutineDispatcher,
) : RecentTransactionRepository {
    override fun recentTransactions(
        clientId: Long?,
        offset: Int?,
        limit: Int?,
    ): Flow<DataState<Page<Transaction>>> {
        return transactionStore.streamData(clientId!!)
            .mapData { transactions -> Page(transactions.size, transactions) }
            .asMifosDataStateFlow()
            .flowOn(ioDispatcher)
    }
}
