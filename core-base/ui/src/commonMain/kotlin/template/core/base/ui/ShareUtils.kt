/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
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
}
