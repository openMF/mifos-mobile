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
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.repository.UserDetailRepository
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import org.mifos.mobile.core.network.DataManager

class UserDetailRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : UserDetailRepository {

    override suspend fun registerNotification(payload: NotificationRegisterPayload?): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.notificationApi.registerNotification(payload)
            }
            DataState.Success("Notification Registered Successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override fun getUserNotificationId(id: Long): Flow<DataState<NotificationUserDetail>> {
        return dataManager.notificationApi.getUserNotificationId(id)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override suspend fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.notificationApi.updateRegisterNotification(id, payload)
            }
            DataState.Success("Notification Updated Successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }
}
