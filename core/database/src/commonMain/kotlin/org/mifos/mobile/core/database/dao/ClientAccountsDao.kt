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

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.database.entity.ClientAccountsEntity

@Dao
interface ClientAccountsDao {

    @Query("SELECT * FROM client_accounts_summary WHERE clientId = :clientId")
    fun getByClientId(clientId: Long): Flow<ClientAccountsEntity?>

    @Insert(entity = ClientAccountsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ClientAccountsEntity)

    @Query("DELETE FROM client_accounts_summary WHERE clientId = :clientId")
    suspend fun deleteByClientId(clientId: Long)

    @Query("DELETE FROM client_accounts_summary")
    suspend fun deleteAll()
}
