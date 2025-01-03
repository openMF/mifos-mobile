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
import okhttp3.ResponseBody
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
    private val notificationRepository: NotificationRepository,
) : HomeRepository {

    override fun clientAccounts(): Flow<ClientAccounts> {
        return flow {
            emit(dataManager.clientAccounts())
        }
    }

    override fun currentClient(): Flow<Client> {
        return flow {
            emit(dataManager.currentClient())
        }
    }

    override fun clientImage(): Flow<ResponseBody> {
        return flow {
            emit(dataManager.clientImage())
        }
    }

    override suspend fun unreadNotificationsCount(): Flow<Int> {
        return notificationRepository.getUnReadNotificationCount()
    }
}
