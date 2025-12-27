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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.repository.ClientRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.network.DataManagerProvider

class ClientRepositoryImp(
    private val dataManager: DataManagerProvider,
    private val ioDispatcher: CoroutineDispatcher,
) : ClientRepository {

    val clientsApi = requireNotNull(dataManager.clientsApi) {
        "ClientService must be provided"
    }

    override fun loadClient(): Flow<DataState<Page<Client>>> {
        return clientsApi.clients()
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
