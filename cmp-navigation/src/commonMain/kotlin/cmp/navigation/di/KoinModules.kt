/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.di

import org.koin.dsl.module
import org.mifos.mobile.core.common.di.DispatchersModule
import org.mifos.mobile.core.data.di.RepositoryModule
import org.mifos.mobile.core.datastore.di.PreferencesModule
import org.mifos.mobile.core.network.di.NetworkModule

object KoinModules {
    private val commonModules = module {
        includes(DispatchersModule)
    }
    private val dataModules = module {
        includes(RepositoryModule)
    }
    private val coreDataStoreModules = module {
        includes(PreferencesModule)
    }
    private val networkModules = module {
        includes(NetworkModule)
    }
//    private val sharedModule = module {
//        viewModelOf(::MifosMobileViewModel)
//    }
//    private val featureModules = module {
//        includes(
//            AuthModule,
//        )
//    }
//    private val LibraryModule = module {
//        includes(PasscodeModule)
//    }

    val allModules = listOf(
        commonModules,
        dataModules,
        coreDataStoreModules,
        networkModules,
//        featureModules,
//        sharedModule,
    )
}
