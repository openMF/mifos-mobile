/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.dao

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.database.Dao
import org.mifos.mobile.core.database.Insert
import org.mifos.mobile.core.database.OnConflictStrategy
import org.mifos.mobile.core.database.Query
import org.mifos.mobile.core.database.entity.MifosNotificationEntity

@Dao
interface MifosNotificationDao {

    @Query("SELECT * FROM mifos_notification ORDER BY timeStamp DESC")
    fun getNotifications(): Flow<List<MifosNotificationEntity>>

    @Query("SELECT COUNT(*) FROM mifos_notification WHERE read = 0")
    fun getUnreadNotificationsCount(): Flow<Int>

    @Insert(entity = MifosNotificationEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNotification(notification: MifosNotificationEntity)

    @Query("DELETE FROM mifos_notification WHERE timeStamp < :cutoffTime")
    suspend fun deleteOldNotifications(cutoffTime: Long)

    @Query("UPDATE mifos_notification SET read = :isRead WHERE timeStamp = :timeStamp")
    suspend fun updateReadStatus(timeStamp: Long, isRead: Boolean)
}
