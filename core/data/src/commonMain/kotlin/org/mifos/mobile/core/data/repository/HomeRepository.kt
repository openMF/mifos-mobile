/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repository

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifospay.core.common.DataState

interface HomeRepository {

    fun clientAccounts(clientId: Long): Flow<DataState<ClientAccounts>>

    fun currentClient(clientId: Long): Flow<DataState<Client>>

    fun clientImage(clientId: Long): Flow<DataState<HttpResponse>>

    fun unreadNotificationsCount(): Flow<DataState<Int>>
}
