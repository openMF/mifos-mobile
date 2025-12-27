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

actual fun readResource(path: String): String {
    return requireNotNull(
        object {}.javaClass.classLoader
            ?.getResourceAsStream(path),
    ) {
        "Test resource not found: $path"
    }.bufferedReader().use { it.readText() }
}
