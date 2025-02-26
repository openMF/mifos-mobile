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

import java.awt.Desktop
import java.net.URI

actual fun callHelpline() {
    if (Desktop.isDesktopSupported()) {
        val uri = URI("tel:8000000000")
        try {
            Desktop.getDesktop().browse(uri)
        } catch (e: Exception) {
            println("Calling is not supported on Desktop.")
        }
    } else {
        println("Calling is not supported on this platform.")
    }
}
actual fun mailHelpline() {
    val uri = URI("mailto:support@example.com?subject=Help%20Request&body=Hello,%20I%20need%20assistance%20with...")
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
        Desktop.getDesktop().mail(uri)
    }
}
