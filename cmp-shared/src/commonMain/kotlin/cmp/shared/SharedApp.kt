/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cmp.navigation.ComposeApp
import coil3.compose.LocalPlatformContext
import template.core.base.platform.LocalManagerProvider
import template.core.base.platform.context.LocalContext
import template.core.base.ui.LocalImageLoaderProvider
import template.core.base.ui.getDefaultImageLoader

@Composable
fun SharedApp(
    handleThemeMode: (osValue: Int) -> Unit,
    handleAppLocale: (locale: String?) -> Unit,
    modifier: Modifier = Modifier,
    onSplashScreenRemoved: () -> Unit,
) {
    LocalManagerProvider(LocalContext.current) {
        LocalImageLoaderProvider(getDefaultImageLoader(LocalPlatformContext.current)) {
            ComposeApp(
                handleThemeMode = handleThemeMode,
                handleAppLocale = handleAppLocale,
                onSplashScreenRemoved = onSplashScreenRemoved,
                modifier = modifier,
            )
        }
    }
}
