/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.shared

import androidx.compose.ui.window.ComposeUIViewController
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin
import platform.Foundation.NSUserDefaults

fun viewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    SharedApp(
        handleThemeMode = {},
        handleAppLocale = { languageTag ->
            applyIosLocale(
                languageTag = languageTag,
            )
        },
        onSplashScreenRemoved = {},
    )
}
private fun applyIosLocale(
    languageTag: String?,
) {
    if (!languageTag.isNullOrBlank()) {
        NSUserDefaults.standardUserDefaults.setObject(
            listOf(languageTag),
            forKey = "AppleLanguages",
        )
    } else {
        NSUserDefaults.standardUserDefaults.removeObjectForKey("AppleLanguages")
    }
}
