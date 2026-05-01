/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.mifos.mobile.core.common.MifosDispatchers
import org.mifos.mobile.core.database.AppDatabase
import org.mifos.mobile.core.database.utils.ChargeTypeConverters
import template.core.base.database.AppDatabaseFactory
import template.core.base.security.FieldEncryptor

actual val platformModule: Module = module {
    single {
        ChargeTypeConverters.install(get<FieldEncryptor>())
        AppDatabaseFactory(androidApplication())
            .createDatabase(AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigrationOnDowngrade(false)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(get(named(MifosDispatchers.IO.name)))
            .build()
    }
}
