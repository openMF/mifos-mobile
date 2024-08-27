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
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.datastore.model.MifosNotification
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : NotificationRepository {

    override suspend fun loadNotifications(): Flow<List<MifosNotification>> {
        return flow {
            emit(dataManager.notifications())
        }
    }
}
