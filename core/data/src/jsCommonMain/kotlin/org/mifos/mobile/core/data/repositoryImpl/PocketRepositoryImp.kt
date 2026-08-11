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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.PocketRepository
import org.mifos.mobile.core.model.entity.payload.PocketLinkPayload
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.entity.pocket.LinkableAccount
import org.mifos.mobile.core.model.entity.pocket.PocketAccount

class PocketRepositoryImp : PocketRepository {
    override suspend fun getPocketAccounts(): DataState<List<PocketAccount>> {
        return DataState.Success(emptyList())
    }

    override fun getDetailedPocketAccounts(
        clientId: Long,
        forceRefresh: Boolean,
    ): Flow<DataState<List<DetailedPocketAccount>>> {
        return flowOf(DataState.Success(emptyList()))
    }

    override suspend fun linkAccounts(
        payload: PocketLinkPayload,
        explicitlyAddedAccounts: List<DetailedPocketAccount>,
        clientId: Long,
    ): DataState<Unit> {
        return DataState.Success(Unit)
    }

    override suspend fun delinkAccounts(
        pocketAccountMappingIds: List<Long>,
        clientId: Long,
    ): DataState<Unit> {
        return DataState.Success(Unit)
    }

    override fun getAvailableAccountsToLink(clientId: Long): Flow<DataState<List<LinkableAccount>>> {
        return flowOf(DataState.Success(emptyList()))
    }

    override suspend fun resetPocketCache() {}
}
