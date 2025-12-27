/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual fun readResource(path: String): String {
    val name = path.substringBeforeLast(".")
    val ext = path.substringAfterLast(".")

    val resourcePath = NSBundle.mainBundle
        .pathForResource(name, ext)

    return NSString.stringWithContentsOfFile(
        resourcePath!!,
        encoding = NSUTF8StringEncoding,
        error = null,
    ) as String
}
