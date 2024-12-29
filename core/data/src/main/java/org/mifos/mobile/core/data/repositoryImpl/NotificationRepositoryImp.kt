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
import kotlinx.coroutines.flow.map
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.datastore.dao.MifosNotificationDao
import org.mifos.mobile.core.datastore.entity.MifosNotification
import org.mifos.mobile.core.datastore.utils.NotificationComparator
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val notificationDao: MifosNotificationDao,
) : NotificationRepository {

    override suspend fun saveNotification(notification: MifosNotification) {
        notificationDao.saveNotification(notification)
    }

    override suspend fun loadNotifications(): Flow<List<MifosNotification>> {
        return notificationDao.getNotifications()
            .map { notifications ->
                notifications.sortedWith(NotificationComparator())
            }
    }

    override suspend fun getUnReadNotificationCount(): Flow<Int> {
        return notificationDao.getUnreadNotificationsCount()
    }

    override suspend fun deleteOldNotifications() {
        val thirtyDaysInMillis = 2592000000L
        val cutoffTime = System.currentTimeMillis() - thirtyDaysInMillis

        notificationDao.deleteOldNotifications(cutoffTime)
    }

    override suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean) {
        notificationDao.updateReadStatus(notification.timeStamp, isRead)
    }
}
