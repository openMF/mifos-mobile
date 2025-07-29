/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.di

import org.koin.dsl.module
import org.mifos.mobile.core.ui.utils.DefaultResultNavigator
import org.mifos.mobile.core.ui.utils.ResultNavigator

val navigatorModule = module {
    single<ResultNavigator> { DefaultResultNavigator() }
}
