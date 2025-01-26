/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.database

import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.database.dao.MifosNotificationDao

expect abstract class AppDatabase {
    abstract val mifosNotificationDao: MifosNotificationDao
    abstract val chargeDao: ChargeDao
}

// fun getRoomDatabase(
//    appDatabase: AppDatabase,
// ): AppDatabase {
//    return appDatabase
// }
