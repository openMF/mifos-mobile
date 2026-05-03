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
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.util.asMifosDataStateFlow
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mobilenativefoundation.store.store5.Store
import template.core.base.store.streamData

class AccountsRepositoryImp(
    private val accountsStore: Store<Long, ClientAccounts>,
    private val ioDispatcher: CoroutineDispatcher,
) : AccountsRepository {

    override fun loadAccounts(clientId: Long?, accountType: String?): Flow<DataState<ClientAccounts>> {
        return accountsStore.streamData(clientId!!)
            .asMifosDataStateFlow()
            .flowOn(ioDispatcher)
    }
}
