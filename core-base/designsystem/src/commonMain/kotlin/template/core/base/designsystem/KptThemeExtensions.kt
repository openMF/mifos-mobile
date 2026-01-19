/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import template.core.base.designsystem.core.KptColorScheme
import template.core.base.designsystem.core.KptElevation
import template.core.base.designsystem.core.KptShapes
import template.core.base.designsystem.core.KptSpacing
import template.core.base.designsystem.core.KptTypography
import template.core.base.designsystem.theme.KptColorSchemeImpl
import template.core.base.designsystem.theme.KptShapesImpl
import template.core.base.designsystem.theme.KptTheme
import template.core.base.designsystem.theme.KptTypographyImpl

/**
 * Creates [PaddingValues] using KPT spacing tokens with horizontal and vertical values.
 *
 * This extension function provides a convenient way to create consistent padding
 * using the design system's spacing scale.
 *
 * Example usage:
 * ```
 * Box(
 *     modifier = Modifier.padding(
 *         KptTheme.spacing.paddingValues(
 *             horizontal = KptTheme.spacing.lg,
 *             vertical = KptTheme.spacing.md
 *         )
 *     )
 * )
 * ```
 *
 * @param horizontal Horizontal padding (start and end). Defaults to [md]
 * @param vertical Vertical padding (top and bottom). Defaults to [md]
 * @return [PaddingValues] configured with the specified spacing
 *
 * @see KptSpacing
 */
@Composable
fun KptSpacing.paddingValues(
    horizontal: Dp = md,
    vertical: Dp = md,
): PaddingValues = PaddingValues(horizontal = horizontal, vertical = vertical)

/**
 * Creates [PaddingValues] using KPT spacing tokens with individual edge values.
 *
 * This extension function provides fine-grained control over padding for each edge
 * while maintaining consistency with the design system's spacing scale.
 *
 * Example usage:
 * ```
 * Card(
 *     modifier = Modifier.padding(
 *         KptTheme.spacing.paddingValues(
 *             start = KptTheme.spacing.lg,
 *             top = KptTheme.spacing.md,
 *             end = KptTheme.spacing.lg,
 *             bottom = KptTheme.spacing.xl
 *         )
 *     )
 * )
 * ```
 *
 * @param start Padding for the start edge (left in LTR, right in RTL). Defaults to [md]
 * @param top Padding for the top edge. Defaults to [md]
 * @param end Padding for the end edge (right in LTR, left in RTL). Defaults to [md]
 * @param bottom Padding for the bottom edge. Defaults to [md]
 * @return [PaddingValues] configured with the specified spacing for each edge
 *
 * @see KptSpacing
 */
@Composable
fun KptSpacing.paddingValues(
    start: Dp = md,
    top: Dp = md,
    end: Dp = md,
    bottom: Dp = md,
): PaddingValues = PaddingValues(start = start, top = top, end = end, bottom = bottom)

@Composable
fun KptTypography.toMaterial3Typography(fontFamily: FontFamily? = FontFamily.Default): Typography {
    return Typography(
        displayLarge = this.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = this.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = this.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = this.headlineSmall.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = Trim.None,
            ),
        ),
        titleLarge = this.titleLarge.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = Trim.LastLineBottom,
            ),
        ),
        titleMedium = this.titleMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        ),
        titleSmall = this.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = this.bodyLarge.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.None,
            ),
        ),
        bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = this.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = this.labelLarge.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        labelMedium = this.labelMedium.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        labelSmall = this.labelSmall.copy(
            fontFamily = fontFamily,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
    )
}

