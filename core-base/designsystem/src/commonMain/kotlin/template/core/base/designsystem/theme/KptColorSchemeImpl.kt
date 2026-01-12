/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import template.core.base.designsystem.core.ComponentDsl
import template.core.base.designsystem.core.KptColorScheme
import template.core.base.designsystem.core.KptElevation
import template.core.base.designsystem.core.KptShapes
import template.core.base.designsystem.core.KptSpacing
import template.core.base.designsystem.core.KptThemeProvider
import template.core.base.designsystem.core.KptTypography

@Immutable
data class KptColorSchemeImpl(
    override val primary: Color = Color(0xFF6750A4),
    override val onPrimary: Color = Color(0xFFFFFFFF),
    override val primaryContainer: Color = Color(0xFFEADDFF),
    override val onPrimaryContainer: Color = Color(0xFF21005D),
    override val secondary: Color = Color(0xFF625B71),
    override val onSecondary: Color = Color(0xFFFFFFFF),
    override val secondaryContainer: Color = Color(0xFFE8DEF8),
    override val onSecondaryContainer: Color = Color(0xFF1D192B),
    override val tertiary: Color = Color(0xFF7D5260),
    override val onTertiary: Color = Color(0xFFFFFFFF),
    override val tertiaryContainer: Color = Color(0xFFFFD8E4),
    override val onTertiaryContainer: Color = Color(0xFF31111D),
    override val error: Color = Color(0xFFBA1A1A),
    override val onError: Color = Color(0xFFFFFFFF),
    override val errorContainer: Color = Color(0xFFFFDAD6),
    override val onErrorContainer: Color = Color(0xFF410002),
    override val background: Color = Color(0xFFFFFBFE),
    override val onBackground: Color = Color(0xFF1C1B1F),
    override val surface: Color = Color(0xFFFFFBFE),
    override val onSurface: Color = Color(0xFF1C1B1F),
    override val surfaceVariant: Color = Color(0xFFE7E0EC),
    override val onSurfaceVariant: Color = Color(0xFF49454F),
    override val outline: Color = Color(0xFF79747E),
    override val outlineVariant: Color = Color(0xFFCAC4D0),
    override val scrim: Color = Color(0xFF000000),
    override val inverseSurface: Color = Color(0xFF313033),
    override val inverseOnSurface: Color = Color(0xFFF4EFF4),
    override val inversePrimary: Color = Color(0xFFD0BCFF),
    override val surfaceDim: Color = Color(0xFFDAD6DC),
    override val surfaceBright: Color = Color(0xFFFFFBFE),
    override val surfaceContainerLowest: Color = Color(0xFFFFFFFF),
    override val surfaceContainerLow: Color = Color(0xFFF3EFF4),
    override val surfaceContainer: Color = Color(0xFFE7E0EC),
    override val surfaceContainerHigh: Color = Color(0xFFDAD6DC),
    override val surfaceContainerHighest: Color = Color(0xFFCFC8D0),
    override val surfaceTint: Color = Color(0xFF6750A4),
) : KptColorScheme

@Immutable
data class KptTypographyImpl(
    override val displayLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    override val displayMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    override val displaySmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    override val headlineLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    override val headlineMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    override val headlineSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    override val titleLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    override val titleMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    override val titleSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    override val bodyLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    override val bodyMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    override val bodySmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    override val labelLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    override val labelMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    override val labelSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
) : KptTypography

@Immutable
data class KptShapesImpl(
    override val extraSmall: CornerBasedShape = RoundedCornerShape(4.dp),
    override val small: CornerBasedShape = RoundedCornerShape(8.dp),
    override val medium: CornerBasedShape = RoundedCornerShape(12.dp),
    override val large: CornerBasedShape = RoundedCornerShape(16.dp),
    override val extraLarge: CornerBasedShape = RoundedCornerShape(28.dp),
) : KptShapes

@Immutable
data class KptSpacingImpl(
    override val xs: Dp = 4.dp,
    override val sm: Dp = 8.dp,
    override val md: Dp = 16.dp,
    override val lg: Dp = 24.dp,
    override val xl: Dp = 32.dp,
    override val xxl: Dp = 64.dp,
) : KptSpacing

@Immutable
data class KptElevationImpl(
    override val level0: Dp = 0.dp,
    override val level1: Dp = 1.dp,
    override val level2: Dp = 3.dp,
    override val level3: Dp = 6.dp,
    override val level4: Dp = 8.dp,
    override val level5: Dp = 12.dp,
) : KptElevation

@Immutable
data class KptThemeProviderImpl(
    override val colors: KptColorScheme = KptColorSchemeImpl(),
    override val typography: KptTypography = KptTypographyImpl(),
    override val shapes: KptShapes = KptShapesImpl(),
    override val spacing: KptSpacing = KptSpacingImpl(),
    override val elevation: KptElevation = KptElevationImpl(),
) : KptThemeProvider

val LocalKptColors = staticCompositionLocalOf<KptColorScheme> { KptColorSchemeImpl() }
val LocalKptTypography = staticCompositionLocalOf<KptTypography> { KptTypographyImpl() }
val LocalKptShapes = staticCompositionLocalOf<KptShapes> { KptShapesImpl() }
val LocalKptSpacing = staticCompositionLocalOf<KptSpacing> { KptSpacingImpl() }
val LocalKptElevation = staticCompositionLocalOf<KptElevation> { KptElevationImpl() }

@ComponentDsl
class KptThemeBuilder {
    private var colors: KptColorScheme = KptColorSchemeImpl()
    private var typography: KptTypography = KptTypographyImpl()
    private var shapes: KptShapes = KptShapesImpl()
    private var spacing: KptSpacing = KptSpacingImpl()
    private var elevation: KptElevation = KptElevationImpl()

