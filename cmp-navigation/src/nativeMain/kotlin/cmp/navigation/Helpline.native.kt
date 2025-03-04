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
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun callHelpline() {
    val url = NSURL.URLWithString("tel://8000000000")
    if (url?.let { UIApplication.sharedApplication.canOpenURL(it) } == true) {
        UIApplication.sharedApplication.openURL(url)
    }
}

actual fun mailHelpline() {
    val url = "mailto:support@example.com?subject=Help%20Request&body=Hello,%20I%20need%20assistance%20with..."
    val mailUrl = NSURL.URLWithString(url)

    if (mailUrl?.let { UIApplication.sharedApplication.canOpenURL(it) } == true) {
        UIApplication.sharedApplication.openURL(mailUrl)
    }
}
