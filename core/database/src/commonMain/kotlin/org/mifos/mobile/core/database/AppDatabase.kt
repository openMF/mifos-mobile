/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.TypeConverters
import org.mifos.mobile.core.database.dao.BeneficiaryDao
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.database.dao.ClientAccountsDao
import org.mifos.mobile.core.database.dao.ClientDao
import org.mifos.mobile.core.database.dao.GuarantorDao
import org.mifos.mobile.core.database.dao.LoanAccountDao
import org.mifos.mobile.core.database.dao.LoanDetailsDao
import org.mifos.mobile.core.database.dao.MifosNotificationDao
import org.mifos.mobile.core.database.dao.SavingsAccountDao
import org.mifos.mobile.core.database.dao.SavingsDetailsDao
import org.mifos.mobile.core.database.dao.ShareAccountDao
import org.mifos.mobile.core.database.dao.TransactionDao
import org.mifos.mobile.core.database.entity.BeneficiaryEntity
import org.mifos.mobile.core.database.entity.ChargeEntity
import org.mifos.mobile.core.database.entity.ClientAccountsEntity
import org.mifos.mobile.core.database.entity.ClientEntity
import org.mifos.mobile.core.database.entity.GuarantorEntity
import org.mifos.mobile.core.database.entity.LoanAccountEntity
import org.mifos.mobile.core.database.entity.LoanDetailsEntity
import org.mifos.mobile.core.database.entity.MifosNotificationEntity
import org.mifos.mobile.core.database.entity.SavingsAccountEntity
import org.mifos.mobile.core.database.entity.SavingsDetailsEntity
import org.mifos.mobile.core.database.entity.ShareAccountEntity
import org.mifos.mobile.core.database.entity.TransactionEntity
import org.mifos.mobile.core.database.utils.ChargeTypeConverters

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

@Database(
    entities = [
        ChargeEntity::class,
        MifosNotificationEntity::class,
        ClientEntity::class,
        LoanAccountEntity::class,
        SavingsAccountEntity::class,
        ShareAccountEntity::class,
        BeneficiaryEntity::class,
        TransactionEntity::class,
        GuarantorEntity::class,
        ClientAccountsEntity::class,
        LoanDetailsEntity::class,
        SavingsDetailsEntity::class,
    ],
    version = AppDatabase.VERSION,
    exportSchema = true,
)
@TypeConverters(ChargeTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val mifosNotificationDao: MifosNotificationDao
    abstract val chargeDao: ChargeDao
    abstract val clientDao: ClientDao
    abstract val loanAccountDao: LoanAccountDao
    abstract val savingsAccountDao: SavingsAccountDao
    abstract val shareAccountDao: ShareAccountDao
    abstract val beneficiaryDao: BeneficiaryDao
    abstract val transactionDao: TransactionDao
    abstract val guarantorDao: GuarantorDao
    abstract val clientAccountsDao: ClientAccountsDao
    abstract val loanDetailsDao: LoanDetailsDao
    abstract val savingsDetailsDao: SavingsDetailsDao

    companion object {
        const val VERSION = 2
        const val DATABASE_NAME = "mifos_database.db"
    }
}
