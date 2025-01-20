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

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.MifosNotification
import org.mifospay.core.common.DataState

interface NotificationRepository {

    fun loadNotifications(): Flow<DataState<List<MifosNotification>>>

    fun getUnReadNotificationCount(): Flow<DataState<Int>>

    suspend fun saveNotification(notification: MifosNotification)

    suspend fun deleteOldNotifications()

    suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean)
}
