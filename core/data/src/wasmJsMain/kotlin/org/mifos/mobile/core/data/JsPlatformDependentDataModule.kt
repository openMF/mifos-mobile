/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifospay.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mifos.mobile.core.data.di.PlatformDependentDataModule
import org.mifos.mobile.core.data.util.NetworkMonitor

class JsPlatformDependentDataModule : PlatformDependentDataModule {
    override val networkMonitor: NetworkMonitor by lazy {
        object : NetworkMonitor {
            override val isOnline: Flow<Boolean> = flowOf(true)
        }
    }
}
