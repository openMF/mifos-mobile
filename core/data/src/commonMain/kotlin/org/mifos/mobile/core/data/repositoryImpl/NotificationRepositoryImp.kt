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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.model.entity.MifosNotification

class NotificationRepositoryImp(
//    private val notificationDao: MifosNotificationDao,
    private val ioDispatcher: CoroutineDispatcher,
) : NotificationRepository {

    override fun loadNotifications(): Flow<DataState<List<MifosNotification>>> {
//        return notificationDao.getNotifications()
//            .map { it.map { it.toModel() } }
//            .flowOn(ioDispatcher)
        return flowOf(DataState.Success(emptyList<MifosNotification>()))
            .flowOn(ioDispatcher)
    }

    override fun getUnReadNotificationCount(): Flow<DataState<Int>> {
//        return notificationDao.getUnreadNotificationsCount().flowOn(ioDispatcher)
        return flowOf(DataState.Success(0))
            .flowOn(ioDispatcher)
    }

    override suspend fun saveNotification(notification: MifosNotification) {
//        withContext(ioDispatcher) {
//            notificationDao.saveNotification(notification.toEntity())
//        }
    }

    override suspend fun deleteOldNotifications() {
//        return withContext(ioDispatcher) {
//            val thirtyDaysInMillis = 2592000000L
//            val cutoffTime = System.currentTimeMillis() - thirtyDaysInMillis
//            notificationDao.deleteOldNotifications(cutoffTime)
//        }
    }

    override suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean) {
//        withContext(ioDispatcher) {
//            notificationDao.updateReadStatus(notification.timeStamp, isRead)
//        }
    }
}
