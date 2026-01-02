/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.ui

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.download
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
actual object ShareUtils {
    actual suspend fun shareText(text: String) {
        FileKit.download(
            bytes = text.encodeToByteArray(),
            fileName = "shared_text.txt",
        )
    }

    actual suspend fun shareImage(
        title: String,
        image: ImageBitmap,
    ) {
        image.asSkiaBitmap().readPixels()?.let {
            FileKit.download(
                bytes = it,
                fileName = "$title.png",
            )
        }
    }

    actual suspend fun shareImage(title: String, byte: ByteArray) {
        FileKit.download(
            bytes = byte,
            fileName = "$title.png",
        )
    }

    actual fun openUrl(url: String) {
    }

    actual fun openAppInfo() {
    }

    actual fun callPhone(number: String) {
    }

    actual fun sendEmail(to: String, subject: String?, body: String?) {
    }

    actual fun sendViaSMS(number: String, message: String) {
    }

    actual fun copyText(text: String) {
    }
}
