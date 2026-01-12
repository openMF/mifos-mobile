/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.core

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the visual variants available for the KPT top app bar.
 *
 * Each variant corresponds to a different Material3 top app bar style with
 * different visual characteristics and use cases.
 *
 * @see KptTopAppBarConfiguration
 */
sealed interface TopAppBarVariant : ComponentVariant {
    override val name: String

    /**
     * Standard compact top app bar suitable for most screens.
     * Height: 64dp
     * Best for: Regular screens with standard content
     */
    data object Small : TopAppBarVariant {
        override val name: String = "small"
    }

    /**
     * Center-aligned top app bar with title centered horizontally.
     * Height: 64dp
     * Best for: Modal screens, settings, or when centering is preferred
     */
    data object CenterAligned : TopAppBarVariant {
        override val name: String = "center_aligned"
    }

    /**
     * Medium height top app bar that provides more visual prominence.
     * Height: 112dp
     * Best for: Secondary screens, categories, or when more emphasis is needed
     */
    data object Medium : TopAppBarVariant {
        override val name: String = "medium"
    }

    /**
     * Large height top app bar for maximum visual impact.
     * Height: 152dp
     * Best for: Main screens, landing pages, or hero sections
     */
    data object Large : TopAppBarVariant {
        override val name: String = "large"
    }
}

/**
 * Configuration class that defines all properties for a KPT top app bar.
 *
 * This immutable data class encapsulates all the customization options for the top app bar,
 * providing a clean API for complex configurations. It supports all Material3 top app bar
 * features while adding KPT-specific enhancements.
 *
 * Example usage:
 * ```
 * val config = KptTopAppBarConfiguration(
 *     title = "My Screen",
 *     variant = TopAppBarVariant.Large,
 *     navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
 *     onNavigationIonClick = { navController.navigateUp() },
 *     actions = listOf(
 *         TopAppBarAction(
 *             icon = Icons.Default.Search,
 *             contentDescription = "Search",
 *             onClick = { openSearch() }
 *         )
 *     ),
 *     subtitle = "Optional subtitle"
 * )
 * ```
 *
 * @param title The primary title text displayed in the app bar
 * @param modifier Modifier to be applied to the app bar
 * @param variant The visual variant of the app bar (Small, CenterAligned, Medium, Large)
 * @param navigationIcon Optional icon for navigation (typically back arrow)
 * @param onNavigationIonClick Optional click handler for the navigation icon
 * @param actions List of action buttons to display on the right side
 * @param subtitle Optional secondary text displayed below the title
 * @param colors Optional custom colors for the app bar
 * @param scrollBehavior Optional scroll behavior for collapsing/expanding
 * @param windowInsets Optional window insets for proper spacing
 * @param testTag Optional test tag for UI testing
 * @param contentDescription Optional content description for accessibility
 *
 * @see TopAppBarVariant
 * @see TopAppBarAction
 */
@OptIn(ExperimentalMaterial3Api::class)
@Immutable
data class KptTopAppBarConfiguration(
    val title: String,
    val modifier: Modifier = Modifier,
    val variant: TopAppBarVariant = TopAppBarVariant.Small,
    val navigationIcon: ImageVector? = null,
    val onNavigationIonClick: (() -> Unit)? = null,
    val actions: List<TopAppBarAction> = emptyList(),
    val subtitle: String? = null,
    val colors: TopAppBarColors? = null,
    val scrollBehavior: TopAppBarScrollBehavior? = null,
    val windowInsets: WindowInsets? = null,
    val testTag: String? = null,
    val contentDescription: String? = null,
)

/**
 * Represents an action button in the top app bar.
 *
 * Action buttons are displayed on the right side of the top app bar and provide
 * quick access to common functions like search, menu, or other contextual actions.
 *
 * @param icon The vector icon to display for this action
 * @param contentDescription Accessibility description for screen readers
 * @param onClick Callback invoked when the action is clicked
 * @param enabled Whether the action button is enabled and clickable
 *
 * @see KptTopAppBarConfiguration
 */
data class TopAppBarAction(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
)

@DslMarker
annotation class TopAppBarDsl

