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

import org.koin.core.module.Module
import org.koin.dsl.module
import org.mifos.mobile.core.database.AppDatabase

val DatabaseModule = module {
    includes(platformModule)
    single { get<AppDatabase>().chargeDao }
    single { get<AppDatabase>().mifosNotificationDao }
    single { get<AppDatabase>().clientDao }
    single { get<AppDatabase>().loanAccountDao }
    single { get<AppDatabase>().savingsAccountDao }
    single { get<AppDatabase>().shareAccountDao }
    single { get<AppDatabase>().beneficiaryDao }
    single { get<AppDatabase>().transactionDao }
    single { get<AppDatabase>().guarantorDao }
    single { get<AppDatabase>().clientAccountsDao }
    single { get<AppDatabase>().loanDetailsDao }
    single { get<AppDatabase>().savingsDetailsDao }
}

expect val platformModule: Module
