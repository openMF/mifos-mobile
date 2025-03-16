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
import androidx.compose.ui.graphics.asSkiaBitmap
import io.github.vinceglb.filekit.core.FileKit

actual object ShareUtils {
    actual fun shareText(text: String) {
    }

    actual suspend fun shareImage(title: String, image: ImageBitmap) {
        FileKit.saveFile(
            bytes = image.asSkiaBitmap().readPixels(),
            baseName = "MifosQrCode",
            extension = "png",
        )
    }

    actual suspend fun shareImage(title: String, byte: ByteArray) {
        FileKit.saveFile(
            bytes = byte,
            baseName = "MifosQrCode",
            extension = "png",
        )
    }
}
