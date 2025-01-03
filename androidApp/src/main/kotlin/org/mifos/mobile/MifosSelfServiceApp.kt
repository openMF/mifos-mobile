/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.feature.settings.applySavedTheme

@HiltAndroidApp
class MifosSelfServiceApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        PreferencesHelper(this).applySavedTheme()
    }
}
