/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import androidx.compose.ui.graphics.ImageBitmap
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual object ShareUtils {
    actual fun shareText(text: String) {
    }

    actual suspend fun shareImage(title: String, image: ImageBitmap) {
    }

    actual suspend fun shareImage(title: String, byte: ByteArray) {
    }

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

    actual fun openAppInfo() {
    }

    actual fun shareApp() {
    }

    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    actual fun ossLicensesMenuActivity() {
    }
}