/**
 * DSL builder class for creating [KptTopAppBarConfiguration] instances.
 *
 * This builder provides a fluent API for configuring top app bars with a clean,
 * readable syntax. All properties have sensible defaults and can be customized
 * as needed.
 *
 * Example usage:
 * ```
 * val config = kptTopAppBar {
 *     title = "Settings"
 *     variant = TopAppBarVariant.Large
 *     navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
 *     onNavigationClick = { navController.navigateUp() }
 *
 *     action(Icons.Default.Search, "Search") { openSearch() }
 *     action(Icons.Default.MoreVert, "More options") { showMenu() }
 *
 *     subtitle = "Customize your experience"
 *     testTag = "SettingsTopAppBar"
 * }
 * ```
 *
 * @see kptTopAppBar
 * @see KptTopAppBarConfiguration
 */
@TopAppBarDsl
class KptTopAppBarBuilder {
    /** The primary title text for the app bar */
    var title: String = ""

    /** Modifier to apply to the app bar */
    var modifier: Modifier = Modifier

    /** Visual variant of the app bar */
    var variant: TopAppBarVariant = TopAppBarVariant.Small

    /** Optional navigation icon (typically back arrow) */
    var navigationIcon: ImageVector? = null

    /** Click handler for the navigation icon */
    var onNavigationClick: (() -> Unit)? = null

    /** Optional secondary text below the title */
    var subtitle: String? = null

    /** Custom colors for the app bar */
    @OptIn(ExperimentalMaterial3Api::class)
    var colors: TopAppBarColors? = null

    /** Scroll behavior for collapsing/expanding */
    @OptIn(ExperimentalMaterial3Api::class)
    var scrollBehavior: TopAppBarScrollBehavior? = null

    /** Window insets for proper spacing */
    @OptIn(ExperimentalMaterial3Api::class)
    var windowInsets: WindowInsets? = null

    /** Test tag for UI testing */
    var testTag: String? = null

    /** Content description for accessibility */
    var contentDescription: String? = null

    private val actionsList = mutableListOf<TopAppBarAction>()

    /**
     * Adds an action button to the app bar.
     *
     * Action buttons are displayed on the right side of the app bar in the order
     * they are added. Keep the number of actions minimal for better UX.
     *
     * @param icon The vector icon to display
     * @param contentDescription Accessibility description
     * @param enabled Whether the action is enabled
     * @param onClick Callback when the action is clicked
     */
    fun action(
        icon: ImageVector,
        contentDescription: String,
        enabled: Boolean = true,
        onClick: () -> Unit,
    ) {
        actionsList.add(TopAppBarAction(icon, contentDescription, onClick, enabled))
    }

    /**
     * Builds the final [KptTopAppBarConfiguration] with all specified properties.
     *
     * @return A configured [KptTopAppBarConfiguration] instance
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun build(): KptTopAppBarConfiguration = KptTopAppBarConfiguration(
        title = title,
        modifier = modifier,
        variant = variant,
        navigationIcon = navigationIcon,
        onNavigationIonClick = onNavigationClick,
        actions = actionsList.toList(),
        subtitle = subtitle,
        colors = colors,
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
        testTag = testTag,
        contentDescription = contentDescription,
    )
}

/**
 * DSL function for creating a [KptTopAppBarConfiguration] using a builder pattern.
 *
 * This function provides a convenient way to configure top app bars with a clean,
 * type-safe DSL syntax. All configuration is done within the builder block.
 *
 * Example usage:
 * ```
 * val topAppBarConfig = kptTopAppBar {
 *     title = "My Screen"
 *     variant = TopAppBarVariant.Medium
 *     navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
 *     onNavigationClick = { navController.navigateUp() }
 *
 *     action(Icons.Default.Search, "Search") {
 *         // Handle search
 *     }
 *
 *     action(Icons.Default.Favorite, "Add to favorites") {
 *         // Handle favorite
 *     }
 * }
 *
 * KptTopAppBar(topAppBarConfig)
 * ```
 *
 * @param block Configuration block for building the top app bar
 * @return A fully configured [KptTopAppBarConfiguration]
 *
 * @see KptTopAppBarBuilder
 * @see KptTopAppBarConfiguration
 */
fun kptTopAppBar(block: KptTopAppBarBuilder.() -> Unit): KptTopAppBarConfiguration {
    return KptTopAppBarBuilder().apply(block).build()
}
