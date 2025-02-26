/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation

import kotlinx.browser.window

actual fun callHelpline() {
    window.alert("Calling is not supported on Web. Please contact support at 8000000000.")
}

actual fun mailHelpline() {
    val url = "mailto:support@example.com?subject=Help%20Request&body=Hello,%20I%20need%20assistance%20with..."
    window.open(url)
}
