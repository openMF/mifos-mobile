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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

object DesignToken {
    val spacing: AppSpacing
        @Composable
        get() = LocalSpacing.current

    val padding: AppPadding
        @Composable
        get() = LocalPadding.current

    val shapes: AppShapes
        @Composable
        get() = LocalShapes.current

    val elevation: AppElevation
        @Composable
        get() = LocalElevation.current

    val sizes: AppSizes
        @Composable
        get() = LocalSizes.current

    val strokes: AppStrokes
        @Composable
        get() = LocalStrokes.current
}

/**
 * Defines standardized spacing values used throughout the application for
 * consistent layout spacing.
 *
 * This immutable data class provides a comprehensive set of spacing values
 * that should be used for margins, gaps between elements, and other layout
 * spacing requirements.
 *
 * Usage example:
 * ```
 * Column(
 *     verticalArrangement = Arrangement.spacedBy(DesignTokens.spacing.medium)
 * ) {
 *     Text("First item")
 *     Text("Second item")
 * }
 * ```
 *
 * @param none Zero spacing for elements that should have no gap (0.dp).
 * @param extraSmall Minimal spacing for very tight layouts or dense UI areas (4.dp).
 * @param small Small spacing typically used for compact components (8.dp).
 * @param medium Moderate spacing used for standard component separation (12.dp).
 * @param large Large spacing suitable for well-spaced layout blocks (16.dp).
 * @param largeIncreased Slightly larger than `large` for extra breathing room (20.dp).
 * @param extraLarge Extra spacing for visual emphasis between major sections (28.dp).
 * @param extraLargeIncreased Even more prominent spacing, useful in scrollable or modal layouts (32.dp).
 * @param extraExtraLarge Significant spacing for major layout divisions or empty states (48.dp).
 * @param full Maximum spacing, typically used for centering or deep offset layouts (1000.dp).
 */
@Immutable
data class AppSpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val largeIncreased: Dp = 20.dp,
    val extraLarge: Dp = 28.dp,
    val extraLargeIncreased: Dp = 32.dp,
    val extraExtraLarge: Dp = 48.dp,
    val full: Dp = 1000.dp,
    // custom spacings
    val negativeDp7: Dp = (-7).dp,
    val dp2: Dp = 2.dp,
    val dp5: Dp = 5.dp,
    val dp6: Dp = 6.dp,
    val dp10: Dp = 10.dp,
    val dp24: Dp = 24.dp,
    val dp40: Dp = 40.dp,
    val dp50: Dp = 50.dp,
)

/**
 * Defines standardized padding values for consistent internal spacing
 * within components.
 *
 * This immutable data class provides padding values that should be used
 * for internal component spacing, content padding, and other internal
 * layout requirements. These values are optimized for touch targets and
 * readability.
 *
 * Usage example:
 * ```
 * Card(
 *     modifier = Modifier.padding(DesignTokens.padding.card)
 * ) {
 *     Text(
 *         text = "Content",
 *         modifier = Modifier.padding(DesignTokens.padding.content)
 *     )
 * }
 * ```
 *
 * @param none Zero padding for elements that should have no internal spacing (0.dp).
 * @param extraSmall Very minimal padding for extremely compact UI elements (4.dp).
 * @param small Small padding suitable for compact components (8.dp).
 * @param medium Moderate padding for standard components (12.dp).
 * @param large Generous padding used in card or section layouts (16.dp).
 * @param largeIncreased Slightly more than large for edge cases requiring more spacing (20.dp).
 * @param extraLarge Larger padding for major containers or elevated surfaces (28.dp).
 * @param extraLargeIncreased Even more prominent padding for large UI blocks (32.dp).
 * @property extraExtraLarge Very large padding for full-width layouts or modal spacing (48.dp).
 * @param full Extremely large padding for special layout use-cases like centering or offset (1000.dp).
 */

@Immutable
data class AppPadding(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val largeIncreased: Dp = 20.dp,
    val extraLarge: Dp = 28.dp,
    val extraLargeIncreased: Dp = 32.dp,
    val extraExtraLarge: Dp = 48.dp,
    val full: Dp = 1000.dp,
    // custom paddings
    val dp2: Dp = 2.dp,
    val dp6: Dp = 6.dp,
    val dp10: Dp = 10.dp,
    val dp14: Dp = 14.dp,
    val dp56: Dp = 56.dp,
    val dp75: Dp = 75.dp,
    val dp100: Dp = 100.dp,
)

