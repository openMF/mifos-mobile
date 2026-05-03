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
import io.github.vinceglb.filekit.saveImageToGallery
import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Suppress("CAST_NEVER_SUCCEEDS")
actual object ShareUtils {

    private fun String.urlEncode(): String {
        val nsString = this as NSString
        return nsString.stringByAddingPercentEncodingWithAllowedCharacters(
            NSCharacterSet.URLQueryAllowedCharacterSet,
        ) ?: this
    }

    actual suspend fun shareText(text: String) {
        val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
        val activityViewController = UIActivityViewController(listOf(text), null)
        currentViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }

    actual suspend fun shareImage(title: String, image: ImageBitmap) {
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
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(
                nsUrl,
                options = emptyMap<Any?, Any>(),
                completionHandler = null,
            )
        }
    }

    actual fun openAppInfo() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    actual fun callPhone(number: String) {
        val url = NSURL.URLWithString("tel:$number")
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    actual fun sendEmail(to: String, subject: String?, body: String?) {
        val encodedSubject = subject?.urlEncode() ?: ""
        val encodedBody = body?.urlEncode() ?: ""
        val query = buildList {
            if (encodedSubject.isNotEmpty()) add("subject=$encodedSubject")
            if (encodedBody.isNotEmpty()) add("body=$encodedBody")
        }.joinToString("&")
        val mailto = if (query.isNotEmpty()) "mailto:$to?$query" else "mailto:$to"
        val url = NSURL.URLWithString(mailto)
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    actual fun sendViaSMS(number: String, message: String) {
        val encodedMessage = message.urlEncode()
        val smsUrl = if (number.isNotEmpty()) {
            "sms:$number&body=$encodedMessage"
        } else {
            "sms:&body=$encodedMessage"
        }
        val url = NSURL.URLWithString(smsUrl)
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    actual fun copyText(text: String) {
        platform.UIKit.UIPasteboard.generalPasteboard.string = text
    }

    actual suspend fun shareApp(storeLink: String, message: String) {
        val shareContent = if (message.isNotEmpty()) {
            "$message\n$storeLink"
        } else {
            storeLink
        }
        val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
        val activityViewController = UIActivityViewController(listOf(shareContent), null)
        currentViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }
}
