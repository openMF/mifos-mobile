/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package cmp.android.app

import org.mifos.core.model.DarkThemeConfig

fun DarkThemeConfig.isDarkMode(
    isSystemDarkMode: Boolean,
): Boolean =
    when (this) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkMode
        DarkThemeConfig.DARK -> true
        DarkThemeConfig.LIGHT -> false
    }
