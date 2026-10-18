/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.dao

import androidx.room.Transaction
import org.mifos.mobile.core.database.entity.PendingPocketDelinkEntity
import org.mifos.mobile.core.database.entity.PocketAccountEntity
import template.core.base.database.Dao
import template.core.base.database.Insert
import template.core.base.database.OnConflictStrategy
import template.core.base.database.Query

@Dao
interface PocketAccountDao {
    @Query("SELECT * FROM pockets")
    suspend fun getAllPocketAccounts(): List<PocketAccountEntity>

    @Insert(entity = PocketAccountEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkPocketAccounts(pockets: List<PocketAccountEntity>)

    @Query("DELETE FROM pockets WHERE id IN (:pocketAccountMappingIds)")
    suspend fun delinkPocketAccounts(pocketAccountMappingIds: List<Long>)

    @Query("DELETE FROM pockets")
    suspend fun deleteAll()

    @Query("SELECT pocketAccountMappingId FROM pending_pocket_delinks")
    suspend fun getPendingDelinkIds(): List<Long>

    @Insert(entity = PendingPocketDelinkEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingDelinks(pendingDelinks: List<PendingPocketDelinkEntity>)

    @Query("DELETE FROM pending_pocket_delinks WHERE pocketAccountMappingId IN (:pocketAccountMappingIds)")
    suspend fun deletePendingDelinks(pocketAccountMappingIds: List<Long>)

    @Transaction
    suspend fun delinkPocketAccountsAndTrackPending(
        pocketAccountMappingIds: List<Long>,
        pendingDelinks: List<PendingPocketDelinkEntity>,
    ) {
        if (pendingDelinks.isNotEmpty()) {
            insertPendingDelinks(pendingDelinks)
        }
        delinkPocketAccounts(pocketAccountMappingIds)
    }

    @Transaction
    suspend fun replaceAllPocketAccounts(pockets: List<PocketAccountEntity>) {
        deleteAll()
        linkPocketAccounts(pockets)
    }
}
