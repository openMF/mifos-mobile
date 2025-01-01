/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.core.database.SelfServiceDatabase
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.database.dao.MifosNotificationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SelfServiceDatabase {
        return SelfServiceDatabase.getDatabase(context)
    }

    @Provides
    fun provideMifosNotificationDao(database: SelfServiceDatabase): MifosNotificationDao {
        return database.mifosNotificationDao()
    }

    @Provides
    fun provideChargeDao(database: SelfServiceDatabase): ChargeDao {
        return database.chargeDao()
    }
}
