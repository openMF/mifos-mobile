/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.intent

import androidx.compose.ui.graphics.ImageBitmap
import template.core.base.platform.model.MimeType

/**
 * Manages platform-specific intent operations and content sharing functionality.
 * This interface abstracts platform differences for various system interactions
 * such as launching activities, sharing content, and handling system intents.
 */
interface IntentManager {
    /**
     * Starts a platform-specific activity with the provided intent.
     *
     * @param intent The platform-specific intent object to be processed
     */
    fun startActivity(intent: Any)

    /**
     * Opens the specified URI in an appropriate application.
     * Typically used for opening websites, deep links, or specific application URIs.
     *
     * @param uri The URI string to be opened
     */
    fun launchUri(uri: String)

    /**
     * Shares text content with other applications via the platform's share mechanism.
     *
     * @param text The text content to be shared
     */
    fun shareText(text: String)

    /**
     * Shares a file with other applications.
     *
     * @param fileUri The URI string pointing to the file to be shared
     * @param mimeType The MIME type of the file to help receiving applications handle it properly
     */
    fun shareFile(fileUri: String, mimeType: MimeType)

    /**
     * Shares a file with other applications, including additional text content.
     *
     * @param fileUri The URI string pointing to the file to be shared
     * @param mimeType The MIME type of the file to help receiving applications handle it properly
     * @param extraText Additional text to include with the shared file
     */
    fun shareFile(fileUri: String, mimeType: MimeType, extraText: String)

    /**
     * Shares an image with other applications.
     *
     * @param title The title to use when sharing the image
     * @param image The ImageBitmap to be shared
     */
    suspend fun shareImage(title: String, image: ImageBitmap)

    /**
     * Creates a platform-specific intent for document creation.
     *
     * @param fileName The suggested name for the document to be created
     * @return A platform-specific intent object for document creation
     */
    fun createDocumentIntent(fileName: String): Any

    /**
     * Opens the application details settings screen for the current application.
     * Typically used to direct users to app permissions, notifications, or other system settings.
     */
    fun startApplicationDetailsSettingsActivity()
}
