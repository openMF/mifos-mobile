/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package cmp.android.app

import android.content.res.Configuration
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.util.Consumer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.mifos.core.model.DarkThemeConfig

@ColorInt
private val SCRIM_COLOR: Int = Color.TRANSPARENT

/**
 * Helper method to handle edge-to-edge logic for dark mode.
 *
 * This logic is from the Now-In-Android app found
 * [here](https://github.com/android/nowinandroid/blob/689ef92e41427ab70f82e2c9fe59755441deae92/app/src/main/kotlin/com/google/samples/apps/nowinandroid/MainActivity.kt#L94).
 */
@Suppress("MaxLineLength")
fun ComponentActivity.setupEdgeToEdge(
    appThemeFlow: Flow<DarkThemeConfig>,
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            combine(
                isSystemInDarkModeFlow(),
                appThemeFlow,
            ) { isSystemDarkMode, appTheme ->
                AppCompatDelegate.setDefaultNightMode(appTheme.osValue)
                appTheme.isDarkMode(isSystemDarkMode = isSystemDarkMode)
            }
                .distinctUntilChanged()
                .collect { isDarkMode ->
                    // This handles all the settings to go edge-to-edge. We are using a transparent
                    // scrim for system bars and switching between "light" and "dark" based on the
                    // system and internal app theme settings.
                    val style = SystemBarStyle.auto(
                        darkScrim = SCRIM_COLOR,
                        lightScrim = SCRIM_COLOR,
                        // Disabling Dark Mode for this app
                        detectDarkMode = { false },
                    )
                    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                }
        }
    }
}

/**
 * Adds a configuration change listener to retrieve whether system is in
 * dark theme or not. This will emit current status immediately and then
 * will emit changes as needed.
 */
private fun ComponentActivity.isSystemInDarkModeFlow(): Flow<Boolean> =
    callbackFlow {
        channel.trySend(element = resources.configuration.isSystemInDarkMode)
        val listener = Consumer<Configuration> {
            channel.trySend(element = it.isSystemInDarkMode)
        }
        addOnConfigurationChangedListener(listener = listener)
        awaitClose { removeOnConfigurationChangedListener(listener = listener) }
    }
        .distinctUntilChanged()
        .conflate()
