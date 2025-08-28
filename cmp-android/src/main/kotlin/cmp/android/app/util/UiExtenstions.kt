/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.android.app.util

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.core.util.Consumer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Convenience wrapper for dark mode checking
 */
val Configuration.isSystemInDarkTheme
    get() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Adds a configuration change listener to retrieve whether system is in
 * dark theme or not. This will emit current status immediately and then
 * will emit changes as needed.
 */
fun ComponentActivity.isSystemInDarkModeFlow(): Flow<Boolean> =
    callbackFlow {
        channel.trySend(element = resources.configuration.isSystemInDarkTheme)
        val listener = Consumer<Configuration> {
            channel.trySend(element = it.isSystemInDarkTheme)
        }
        addOnConfigurationChangedListener(listener = listener)
        awaitClose { removeOnConfigurationChangedListener(listener = listener) }
    }
        .distinctUntilChanged()
        .conflate()
