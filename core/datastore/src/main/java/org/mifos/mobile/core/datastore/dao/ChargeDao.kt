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
import org.mifos.mobile.core.datastore.entity.Charge

@Dao
interface ChargeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharge(charge: List<Charge>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncCharges(charges: List<Charge>)

    @Query("SELECT * FROM charges")
    fun clientLocalCharges(): Flow<List<Charge>>
}
