/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.di

import com.russhwolf.settings.Settings
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.mifos.library.passcode.data.PasscodeManager
import org.mifos.library.passcode.data.PasscodePreferencesDataSource
import org.mifos.mobile.core.common.MifosDispatchers

val PasscodePreferenceModule = module {
    factory<Settings> { Settings() }
    factory { PasscodePreferencesDataSource(get(), get(named(MifosDispatchers.IO.name))) }
    factory { PasscodeManager(get(), get(named(MifosDispatchers.Unconfined.name))) }
}
