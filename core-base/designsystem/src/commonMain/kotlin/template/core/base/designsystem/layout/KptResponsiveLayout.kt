/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A responsive layout composable that adapts content based on screen size breakpoints.
 *
 * This component automatically selects the appropriate layout based on the current screen width:
 * - **Compact**: < 600dp (phones in portrait, small tablets)
 * - **Medium**: 600-840dp (tablets, phones in landscape)
 * - **Expanded**: ≥ 840dp (large tablets, desktop)
 *
 * The layout will fall back to the compact layout if medium/expanded layouts are not provided
 * for the current screen size.
 *
 * Example usage:
 * ```
 * KptResponsiveLayout(
 *     compact = {
 *         // Single column layout for phones
 *         LazyColumn {
 *             items(data) { item -> ItemCard(item) }
 *         }
 *     },
 *     medium = {
 *         // Two column grid for tablets
 *         LazyVerticalGrid(columns = GridCells.Fixed(2)) {
 *             items(data) { item -> ItemCard(item) }
 *         }
 *     },
 *     expanded = {
 *         // Three column layout with sidebar for desktop
 *         Row {
 *             Sidebar(modifier = Modifier.width(240.dp))
 *             LazyVerticalGrid(
 *                 columns = GridCells.Fixed(3),
 *                 modifier = Modifier.weight(1f)
 *             ) {
 *                 items(data) { item -> ItemCard(item) }
 *             }
 *         }
 *     }
 * )
 * ```
 *
 * @param compact The layout to display on compact screens (< 600dp). Always required.
 * @param medium The layout to display on medium screens (600-840dp). Optional, falls back to compact.
 * @param expanded The layout to display on expanded screens (≥ 840dp). Optional, falls back to medium or compact.
 *
 * @see rememberResponsiveLayoutInfo
 * @see ResponsiveLayoutInfo
 */
@Composable
fun KptResponsiveLayout(
    compact: @Composable () -> Unit,
    medium: (@Composable () -> Unit)? = null,
    expanded: (@Composable () -> Unit)? = null,
) {
    val layoutInfo = rememberResponsiveLayoutInfo()

    when {
        layoutInfo.isExpanded && expanded != null -> expanded()
        layoutInfo.isMedium && medium != null -> medium()
        else -> compact()
    }
}

/**
 * Contains information about the current screen size and responsive breakpoints.
 *
 * This class provides both the raw screen dimensions and convenience boolean flags
 * for determining which breakpoint the current screen size falls into.
 *
 * @property screenWidthDp The current screen width in density-independent pixels
 * @property screenHeightDp The current screen height in density-independent pixels
 * @property isCompact True if screen width < 600dp
 * @property isMedium True if screen width is between 600-840dp
 * @property isExpanded True if screen width ≥ 840dp
 *
 * @see rememberResponsiveLayoutInfo
 */
@Stable
class ResponsiveLayoutInfo(
    val screenWidthDp: Dp,
    val screenHeightDp: Dp,
    val isCompact: Boolean,
    val isMedium: Boolean,
    val isExpanded: Boolean,
)

/**
 * Remembers and provides responsive layout information based on the current window size.
 *
 * This composable function observes the current window configuration and returns
 * a [ResponsiveLayoutInfo] object that contains both raw dimensions and computed
 * breakpoint flags.
 *
 * The breakpoints follow Material Design guidelines:
 * - Compact: < 600dp width
 * - Medium: 600-840dp width
 * - Expanded: ≥ 840dp width
 *
 * The returned object is automatically recomposed when the window size changes,
 * allowing responsive layouts to adapt in real-time.
 *
 * Example usage:
 * ```
 * @Composable
 * fun MyScreen() {
 *     val layoutInfo = rememberResponsiveLayoutInfo()
 *
 *     when {
 *         layoutInfo.isCompact -> CompactLayout()
 *         layoutInfo.isMedium -> MediumLayout()
 *         layoutInfo.isExpanded -> ExpandedLayout()
 *     }
 * }
 * ```
 *
 * @return A [ResponsiveLayoutInfo] object containing current screen size information
 *
 * @see ResponsiveLayoutInfo
 * @see KptResponsiveLayout
 */
@Composable
fun rememberResponsiveLayoutInfo(): ResponsiveLayoutInfo {
    val configuration = LocalWindowInfo.current
    val width = configuration.containerSize.width
    val height = configuration.containerSize.height.dp

    return remember(configuration) {
        ResponsiveLayoutInfo(
            screenWidthDp = width.dp,
            screenHeightDp = height,
            isCompact = width < 600,
            isMedium = width >= 600 && width < 840,
            isExpanded = width >= 840,
        )
    }
}
