/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    error = RedErrorDark,
    background = BackgroundLight,
    onSurface = Black2,
    onSecondary = Color.Gray,
    outlineVariant = Color.Gray,
    surfaceTint = LightSurfaceTint,
)

private val DarkThemeColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.White,
    secondary = Black1,
    error = RedErrorDark,
    background = BackgroundDark,
    surface = BackgroundDark,
    onSurface = Color.White,
    onSecondary = Color.White,
    outlineVariant = Color.White,
    surfaceTint = DarkSurfaceTint,
)

@Composable
fun MifosMobileTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = when {
        useDarkTheme -> DarkThemeColors
        else -> LightThemeColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}
