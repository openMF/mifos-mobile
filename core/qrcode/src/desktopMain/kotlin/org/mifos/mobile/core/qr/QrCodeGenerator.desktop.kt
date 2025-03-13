/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.qr

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Generate a QR Code and return platform-specific representation.
 * @param str Data to be stored in QR Code.
 * @return Platform-specific QR Code representation.
 */
actual fun generateQrCode(str: String): ImageBitmap? {
    TODO("Not yet implemented")
}

actual fun decodeQrCode(bitmap: ImageBitmap): String? {
    TODO("Not yet implemented")
}
