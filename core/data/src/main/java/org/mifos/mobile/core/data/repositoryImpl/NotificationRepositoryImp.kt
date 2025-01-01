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
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.network.Dispatcher
import org.mifos.mobile.core.common.network.MifosDispatchers
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.database.dao.MifosNotificationDao
import org.mifos.mobile.core.database.entity.MifosNotificationEntity
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val notificationDao: MifosNotificationDao,
    @Dispatcher(MifosDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher,
) : NotificationRepository {

    override suspend fun saveNotification(notification: MifosNotificationEntity) {
        notificationDao.saveNotification(notification)
    }

    override suspend fun loadNotifications(): Flow<List<MifosNotificationEntity>> {
        return withContext(ioDispatcher) {
            notificationDao.getNotifications()
        }
    }

    override suspend fun getUnReadNotificationCount(): Flow<Int> {
        return notificationDao.getUnreadNotificationsCount()
    }

    override suspend fun deleteOldNotifications() {
        return withContext(ioDispatcher) {
            val thirtyDaysInMillis = 2592000000L
            val cutoffTime = System.currentTimeMillis() - thirtyDaysInMillis
            notificationDao.deleteOldNotifications(cutoffTime)
        }
    }

    override suspend fun updateReadStatus(notification: MifosNotificationEntity, isRead: Boolean) {
        notificationDao.updateReadStatus(notification.timeStamp, isRead)
    }
}
