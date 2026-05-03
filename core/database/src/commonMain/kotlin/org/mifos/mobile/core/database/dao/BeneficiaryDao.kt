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
import org.mifos.mobile.core.database.entity.BeneficiaryEntity

@Dao
interface BeneficiaryDao {

    @Query("SELECT * FROM beneficiaries")
    fun getAllBeneficiaries(): Flow<List<BeneficiaryEntity>>

    @Insert(entity = BeneficiaryEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(beneficiaries: List<BeneficiaryEntity>)

    @Query("DELETE FROM beneficiaries")
    suspend fun deleteAll()
}
