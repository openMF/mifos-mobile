/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import cmp.shared.SharedApp

/**
 * Main activity class.
 * This class is used to set the content view of the activity.
 *
 * @constructor Create empty Main activity
 * @see ComponentActivity
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling [setContentView(int)] to inflate the activity's UI,
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        /**
         * Set the content view of the activity.
         * @see setContent
         */
        setContent {
            SharedApp()
        }
    }
}