/**
 * Defines standardized shape values for consistent corner radii and border
 * shapes across components.
 *
 * This immutable data class provides shape definitions that ensure visual
 * consistency across all UI components. These shapes follow Material
 * Design principles while maintaining the application's visual identity.
 *
 * Usage example:
 * ```
 * Card(
 *     shape = DesignTokens.shapes.card
 * ) {
 *     // Card content
 * }
 *
 * Button(
 *     shape = DesignTokens.shapes.button,
 *     onClick = { }
 * ) {
 *     Text("Button")
 * }
 * ```
 *
 * @param none No corner radius; results in sharp rectangular shapes (0.dp).
 * @param extraSmall Extra small corner radius for minimal rounding (4.dp).
 * @v small Small corner radius for subtle rounded corners (8.dp).
 * @param medium Medium corner radius for standard UI components (12.dp).
 * @param large Large corner radius for more pronounced rounding (16.dp).
 * @param largeIncreased Slightly larger than large for prominent components (20.dp).
 * @param extraLarge Extra large corner radius for highly rounded UI elements (28.dp).
 * @param extraLargeIncreased Larger extra-large radius for rounded designs (32.dp).
 * @param extraExtraLarge Extremely large radius for pill-shaped or very soft corners (48.dp).
 * @param full Fully rounded corners for shapes like capsules (1000.dp).
 * @param circle Perfectly circular shape (50% radius of height/width).
 * @param bottomSheet Shape for bottom sheets with only top corners rounded (16.dp topStart, topEnd).
 * @param topBar Shape for top bars with only bottom corners rounded (16.dp bottomStart, bottomEnd).
 */
@Immutable
data class AppShapes(
    val none: Shape = RoundedCornerShape(0.dp),
    val extraSmall: Shape = RoundedCornerShape(4.dp),
    val small: Shape = RoundedCornerShape(8.dp),
    val medium: Shape = RoundedCornerShape(12.dp),
    val large: Shape = RoundedCornerShape(16.dp),
    val largeIncreased: Shape = RoundedCornerShape(20.dp),
    val extraLarge: Shape = RoundedCornerShape(28.dp),
    val extraLargeIncreased: Shape = RoundedCornerShape(32.dp),
    val extraExtraLarge: Shape = RoundedCornerShape(48.dp),
    val full: Shape = RoundedCornerShape(1000.dp),
    val circle: Shape = RoundedCornerShape(50),
    val bottomSheet: Shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    val topBar: Shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    // custom shapes
    val topCornerDp8: Shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    val topCornerDp16: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    val bottomCornerDp8: Shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
    val bottomCornerDp12: Shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
    val dp2: Shape = RoundedCornerShape(2.dp),
    val dp25: Shape = RoundedCornerShape(25.dp),
    val dp100: Shape = RoundedCornerShape(100.dp),
)

/**
 * Defines standardized elevation values for consistent shadow depths and
 * layering across components.
 *
 * This immutable data class provides elevation values that create a
 * consistent visual hierarchy and depth perception throughout the
 * application. These values follow Material Design elevation guidelines
 * to ensure appropriate shadow depths for different component types.
 *
 * Usage example:
 * ```
 * Card(
 *     elevation = CardDefaults.cardElevation(
 *         defaultElevation = DesignTokens.elevation.card
 *     )
 * ) {
 *     // Card content
 * }
 *
 * FloatingActionButton(
 *     elevation = FloatingActionButtonDefaults.elevation(
 *         defaultElevation = DesignTokens.elevation.fab
 *     ),
 *     onClick = { }
 * ) {
 *     Icon(Icons.Default.Add, contentDescription = "Add")
 * }
 * ```
 *
 * @param none No elevation for flat components at surface level
 * @param elevation Dialog elevation for maximum prominence (25dp)
 */
@Immutable
data class AppElevation(
    val none: Dp = 0.dp,
    val elevation: Dp = 25.dp,
    // custom elevation
    val dp6: Dp = 6.dp,
    val dp2: Dp = 2.dp,
)

