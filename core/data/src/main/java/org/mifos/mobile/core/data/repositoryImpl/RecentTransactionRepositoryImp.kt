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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.data.repository.RecentTransactionRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class RecentTransactionRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : RecentTransactionRepository {

    override suspend fun recentTransactions(
        offset: Int?,
        limit: Int?,
    ): Flow<Page<Transaction>> {
        return flow {
            if (offset != null && limit != null) {
                emit(dataManager.getRecentTransactions(offset = offset, limit = limit))
            }
        }
    }
}
