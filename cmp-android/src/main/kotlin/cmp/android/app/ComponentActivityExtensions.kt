/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.android.app

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cmp.android.app.util.isSystemInDarkModeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.mifos.mobile.core.model.MifosThemeConfig

@ColorInt
private val SCRIM_COLOR: Int = Color.Transparent.toArgb()

/**
 * Helper method to handle edge-to-edge logic for dark mode.
 *
 * This logic is from the Now-In-Android app found
 * [here](https://github.com/android/nowinandroid/blob/689ef92e41427ab70f82e2c9fe59755441deae92/app/src/main/kotlin/com/google/samples/apps/nowinandroid/MainActivity.kt#L94).
 */
@Suppress("MaxLineLength")
fun ComponentActivity.setupEdgeToEdge(
    appThemeFlow: Flow<MifosThemeConfig>,
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            combine(
                isSystemInDarkModeFlow(),
                appThemeFlow,
            ) { isSystemDarkMode, appTheme ->

                val currentNightMode = AppCompatDelegate.getDefaultNightMode()
                if (currentNightMode != appTheme.osValue) {
                    AppCompatDelegate.setDefaultNightMode(appTheme.osValue)
                }

                when (appTheme.osValue) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> isSystemDarkMode
                    AppCompatDelegate.MODE_NIGHT_YES -> true
                    AppCompatDelegate.MODE_NIGHT_NO -> false
                    else -> isSystemDarkMode
                }
            }
                .distinctUntilChanged()
                .collect { isDarkMode ->
                    // This handles all the settings to go edge-to-edge. We are using a transparent
                    // scrim for system bars and switching between "light" and "dark" based on the
                    // system and internal app theme settings.
                    val style = SystemBarStyle.auto(
                        darkScrim = SCRIM_COLOR,
                        lightScrim = SCRIM_COLOR,
                        detectDarkMode = { isDarkMode },
                    )
                    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                }
        }
    }
}