/**
 * Defines standardized size values for consistent component dimensions
 * across the application.
 *
 * This immutable data class provides size specifications that ensure
 * consistent dimensions for common UI elements like icons, avatars,
 * buttons, and input fields. These values are optimized for accessibility
 * and touch target requirements.
 *
 * Usage example:
 * ```
 * Icon(
 *     imageVector = Icons.Default.Home,
 *     contentDescription = "Home",
 *     modifier = Modifier.size(DesignTokens.sizes.iconMedium)
 * )
 *
 * Button(
 *     onClick = { },
 *     modifier = Modifier.height(DesignTokens.sizes.buttonHeight)
 * ) {
 *     Text("Action")
 * }
 *
 * AsyncImage(
 *     model = userAvatarUrl,
 *     contentDescription = "User Avatar",
 *     modifier = Modifier.size(DesignTokens.sizes.avatarLarge)
 * )
 * ```
 *
 * @param iconSmall Small icon size for compact layouts (16dp)
 * @param iconMedium Standard icon size for most use cases (24dp)
 * @param iconLarge Large icon size for prominent display (32dp)
 * @param avatarSmall Small avatar size for compact user representations
 *    (32dp)
 * @param avatarMedium Standard avatar size for most user displays (48dp)
 * @param avatarLarge Large avatar size for profile screens (64dp)
 * @param buttonHeight Standard height for button components (48dp)
 * @param inputHeight Standard height for input field components (56dp)
 * @param cardMinHeight Minimum height for card components to ensure proper
 *    proportions (120dp)
 */
@Immutable
data class AppSizes(
    val iconTiny: Dp = 12.dp,
    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 32.dp,
    val iconExtraLarge: Dp = 36.dp,
    val avatarSmall: Dp = 32.dp,
    val avatarMedium: Dp = 48.dp,
    val avatarLarge: Dp = 64.dp,
    val buttonHeight: Dp = 56.dp,
    val inputHeight: Dp = 56.dp,
    val cardMinHeight: Dp = 120.dp,
    val profile: Dp = 72.dp,
    val headerToContentHeight: Dp = 28.dp,
    // custom sizes
    val iconDp39: Dp = 39.dp,
    val iconDp100: Dp = 100.dp,
    val checkboxDp18: Dp = 18.dp,
    val imageDp14: Dp = 14.dp,
    val imageDp28: Dp = 28.dp,
    val imageDp48: Dp = 48.dp,
    val imageDp50: Dp = 50.dp,
    val imageDp60: Dp = 60.dp,
    val imageDp96: Dp = 96.dp,
    val imageDp100: Dp = 100.dp,
    val imageDp128: Dp = 128.dp,
    val imageDp140: Dp = 140.dp,
    val imageDp150: Dp = 150.dp,
    val imageDp165: Dp = 165.dp,
    val imageDp212: Dp = 212.dp,
    val iconDp20: Dp = 20.dp,
    val cardDp64: Dp = 64.dp,
    val cardDp112: Dp = 112.dp,
    val cardDp128: Dp = 128.dp,
    val boxDp4: Dp = 4.dp,
    val boxDp12: Dp = 12.dp,
    val boxDp36: Dp = 36.dp,
    val boxDp41: Dp = 41.dp,
    val boxDp76: Dp = 76.dp,
    val boxDp128: Dp = 128.dp,
    val boxDp100: Dp = 100.dp,
    val boxDp107: Dp = 107.33333.dp,
    val buttonDp50: Dp = 50.dp,
    val surfaceDp40: Dp = 40.dp,
    val minThumbSizeDp40: Dp = 40.dp,
    val lazyColHeightInDp500: Dp = 500.dp,
    val rippleRadiusDp24: Dp = 24.dp,
    val textDp40: Dp = 40.dp,
    val surfaceColWidthInDp80: Dp = 80.dp,
    val dropDownMenuHeightInDp200: Dp = 200.dp,
    val backgroundDp100: Dp = 100.dp,
    val buttonHeightDp48: Dp = 48.dp,
    val stepIndicatorDp40: Dp = 40.dp,
    val tableCellWidthSmall: Dp = 60.dp,
    val tableCellWidthMedium: Dp = 100.dp,
    val tableCellWidthLarge: Dp = 150.dp,
)

