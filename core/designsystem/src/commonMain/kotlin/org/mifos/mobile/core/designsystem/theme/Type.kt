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

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import mifos_mobile.core.designsystem.generated.resources.Res
import mifos_mobile.core.designsystem.generated.resources.poppins_black
import mifos_mobile.core.designsystem.generated.resources.poppins_bold
import mifos_mobile.core.designsystem.generated.resources.poppins_extra_bold
import mifos_mobile.core.designsystem.generated.resources.poppins_extra_light
import mifos_mobile.core.designsystem.generated.resources.poppins_light
import mifos_mobile.core.designsystem.generated.resources.poppins_medium
import mifos_mobile.core.designsystem.generated.resources.poppins_regular
import mifos_mobile.core.designsystem.generated.resources.poppins_semi_bold
import mifos_mobile.core.designsystem.generated.resources.poppins_thin
import org.jetbrains.compose.resources.Font

@Composable
private fun appFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.poppins_black, FontWeight.Black),
        Font(Res.font.poppins_bold, FontWeight.Bold),
        Font(Res.font.poppins_semi_bold, FontWeight.SemiBold),
        Font(Res.font.poppins_medium, FontWeight.Medium),
        Font(Res.font.poppins_regular, FontWeight.Normal),
        Font(Res.font.poppins_light, FontWeight.Light),
        Font(Res.font.poppins_thin, FontWeight.Thin),
        Font(Res.font.poppins_extra_light, FontWeight.ExtraLight),
        Font(Res.font.poppins_extra_bold, FontWeight.ExtraBold),
    )
}

// Set of Material typography styles to start with
@Composable
internal fun mifosTypography() = Typography(
    displayLarge = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Bottom,
            trim = LineHeightStyle.Trim.None,
        ),
    ),
    titleLarge = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    // Default text style
    bodyLarge = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None,
        ),
    ),
    bodyMedium = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    // Used for Button
    labelLarge = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    // Used for Navigation items
    labelMedium = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.LastLineBottom,
        ),
    ),
    // Used for Tag
    labelSmall = TextStyle(
        fontFamily = appFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.LastLineBottom,
        ),
    ),
)
