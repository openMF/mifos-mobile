/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-wallet/blob/master/LICENSE.md
 */

import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin
import java.util.Locale

/**
 * Main function.
 * This function is used to start the desktop application.
 * It performs the following tasks:
 * 1. Initializes Koin for dependency injection.
 * 2. Creates a window state to manage the window's state.
 * 3. Creates a window with a specified title and close request handler.
 * 4. Calls `SharedApp()` to render the root composable of the application.
 *
 * @see application
 * @see rememberWindowState
 * @see Window
 * @see SharedApp
 */
fun main() {
    application {
        // Initializes the Koin dependency injection framework.
        initKoin()

        // Creates a window state to manage the window's state.
        val windowState = rememberWindowState()

        // State to trigger recomposition when locale changes
        var localeVersion by remember { mutableStateOf(0) }

        // Creates a window with a specified title and close request handler.
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "DesktopApp",
        ) {
            // Use key() to force complete recomposition when locale changes
            key(localeVersion) {
                // Sets the content of the window.
                SharedApp(
                    updateScreenCapture = {},
                    handleRecreate = {
                        // Increment version to trigger recomposition
                        localeVersion++
                    },
                    handleThemeMode = {},
                    handleAppLocale = { languageTag ->
                        if (languageTag != null) {
                            // Parse language tag and set as default locale
                            val locale = when {
                                languageTag.contains("-") -> {
                                    val parts = languageTag.split("-")
                                    Locale(parts[0], parts[1])
                                }
                                else -> Locale(languageTag)
                            }
                            Locale.setDefault(locale)
                        } else {
                            // System Default: reset to system locale
                            val systemLocale = Locale.getDefault(Locale.Category.DISPLAY)
                            Locale.setDefault(systemLocale)
                        }
                        // Trigger recomposition with new locale
                        localeVersion++
                    },
                    onSplashScreenRemoved = {}
                )
            }
        }
    }
}
