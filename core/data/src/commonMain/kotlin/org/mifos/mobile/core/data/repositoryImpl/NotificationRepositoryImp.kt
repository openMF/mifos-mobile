/*
 * Copyright 2026 Mifos Initiative
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.database.dao.MifosNotificationDao
import org.mifos.mobile.core.database.entity.MifosNotificationEntity
import org.mifos.mobile.core.model.entity.MifosNotification
import kotlin.time.Clock

class NotificationRepositoryImp(
    private val notificationDao: MifosNotificationDao,
    private val ioDispatcher: CoroutineDispatcher,
) : NotificationRepository {

    override fun loadNotifications(): Flow<DataState<List<MifosNotification>>> {
        return notificationDao.getNotifications()
            .map { entities -> entities.map { it.toModel() } }
            .asDataStateFlow()
            .flowOn(ioDispatcher)
    }

    override fun getUnReadNotificationCount(): Flow<DataState<Int>> {
        return notificationDao.getUnreadNotificationsCount()
            .asDataStateFlow()
            .flowOn(ioDispatcher)
    }

    override suspend fun saveNotification(notification: MifosNotification) {
        withContext(ioDispatcher) {
            notificationDao.saveNotification(notification.toEntity())
        }
    }

    override suspend fun deleteOldNotifications() {
        withContext(ioDispatcher) {
            val thirtyDaysInMillis = 2592000000L
            val cutoffTime = Clock.System.now().toEpochMilliseconds() - thirtyDaysInMillis
            notificationDao.deleteOldNotifications(cutoffTime)
        }
    }

    override suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean) {
        withContext(ioDispatcher) {
            notificationDao.updateReadStatus(notification.timeStamp, isRead)
        }
    }
}

private fun MifosNotificationEntity.toModel(): MifosNotification =
    MifosNotification(timeStamp = timeStamp, msg = msg, read = read)

private fun MifosNotification.toEntity(): MifosNotificationEntity =
    MifosNotificationEntity(timeStamp = timeStamp, msg = msg, read = read)
