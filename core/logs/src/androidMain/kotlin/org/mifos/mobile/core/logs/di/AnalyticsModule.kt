/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.logs.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.dsl.module
import org.mifos.mobile.core.logs.AnalyticsHelper

val AnalyticsModule = module {

    single {
        FirebaseAnalytics.getInstance(get<Context>())
    }
    single<AnalyticsHelper> {
        FirebaseAnalyticsHelper(firebaseAnalytics = get())
    }
}
