/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.fakeServices

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.services.ClientService

class FakeClientService(
    private val accounts: ClientAccounts? = null,
    private val shouldFail: Boolean = false,
) : ClientService {

    override fun getAccounts(clientId: Long, accountType: String?): Flow<ClientAccounts> =
        flow {
            if (shouldFail) {
                throw RuntimeException("Network error")
            }
            emit(accounts ?: ClientAccounts())
        }

    override fun clients(): Flow<Page<Client>> = TODO()
    override fun getClientForId(clientId: Long): Flow<Client> = TODO()
    override fun getClientImage(clientId: Long): Flow<HttpResponse> = TODO()
    override fun getClientAccounts(clientId: Long): Flow<ClientAccounts> = TODO()
}