fun Typography.toKptTypography(fontFamily: FontFamily? = FontFamily.Default): KptTypography = KptTypographyImpl(
    displayLarge = this.displayLarge.copy(fontFamily = fontFamily),
    displayMedium = this.displayMedium.copy(fontFamily = fontFamily),
    displaySmall = this.displaySmall.copy(fontFamily = fontFamily),
    headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall = this.headlineSmall.copy(
        fontFamily = fontFamily,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Bottom,
            trim = Trim.None,
        ),
    ),
    titleLarge = this.titleLarge.copy(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Bottom,
            trim = Trim.LastLineBottom,
        ),
    ),
    titleMedium = this.titleMedium.copy(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    titleSmall = this.titleSmall.copy(fontFamily = fontFamily),
    bodyLarge = this.bodyLarge.copy(
        fontFamily = fontFamily,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = Trim.None,
        ),
    ),
    bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily),
    bodySmall = this.bodySmall.copy(fontFamily = fontFamily),
    labelLarge = this.labelLarge.copy(
        fontFamily = fontFamily,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
    labelMedium = this.labelMedium.copy(
        fontFamily = fontFamily,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
    labelSmall = this.labelSmall.copy(
        fontFamily = fontFamily,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    ),
)

/**
 * Extension function to convert KptTypography to Material3 Typography
 * This ensures that all Material3 components automatically use KptTheme typography
 */
fun KptTypography.toMaterial3Typography(): Typography {
    return Typography(
        displayLarge = this.displayLarge,
        displayMedium = this.displayMedium,
        displaySmall = this.displaySmall,
        headlineLarge = this.headlineLarge,
        headlineMedium = this.headlineMedium,
        headlineSmall = this.headlineSmall,
        titleLarge = this.titleLarge,
        titleMedium = this.titleMedium,
        titleSmall = this.titleSmall,
        bodyLarge = this.bodyLarge,
        bodyMedium = this.bodyMedium,
        bodySmall = this.bodySmall,
        labelLarge = this.labelLarge,
        labelMedium = this.labelMedium,
        labelSmall = this.labelSmall,
    )
}

fun Typography.toKptTypography(): KptTypography = KptTypographyImpl(
    displayLarge = this.displayLarge,
    displayMedium = this.displayMedium,
    displaySmall = this.displaySmall,
    headlineLarge = this.headlineLarge,
    headlineMedium = this.headlineMedium,
    headlineSmall = this.headlineSmall,
    titleLarge = this.titleLarge,
    titleMedium = this.titleMedium,
    titleSmall = this.titleSmall,
    bodyLarge = this.bodyLarge,
    bodyMedium = this.bodyMedium,
    bodySmall = this.bodySmall,
    labelLarge = this.labelLarge,
    labelMedium = this.labelMedium,
    labelSmall = this.labelSmall,
)

/**
 * Extension function to convert KptColorScheme to Material3 ColorScheme
 * This ensures that all Material3 components automatically use KptTheme colors
 */
@Composable
fun KptColorScheme.toMaterial3ColorScheme(): ColorScheme {
    return ColorScheme(
        primary = this.primary,
        onPrimary = this.onPrimary,
        primaryContainer = this.primaryContainer,
        onPrimaryContainer = this.onPrimaryContainer,
        inversePrimary = this.inversePrimary,
        secondary = this.secondary,
        onSecondary = this.onSecondary,
        secondaryContainer = this.secondaryContainer,
        onSecondaryContainer = this.onSecondaryContainer,
        tertiary = this.tertiary,
        onTertiary = this.onTertiary,
        tertiaryContainer = this.tertiaryContainer,
        onTertiaryContainer = this.onTertiaryContainer,
        background = this.background,
        onBackground = this.onBackground,
        surface = this.surface,
        onSurface = this.onSurface,
        surfaceVariant = this.surfaceVariant,
        onSurfaceVariant = this.onSurfaceVariant,
        surfaceTint = this.primary,
        inverseSurface = this.inverseSurface,
        inverseOnSurface = this.inverseOnSurface,
        error = this.error,
        onError = this.onError,
        errorContainer = this.errorContainer,
        onErrorContainer = this.onErrorContainer,
        outline = this.outline,
        outlineVariant = this.outlineVariant,
        scrim = this.scrim,
        surfaceBright = this.surfaceBright,
        surfaceDim = this.surfaceDim,
        surfaceContainer = this.surfaceContainer,
        surfaceContainerHigh = this.surfaceContainerHigh,
        surfaceContainerHighest = this.surfaceContainerHighest,
        surfaceContainerLow = this.surfaceContainerLow,
        surfaceContainerLowest = this.surfaceContainerLowest,
    )
}

fun ColorScheme.toKptColorScheme(): KptColorScheme = KptColorSchemeImpl(
    primary = this.primary,
    onPrimary = this.onPrimary,
    primaryContainer = this.primaryContainer,
    onPrimaryContainer = this.onPrimaryContainer,
    inversePrimary = this.inversePrimary,
    secondary = this.secondary,
    onSecondary = this.onSecondary,
    secondaryContainer = this.secondaryContainer,
    onSecondaryContainer = this.onSecondaryContainer,
    tertiary = this.tertiary,
    onTertiary = this.onTertiary,
    tertiaryContainer = this.tertiaryContainer,
    onTertiaryContainer = this.onTertiaryContainer,
    background = this.background,
    onBackground = this.onBackground,
    surface = this.surface,
    onSurface = this.onSurface,
    surfaceVariant = this.surfaceVariant,
    onSurfaceVariant = this.onSurfaceVariant,
    surfaceTint = this.surfaceTint,
    inverseSurface = this.inverseSurface,
    inverseOnSurface = this.inverseOnSurface,
    error = this.error,
    onError = this.onError,
    errorContainer = this.errorContainer,
    onErrorContainer = this.onErrorContainer,
    outline = this.outline,
    outlineVariant = this.outlineVariant,
    scrim = this.scrim,
    surfaceBright = this.surfaceBright,
    surfaceDim = this.surfaceDim,
    surfaceContainer = this.surfaceContainer,
    surfaceContainerHigh = this.surfaceContainerHigh,
    surfaceContainerHighest = this.surfaceContainerHighest,
    surfaceContainerLow = this.surfaceContainerLow,
    surfaceContainerLowest = this.surfaceContainerLowest,
)

/**
 * Extension function to convert KptShapes to Material3 Shapes
 * This ensures that all Material3 components automatically use KptTheme shapes
 */
fun KptShapes.toMaterial3Shapes(): Shapes {
    return Shapes(
        extraSmall = this.extraSmall,
        small = this.small,
        medium = this.medium,
        large = this.large,
        extraLarge = this.extraLarge,
    )
}

fun Shapes.toKptShapes(): KptShapes = KptShapesImpl(
    extraSmall = this.extraSmall,
    small = this.small,
    medium = this.medium,
    large = this.large,
    extraLarge = this.extraLarge,
)

/**
 * Get CardDefaults.cardElevation using KptTheme elevation
 */
@Composable
fun KptElevation.cardElevation(
    defaultElevation: Dp = level1,
    pressedElevation: Dp = level2,
    focusedElevation: Dp = level2,
    hoveredElevation: Dp = level2,
    draggedElevation: Dp = level4,
    disabledElevation: Dp = level0,
): CardElevation = CardDefaults.cardElevation(
    defaultElevation = defaultElevation,
    pressedElevation = pressedElevation,
    focusedElevation = focusedElevation,
    hoveredElevation = hoveredElevation,
    draggedElevation = draggedElevation,
    disabledElevation = disabledElevation,
)

/**
 * Predefined spacing combinations for common UI patterns.
 *
 * This object provides convenient access to commonly used padding configurations
 * that follow design system best practices. Use these instead of hardcoded values
 * to maintain consistency across the application.
 *
 * Example usage:
 * ```
 * // Apply standard screen padding
 * Column(
 *     modifier = Modifier.padding(KptSpacingDefaults.screenPadding())
 * ) {
 *     // Screen content
 * }
 *
 * // Apply card content padding
 * Card {
 *     Column(
 *         modifier = Modifier.padding(KptSpacingDefaults.cardPadding())
 *     ) {
 *         // Card content
 *     }
 * }
 * ```
 */
object KptSpacingDefaults {
    /**
     * Standard padding for screen-level content.
     * Horizontal: lg (24dp), Vertical: md (16dp)
     * Best for: Main screen content, page layouts
     */
    @Composable
    fun screenPadding() = KptTheme.spacing.paddingValues(
        horizontal = KptTheme.spacing.lg,
        vertical = KptTheme.spacing.md,
    )

    /**
     * Standard padding for card content.
     * Horizontal: md (16dp), Vertical: sm (8dp)
     * Best for: Content inside cards, list items
     */
    @Composable
    fun cardPadding() = KptTheme.spacing.paddingValues(
        horizontal = KptTheme.spacing.md,
        vertical = KptTheme.spacing.sm,
    )

    /**
     * Standard padding for button content.
     * Horizontal: lg (24dp), Vertical: sm (8dp)
     * Best for: Button internal padding, touch targets
     */
    @Composable
    fun buttonPadding() = KptTheme.spacing.paddingValues(
        horizontal = KptTheme.spacing.lg,
        vertical = KptTheme.spacing.sm,
    )
}

/**
 * Predefined elevation configurations for common UI patterns.
 *
 * This object provides semantically meaningful elevation presets that follow
 * Material Design elevation guidelines. Use these to maintain consistent
 * visual hierarchy throughout the application.
 *
 * Example usage:
 * ```
 * // Standard card elevation
 * Card(elevation = KptElevationDefaults.card()) {
 *     // Card content
 * }
 *
 * // Prominent card for important content
 * Card(elevation = KptElevationDefaults.raisedCard()) {
 *     // Important content
 * }
 * ```
 */
object KptElevationDefaults {
    /**
     * Standard card elevation for normal content.
     * Default: level1 (1dp), Pressed: level2 (3dp)
     * Best for: Regular cards, list items, content containers
     */
    @Composable
    fun card() = KptTheme.elevation.cardElevation(
        defaultElevation = KptTheme.elevation.level1,
        pressedElevation = KptTheme.elevation.level2,
    )

    /**
     * Elevated card for prominent content.
     * Default: level3 (6dp), Pressed: level4 (8dp)
     * Best for: Featured content, important cards, floating panels
     */
    @Composable
    fun raisedCard() = KptTheme.elevation.cardElevation(
        defaultElevation = KptTheme.elevation.level3,
        pressedElevation = KptTheme.elevation.level4,
    )

    /**
     * High elevation for modal content.
     * Default: level5 (12dp)
     * Best for: Dialogs, modal bottom sheets, overlays
     */
    @Composable
    fun dialogCard() = KptTheme.elevation.cardElevation(
        defaultElevation = KptTheme.elevation.level5,
    )
}

/**
 * Provides convenient access to container color combinations.
 *
 * This extension property groups related container colors and their corresponding
 * content colors for easy access. Container colors are typically used for
 * backgrounds, surfaces, and filled components.
 *
 * Example usage:
 * ```
 * val colors = KptTheme.colorScheme.containerColors
 *
 * Card(
 *     colors = CardDefaults.cardColors(
 *         containerColor = colors.primary,
 *         contentColor = colors.onPrimary
 *     )
 * ) {
 *     // Card content
 * }
 * ```
 *
 * @see ContainerColors
 * @see KptColorScheme
 */
@get:Composable
val KptColorScheme.containerColors: ContainerColors
    get() = ContainerColors(
        primary = primaryContainer,
        onPrimary = onPrimaryContainer,
        secondary = secondaryContainer,
        onSecondary = onSecondaryContainer,
        tertiary = tertiaryContainer,
        onTertiary = onTertiaryContainer,
        error = errorContainer,
        onError = onErrorContainer,
    )

/**
 * A collection of container colors and their corresponding content colors.
 *
 * This data class groups semantically related color pairs to make it easier
 * to apply consistent color schemes to components that need both background
 * and foreground colors.
 *
 * @param primary Primary container background color
 * @param onPrimary Color for content on primary container backgrounds
 * @param secondary Secondary container background color
 * @param onSecondary Color for content on secondary container backgrounds
 * @param tertiary Tertiary container background color
 * @param onTertiary Color for content on tertiary container backgrounds
 * @param error Error container background color
 * @param onError Color for content on error container backgrounds
 *
 * @see KptColorScheme.containerColors
 */
data class ContainerColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val error: Color,
    val onError: Color,
)
