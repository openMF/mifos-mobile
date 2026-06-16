/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.entity.pocket.LinkableAccount
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.network.dto.pocket.PocketLinkRequest

interface PocketRepository {

    suspend fun getPocketAccounts(): DataState<List<PocketAccount>>

    fun getDetailedPocketAccounts(
        clientId: Long,
        forceRefresh: Boolean = false,
    ): Flow<DataState<List<DetailedPocketAccount>>>

    fun getAvailableAccountsToLink(
        clientId: Long,
    ): Flow<DataState<List<LinkableAccount>>>

    suspend fun linkAccounts(
        request: PocketLinkRequest,
        explicitlyAddedAccount: DetailedPocketAccount,
    ): DataState<Unit>

    suspend fun delinkAccounts(
        pocketAccountMappingIds: List<Long>,
    ): DataState<Unit>
}
