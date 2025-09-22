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

/**
 * Platform-specific utilities for sharing content such as text and files.
 *
 * This expect declaration should be implemented for each platform (e.g., Android, iOS) to handle
 * the specifics of sharing functionality.
 */
expect object ShareUtils {

    /**
     * Shares plain text content using the platform's native sharing mechanism.
     *
     * @param text The text content to be shared.
     */
    suspend fun shareText(text: String)

    /**
     * Shares a file using the platform's native sharing mechanism.
     *
     * This is a suspend function, allowing for asynchronous operations such as file preparation
     * or permission handling if needed.
     *
     * @param file A [ShareFileModel] containing the file's metadata and content.
     */
    suspend fun shareFile(file: ShareFileModel)

    fun openAppInfo()

    fun shareApp()

    fun callHelpline()

    fun mailHelpline()

    fun openUrl(url: String)

    fun ossLicensesMenuActivity()
}

/**
 * Represents supported MIME types for file sharing.
 */
enum class MimeType {
    PDF,
    TEXT,
    IMAGE,
}

/**
 * Model representing a file to be shared.
 *
 * @property mime The MIME type of the file. Defaults to [MimeType.PDF].
 * @property fileName The name of the file, including its extension.
 * @property bytes The binary content of the file.
 */
data class ShareFileModel(
    val mime: MimeType = MimeType.PDF,
    val fileName: String,
    val bytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ShareFileModel

        if (mime != other.mime) return false
        if (fileName != other.fileName) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mime.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
