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
import org.mifos.mobile.core.data.repository.UserDetailRepository
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class UserDetailRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : UserDetailRepository {

    override fun registerNotification(payload: NotificationRegisterPayload?): Flow<ResponseBody> {
        return flow {
            emit(dataManager.registerNotification(payload))
        }
    }

    override fun getUserNotificationId(id: Long): Flow<NotificationUserDetail> {
        return flow {
            emit(dataManager.getUserNotificationId(id))
        }
    }

    override fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?,
    ): Flow<ResponseBody> {
        return flow {
            emit(dataManager.updateRegisterNotification(id, payload))
        }
    }
}
