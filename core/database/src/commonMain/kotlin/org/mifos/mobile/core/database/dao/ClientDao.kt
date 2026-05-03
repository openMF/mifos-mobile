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
import org.mifos.mobile.core.database.entity.ClientEntity

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients WHERE clientId = :clientId")
    fun getClient(clientId: Long): Flow<ClientEntity?>

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Insert(entity = ClientEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clients: List<ClientEntity>)

    @Insert(entity = ClientEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: ClientEntity)

    @Query("DELETE FROM clients")
    suspend fun deleteAll()
}
