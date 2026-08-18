/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.mifos.mobile.core.database.dao.MifosNotificationDao
import org.mifos.mobile.core.database.entity.MifosNotificationEntity

class FakeMifosNotificationDao : MifosNotificationDao {

    var returnError: Boolean = false

    private val notificationsFlow = MutableStateFlow<List<MifosNotificationEntity>>(emptyList())

    override fun getNotifications(): Flow<List<MifosNotificationEntity>> {
        if (returnError) {
            return kotlinx.coroutines.flow.flow { throw Exception("Dummy error") }
        }
        return notificationsFlow
    }

    override fun getUnreadNotificationsCount(): Flow<Int> {
        return notificationsFlow.map { list -> list.count { it.read == false } }
    }

    override suspend fun saveNotification(notification: MifosNotificationEntity) {
        notificationsFlow.update { currentList ->
            val index = currentList.indexOfFirst { it.timeStamp == notification.timeStamp }
            if (index == -1) {
                currentList + notification
            } else {
                currentList.toMutableList().apply { this[index] = notification }
            }
        }
    }

    override suspend fun deleteOldNotifications(cutoffTime: Long) {
        notificationsFlow.update { currentList ->
            currentList.filter { it.timeStamp >= cutoffTime }
        }
    }

    override suspend fun updateReadStatus(timeStamp: Long, isRead: Boolean) {
        notificationsFlow.update { currentList ->
            val index = currentList.indexOfFirst { it.timeStamp == timeStamp }
            if (index == -1) {
                currentList
            } else {
                currentList.toMutableList().apply {
                    this[index] = this[index].copy(read = isRead)
                }
            }
        }
    }

    fun populateNotifications(notifications: List<MifosNotificationEntity>) {
        notificationsFlow.value = notifications
    }
}
