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
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class AccountsRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : AccountsRepository {

    override fun loadAccounts(accountType: String?): Flow<ClientAccounts> {
        return flow {
            emit(dataManager.getAccounts(accountType))
        }
    }
}
