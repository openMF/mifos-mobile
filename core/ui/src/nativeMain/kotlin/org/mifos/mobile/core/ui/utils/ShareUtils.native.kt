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
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.dialogs.shareFile
import io.github.vinceglb.filekit.write
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIViewController

/**
 * Actual implementation of [ShareUtils] for iOS platform.
 *
 * Provides functionality to share text and files using iOS native `UIActivityViewController`.
 */
actual object ShareUtils {
    /**
     * Shares plain text using the iOS share sheet (`UIActivityViewController`).
     *
     * @param text The text content to be shared.
     */
    actual suspend fun shareText(text: String) {
        val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
        val activityViewController = UIActivityViewController(listOf(text), null)
        currentViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }

    /**
     * Shares a file (image or other binary) using the iOS share sheet.
     *
     * If the file is an image, it will be compressed before sharing.
     *
     * @param file The file metadata and byte content to share.
     */
    actual suspend fun shareFile(file: ShareFileModel) {
        try {
            val compressedBytes = if (file.mime == MimeType.IMAGE) {
                compressImage(file.bytes)
            } else {
                file.bytes
            }

            val fileToShare = saveFile(data = compressedBytes, fileName = file.fileName)
            FileKit.shareFile(fileToShare)
        } catch (e: Exception) {
            println("Failed to share file: ${e.message}")
        }
    }

    /**
     * Saves a byte array as a file inside the iOS app's cache directory.
     *
     * Converts the resulting file path to a properly scoped [NSURL],
     * which is necessary for iOS to allow sharing via `UIActivityViewController`.
     *
     * @param data The file content to write.
     * @param fileName The name of the file to create.
     * @return A [PlatformFile] backed by a scoped `NSURL`, ready for sharing.
     */
    private suspend fun saveFile(data: ByteArray, fileName: String): PlatformFile {
        val tempFile = PlatformFile(FileKit.cacheDir, fileName)
        tempFile.write(data)

        /**
         * iOS requires file URLs used in `UIActivityViewController` to be created
         * with `NSURL.fileURLWithPath(...)` to ensure they have proper sandbox access.
         *
         * If the file is created from a raw path string, the system may reject it
         * with a sandbox extension error (e.g., "Cannot issue sandbox extension for URL").
         *
         * Wrapping the path in `NSURL` ensures the file is treated as a valid
         * security-scoped resource.
         */
        val nsUrl = NSURL.fileURLWithPath(tempFile.absolutePath())
        return PlatformFile(nsUrl)
    }

    /**
     * Compresses an image file using [FileKit] logic.
     *
     * @param imageBytes The original image byte array.
     * @return A compressed image as a byte array.
     */
    private suspend fun compressImage(imageBytes: ByteArray): ByteArray {
        return FileKit.compressImage(
            bytes = imageBytes,
            // Compression quality (0–100)
            quality = 100,
            // Max width in pixels
            maxWidth = 1024,
            // Max height in pixels
            maxHeight = 1024,
            // Image format (e.g., PNG or JPEG)
            imageFormat = ImageFormat.PNG,
        )
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
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    actual fun shareApp() {
        // TODO Replace with app store link
        val appStoreUrl = "https://apps.apple.com/app/idXXXXXXXX"
        val activityVC = UIActivityViewController(activityItems = listOf(appStoreUrl), applicationActivities = null)
        present(activityVC)
    }

    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    actual fun ossLicensesMenuActivity() {
    }

    private fun present(controller: UIViewController) {
        val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootController?.presentViewController(controller, animated = true, completion = null)
    }
}
