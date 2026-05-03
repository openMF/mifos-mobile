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
import org.mifos.mobile.core.database.entity.LoanDetailsEntity

@Dao
interface LoanDetailsDao {

    @Query("SELECT * FROM loan_details WHERE loanId = :loanId")
    fun getLoanDetails(loanId: Long): Flow<LoanDetailsEntity?>

    @Insert(entity = LoanDetailsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LoanDetailsEntity)

    @Query("DELETE FROM loan_details WHERE loanId = :loanId")
    suspend fun delete(loanId: Long)

    @Query("DELETE FROM loan_details")
    suspend fun deleteAll()
}
