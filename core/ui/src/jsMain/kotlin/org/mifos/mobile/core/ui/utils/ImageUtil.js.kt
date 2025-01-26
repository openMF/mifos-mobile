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

// TODO: Not Implemented
actual object ImageUtil {
    actual val DEFAULT_MAX_WIDTH: Float = 816f
    actual val DEFAULT_MAX_HEIGHT: Float = 612f

    actual fun compressImage(
        decodedBytes: ByteArray,
        maxWidth: Float,
        maxHeight: Float,
    ): ByteArray {
        return decodedBytes
    }
}
