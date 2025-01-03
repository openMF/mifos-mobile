/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.core.database.SelfServiceDatabase
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.database.dao.MifosNotificationDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun provideMifosNotificationDao(database: SelfServiceDatabase): MifosNotificationDao {
        return database.mifosNotificationDao()
    }

    @Provides
    fun provideChargeDao(database: SelfServiceDatabase): ChargeDao {
        return database.chargeDao()
    }
}
