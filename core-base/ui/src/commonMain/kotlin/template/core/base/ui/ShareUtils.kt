/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.ui

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Platform-specific utility for sharing content with other applications.
 * This expect class requires platform-specific implementations.
 */
expect object ShareUtils {

    /**
     * Shares text content with other applications.
     *
     * @param text The text content to be shared
     */
    suspend fun shareText(text: String)

    /**
     * Shares an image with other applications.
     *
     * @param title The title to use when sharing the image
     * @param image The ImageBitmap to be shared
     */
    suspend fun shareImage(title: String, image: ImageBitmap)

    /**
     * Shares an image with other applications using raw byte data.
     *
     * @param title The title to use when sharing the image
     * @param byte The raw image data as ByteArray
     */
    suspend fun shareImage(title: String, byte: ByteArray)

    /**
     * Opens the specified URL in the device's default web browser.
     *
     * @param url The URL to open.
     */
    fun openUrl(url: String)

    /**
     * Opens the application info screen in the device settings.
     *
     * Typically used to allow users to manage app permissions,
     * storage, or other app-specific settings.
     */
    fun openAppInfo()

    /**
     * Initiates a phone call using the platform dialer UI.
     *
     * @param number The phone number to dial, digits only or including country code.
     */
    fun callPhone(number: String)

    /**
     * Opens the platform email composer with the given parameters.
     *
     * @param to Recipient email address
     * @param subject Optional subject line
     * @param body Optional email body
     */
    fun sendEmail(to: String, subject: String? = null, body: String? = null)

    /**
     * Opens the platform SMS composer with the given parameters.
     *
     * @param number Recipient phone number (can be empty to let user choose)
     * @param message The SMS message body
     */
    fun sendViaSMS(number: String, message: String)

    /**
     * Copies the given text to the system clipboard.
     *
     * @param text The text to copy to clipboard
     */
    fun copyText(text: String)
}
