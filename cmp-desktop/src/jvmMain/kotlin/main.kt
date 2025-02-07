/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-wallet/blob/master/LICENSE.md
 */

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin

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

        // Creates a window with a specified title and close request handler.
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "DesktopApp",
        ) {
            // Sets the content of the window.
            SharedApp()
        }
    }
}
