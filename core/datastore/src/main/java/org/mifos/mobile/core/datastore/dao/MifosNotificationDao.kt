/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.datastore.entity.MifosNotification

@Dao
interface MifosNotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNotification(notification: MifosNotification)

    @Query("SELECT * FROM mifos_notifications ORDER BY timeStamp DESC")
    fun getNotifications(): Flow<List<MifosNotification>>

    @Query("SELECT COUNT(*) FROM mifos_notifications WHERE read = 0")
    fun getUnreadNotificationsCount(): Flow<Int>

    @Query("DELETE FROM mifos_notifications WHERE timeStamp < :cutoffTime")
    suspend fun deleteOldNotifications(cutoffTime: Long)

    @Query("UPDATE mifos_notifications SET read = :isRead WHERE timeStamp = :timeStamp")
    suspend fun updateReadStatus(timeStamp: Long, isRead: Boolean)
}
