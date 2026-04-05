/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mifos_mobile.core.designsystem.generated.resources.NotoSansArabic_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSansBengali_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSansDevanagari_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSansKannada_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSansKhmer_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSansMalayalam_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSansMyanmar_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSansTelugu_Regular
import mifos_mobile.core.designsystem.generated.resources.NotoSans_Regular
import mifos_mobile.core.designsystem.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
internal fun fontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.NotoSans_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansDevanagari_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansBengali_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansKannada_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansTelugu_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansArabic_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansKhmer_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansMyanmar_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansMalayalam_Regular, FontWeight.Normal),
    )
}

/**
 * A workaround for a known issue in Compose Web (Wasm/JS) with Skia font fallback.
 * By rendering these fonts in an invisible box, Compose is forced to download them
 * asynchronously at startup so they are available immediately when Skia needs them
 * as fallbacks for missing glyphs.
 */
@Suppress("ModifierMissing")
@Composable
internal fun FontFallbackPreloader() {
    Box(Modifier.size(0.dp)) {
        val fonts = listOf(
            FontFamily(Font(Res.font.NotoSansDevanagari_Regular, FontWeight.Normal)),
            FontFamily(Font(Res.font.NotoSansBengali_Regular, FontWeight.Normal)),
            FontFamily(Font(Res.font.NotoSansKannada_Regular, FontWeight.Normal)),
            FontFamily(Font(Res.font.NotoSansTelugu_Regular, FontWeight.Normal)),
            FontFamily(Font(Res.font.NotoSansArabic_Regular, FontWeight.Normal)),
            FontFamily(Font(Res.font.NotoSansKhmer_Regular, FontWeight.Normal)),
            FontFamily(Font(Res.font.NotoSansMyanmar_Regular, FontWeight.Normal)),
            FontFamily(Font(Res.font.NotoSansMalayalam_Regular, FontWeight.Normal)),
        )
        fonts.forEach { font ->
            Text(
                text = " ",
                fontFamily = font,
            )
        }
    }
}

// Set of Material typography styles to start with
@Composable
internal fun appTypography() = Typography(
    displayLarge = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = fontFamily(),
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
        fontFamily = fontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    // Default text style
    bodyLarge = TextStyle(
        fontFamily = fontFamily(),
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
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    // Used for Button
    labelLarge = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    // Used for Navigation items
    labelMedium = TextStyle(
        fontFamily = fontFamily(),
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
        fontFamily = fontFamily(),
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

object MifosTypography {
    val displayLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 57.sp,
            lineHeight = 64.sp,
            fontFamily = fontFamily(),
            letterSpacing = (-0.25).sp,
            fontWeight = FontWeight.Normal,
        )

    val displayMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 45.sp,
            lineHeight = 52.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight.Normal,
        )

    val displaySmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 36.sp,
            lineHeight = 44.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(700),
        )

    val headlineLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(600),
        )

    // verified
    val headlineMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(700),
        )

    // verified
    val headlineSmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(400),
        )

    // verified
    val headlineSmallEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(700),
        )

    // verified
    val titleLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(700),
        )

    val titleLargeEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(500),
        )

    // verified
    val titleMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(500),
        )

    val titleMediumEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(600),
        )

    val titleSmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(400),
        )

    // verified
    val titleSmallEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(600),
        )

    val labelLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.1.sp,
            fontWeight = FontWeight(500),
        )

    val labelLargeEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.1.sp,
            fontWeight = FontWeight(600),
        )

    // verified
    val labelMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight(500),
        )

    val labelMediumEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight(600),
        )

    val labelSmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight(500),
        )

    val labelSmallEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight(600),
        )

    // verified
    val bodyLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight(400),
        )

    val bodyMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.25.sp,
            fontWeight = FontWeight(400),
        )

    val bodyMediumEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.25.sp,
            fontWeight = FontWeight(500),
        )

    // verified
    val bodySmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.4.sp,
            fontWeight = FontWeight(400),
        )

    val bodySmallEmphasized: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = fontFamily(),
            letterSpacing = 0.4.sp,
            fontWeight = FontWeight(500),
        )

    // verified
    val tag: TextStyle
        @Composable get() = TextStyle(
            fontSize = 10.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(600),
        )

    val keyBoardNumeric: TextStyle
        @Composable get() = TextStyle(
            fontSize = 21.sp,
            lineHeight = 24.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(400),

        )

    val keyBoardAlpha: TextStyle
        @Composable get() = TextStyle(
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight(400),
        )
}
