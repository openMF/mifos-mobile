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

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.download
import kotlinx.browser.window

/**
 * Provides utility functions for sharing content on JS and WASM platforms.
 *
 * This implementation uses [FileKit.download] to trigger file downloads
 * in web environments (WASM), as native share dialogs are not supported
 * on these platforms.
 */
actual object ShareUtils {
    /**
     * Shares plain text content by triggering a file download.
     *
     * The text is saved to a file named `text.txt` and offered to the user
     * as a downloadable file in the browser.
     *
     * @param text The plain text content to be shared.
     */
    actual suspend fun shareText(text: String) {
        FileKit.download(bytes = text.encodeToByteArray(), fileName = "text.txt")
    }

    /**
     * Shares a file by triggering a download of the file's byte content.
     *
     * This method creates a download link in the browser for the given
     * [ShareFileModel.bytes], using the provided [ShareFileModel.fileName]
     * as the download file name.
     *
     * @param file The [ShareFileModel] containing file name and content to be downloaded.
     */
    actual suspend fun shareFile(file: ShareFileModel) {
        FileKit.download(bytes = file.bytes, fileName = file.fileName)
    }
    actual fun callHelpline() {
        window.alert("Calling is not supported on Web. Please contact support at 8000000000.")
    }

    actual fun mailHelpline() {
        val url = "mailto:support@example.com?subject=Help%20Request&body=Hello,%20I%20need%20assistance%20with..."
        window.open(url)
    }

    actual fun openAppInfo() {
    }

    actual fun shareApp() {
    }

    actual fun openUrl(url: String) {
        window.open(url)
    }

    actual fun ossLicensesMenuActivity() {
    }
}