@Immutable
data class AppStrokes(
    val dpPoint5: Dp = 0.5.dp,
    val thin: Dp = 1.dp,
    val dp2: Dp = 2.dp,
    val dp5: Dp = 5.dp,
)

/**
 * CompositionLocal provider for accessing [AppSpacing] values throughout
 * the composition tree.
 *
 * This provides a default instance of [AppSpacing] that can be overridden
 * by providing a different value through [DesignTokenTheme].
 */
val LocalSpacing = staticCompositionLocalOf { AppSpacing() }

/**
 * CompositionLocal provider for accessing [AppPadding] values throughout
 * the composition tree.
 *
 * This provides a default instance of [AppPadding] that can be overridden
 * by providing a different value through [DesignTokenTheme].
 */
val LocalPadding = staticCompositionLocalOf { AppPadding() }

/**
 * CompositionLocal provider for accessing [AppShapes] values throughout
 * the composition tree.
 *
 * This provides a default instance of [AppShapes] that can be overridden
 * by providing a different value through [DesignTokenTheme].
 */
val LocalShapes = staticCompositionLocalOf { AppShapes() }

/**
 * CompositionLocal provider for accessing [AppElevation] values throughout
 * the composition tree.
 *
 * This provides a default instance of [AppElevation] that can be
 * overridden by providing a different value through [DesignTokenTheme].
 */
val LocalElevation = staticCompositionLocalOf { AppElevation() }

/**
 * CompositionLocal provider for accessing [AppSizes] values throughout the
 * composition tree.
 *
 * This provides a default instance of [AppSizes] that can be overridden by
 * providing a different value through [DesignTokenTheme].
 */
val LocalSizes = staticCompositionLocalOf { AppSizes() }

val LocalStrokes = staticCompositionLocalOf { AppStrokes() }

/**
 * Theme provider composable that establishes the design token context for
 * the entire composition tree.
 *
 * This composable should be placed at the root of your application's
 * composition hierarchy to provide consistent access to design tokens
 * throughout all child composables. It uses CompositionLocal to
 * efficiently propagate design token values without explicit parameter
 * passing.
 *
 * Usage example:
 * ```
 * @Composable
 * fun MyApp() {
 *     DesignTokenTheme(
 *         spacing = AppSpacing(medium = 20.dp), // Custom spacing
 *         shapes = AppShapes(card = RoundedCornerShape(16.dp)) // Custom shapes
 *     ) {
 *         // Your app content here
 *         MainScreen()
 *     }
 * }
 * ```
 *
 * @param spacing Custom spacing configuration, defaults to [AppSpacing]
 *    with standard values
 * @param padding Custom padding configuration, defaults to [AppPadding]
 *    with standard values
 * @param shapes Custom shape configuration, defaults to [AppShapes] with
 *    standard values
 * @param elevation Custom elevation configuration, defaults to
 *    [AppElevation] with standard values
 * @param sizes Custom size configuration, defaults to [AppSizes] with
 *    standard values
 * @param content The composable content that will have access to these
 *    design tokens
 */
@Composable
internal fun DesignTokenTheme(
    spacing: AppSpacing = AppSpacing(),
    padding: AppPadding = AppPadding(),
    shapes: AppShapes = AppShapes(),
    elevation: AppElevation = AppElevation(),
    sizes: AppSizes = AppSizes(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSpacing provides spacing,
        LocalPadding provides padding,
        LocalShapes provides shapes,
        LocalElevation provides elevation,
        LocalSizes provides sizes,
        content = content,
    )
}

// Usage Examples
@Composable
fun ExampleUsage(
    modifier: Modifier = Modifier,
) {
    // Using the convenience object
    Card(
        modifier = modifier.padding(DesignToken.padding.medium),
        shape = DesignToken.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = DesignToken.elevation.elevation,
        ),
    ) {
        Column(
            modifier = Modifier.padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
        ) {
            Text(
                text = "Example Card",
                style = MaterialTheme.typography.headlineSmall,
            )

            Button(
                onClick = { },
                modifier = Modifier.height(DesignToken.sizes.buttonHeight),
                shape = DesignToken.shapes.small,
            ) {
                Text("Action Button")
            }
        }
    }
}

@Preview
@Composable
private fun Example_preview() {
    MifosMobileTheme {
        ExampleUsage()
    }
}
