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

import androidx.compose.ui.graphics.asSkiaBitmap
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.saveImageToGallery
import kotlinx.coroutines.DelicateCoroutinesApi
import java.awt.Desktop
import java.net.URI

actual object ShareUtils {
    @OptIn(DelicateCoroutinesApi::class)
    actual suspend fun shareText(text: String) {
        FileKit.saveImageToGallery(
            bytes = text.encodeToByteArray(),
            filename = "shared_text.txt",
        )
    }

    actual suspend fun shareImage(
        title: String,
        image: androidx.compose.ui.graphics.ImageBitmap,
    ) {
        image.asSkiaBitmap().readPixels()?.let {
            FileKit.saveImageToGallery(
                bytes = it,
                filename = "$title.png",
            )
        }
    }

    actual suspend fun shareImage(title: String, byte: ByteArray) {
        FileKit.saveImageToGallery(
            bytes = byte,
            filename = "$title.png",
        )
    }

    actual fun openUrl(url: String) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(url))
            }
        } catch (e: Exception) {
            println("Error opening URL: ${e.message}")
        }
    }

    actual fun openAppInfo() {
        // Not applicable on Desktop; no-op
    }

    actual fun callPhone(number: String) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI("tel:$number"))
            }
        } catch (e: Exception) {
            println("Error opening dialer: ${e.message}")
        }
    }

    actual fun sendEmail(to: String, subject: String?, body: String?) {
        val q = mutableListOf<String>()
        subject?.let { q.add("subject=" + java.net.URLEncoder.encode(it, Charsets.UTF_8)) }
        body?.let { q.add("body=" + java.net.URLEncoder.encode(it, Charsets.UTF_8)) }
        val query = if (q.isNotEmpty()) "?" + q.joinToString("&") else ""
        val mailto = "mailto:$to$query"
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(mailto))
            }
        } catch (e: Exception) {
            println("Error opening email client: ${e.message}")
        }
    }

    actual fun sendViaSMS(number: String, message: String) {
        val encodedMessage = java.net.URLEncoder.encode(message, Charsets.UTF_8)
        val smsUrl = if (number.isNotEmpty()) {
            "sms:$number?body=$encodedMessage"
        } else {
            "sms:?body=$encodedMessage"
        }
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(smsUrl))
            }
        } catch (e: Exception) {
            println("Error opening SMS: ${e.message}")
        }
    }

    actual fun copyText(text: String) {
        try {
            val clipboard = java.awt.Toolkit.getDefaultToolkit().systemClipboard
            val stringSelection = java.awt.datatransfer.StringSelection(text)
            clipboard.setContents(stringSelection, null)
        } catch (e: Exception) {
            println("Error copying to clipboard: ${e.message}")
        }
    }
}