    fun colors(block: KptColorSchemeBuilder.() -> Unit) {
        colors = KptColorSchemeBuilder().apply(block).build()
    }

    fun typography(block: KptTypographyBuilder.() -> Unit) {
        typography = KptTypographyBuilder().apply(block).build()
    }

    fun shapes(block: KptShapesBuilder.() -> Unit) {
        shapes = KptShapesBuilder().apply(block).build()
    }

    fun spacing(block: KptSpacingBuilder.() -> Unit) {
        spacing = KptSpacingBuilder().apply(block).build()
    }

    fun elevation(block: KptElevationBuilder.() -> Unit) {
        elevation = KptElevationBuilder().apply(block).build()
    }

    fun build(): KptThemeProvider = KptThemeProviderImpl(
        colors = colors,
        typography = typography,
        shapes = shapes,
        spacing = spacing,
        elevation = elevation,
    )
}

@ComponentDsl
class KptColorSchemeBuilder {
    var primary: Color = Color(0xFF6750A4)
    var onPrimary: Color = Color(0xFFFFFFFF)
    var primaryContainer: Color = Color(0xFFEADDFF)
    var onPrimaryContainer: Color = Color(0xFF21005D)
    var secondary: Color = Color(0xFF625B71)
    var onSecondary: Color = Color(0xFFFFFFFF)
    var secondaryContainer: Color = Color(0xFFE8DEF8)
    var onSecondaryContainer: Color = Color(0xFF1D192B)
    var tertiary: Color = Color(0xFF7D5260)
    var onTertiary: Color = Color(0xFFFFFFFF)
    var tertiaryContainer: Color = Color(0xFFFFD8E4)
    var onTertiaryContainer: Color = Color(0xFF31111D)
    var error: Color = Color(0xFFBA1A1A)
    var onError: Color = Color(0xFFFFFFFF)
    var errorContainer: Color = Color(0xFFFFDAD6)
    var onErrorContainer: Color = Color(0xFF410002)
    var background: Color = Color(0xFFFFFBFE)
    var onBackground: Color = Color(0xFF1C1B1F)
    var surface: Color = Color(0xFFFFFBFE)
    var onSurface: Color = Color(0xFF1C1B1F)
    var surfaceVariant: Color = Color(0xFFE7E0EC)
    var onSurfaceVariant: Color = Color(0xFF49454F)
    var outline: Color = Color(0xFF79747E)
    var outlineVariant: Color = Color(0xFFCAC4D0)

    fun build(): KptColorScheme = KptColorSchemeImpl(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
    )
}

@ComponentDsl
class KptTypographyBuilder {
    var displayLarge: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 57.sp)
    var displayMedium: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 45.sp)
    var displaySmall: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 36.sp)
    var headlineLarge: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 32.sp)
    var headlineMedium: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 28.sp)
    var headlineSmall: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 24.sp)
    var titleLarge: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 22.sp)
    var titleMedium: TextStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp)
    var titleSmall: TextStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp)
    var bodyLarge: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)
    var bodyMedium: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
    var bodySmall: TextStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp)
    var labelLarge: TextStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp)
    var labelMedium: TextStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)
    var labelSmall: TextStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp)

    fun build(): KptTypography = KptTypographyImpl(
        displayLarge = displayLarge,
        displayMedium = displayMedium,
        displaySmall = displaySmall,
        headlineLarge = headlineLarge,
        headlineMedium = headlineMedium,
        headlineSmall = headlineSmall,
        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall,
        labelLarge = labelLarge,
        labelMedium = labelMedium,
        labelSmall = labelSmall,
    )
}

@ComponentDsl
class KptShapesBuilder {
    var extraSmall: CornerBasedShape = RoundedCornerShape(4.dp)
    var small: CornerBasedShape = RoundedCornerShape(8.dp)
    var medium: CornerBasedShape = RoundedCornerShape(12.dp)
    var large: CornerBasedShape = RoundedCornerShape(16.dp)
    var extraLarge: CornerBasedShape = RoundedCornerShape(28.dp)

    fun build(): KptShapes = KptShapesImpl(
        extraSmall = extraSmall,
        small = small,
        medium = medium,
        large = large,
        extraLarge = extraLarge,
    )
}

@ComponentDsl
class KptSpacingBuilder {
    var xs: Dp = 4.dp
    var sm: Dp = 8.dp
    var md: Dp = 16.dp
    var lg: Dp = 24.dp
    var xl: Dp = 32.dp
    var xxl: Dp = 64.dp

    fun build(): KptSpacing = KptSpacingImpl(
        xs = xs,
        sm = sm,
        md = md,
        lg = lg,
        xl = xl,
        xxl = xxl,
    )
}

@ComponentDsl
class KptElevationBuilder {
    var level0: Dp = 0.dp
    var level1: Dp = 1.dp
    var level2: Dp = 3.dp
    var level3: Dp = 6.dp
    var level4: Dp = 8.dp
    var level5: Dp = 12.dp

    fun build(): KptElevation = KptElevationImpl(
        level0 = level0,
        level1 = level1,
        level2 = level2,
        level3 = level3,
        level4 = level4,
        level5 = level5,
    )
}

object KptTheme {
    val colorScheme: KptColorScheme
        @Composable get() = LocalKptColors.current

    val typography: KptTypography
        @Composable get() = LocalKptTypography.current

    val shapes: KptShapes
        @Composable get() = LocalKptShapes.current

    val spacing: KptSpacing
        @Composable get() = LocalKptSpacing.current

    val elevation: KptElevation
        @Composable get() = LocalKptElevation.current
}

fun kptTheme(block: KptThemeBuilder.() -> Unit): KptThemeProvider {
    return KptThemeBuilder().apply(block).build()
}
