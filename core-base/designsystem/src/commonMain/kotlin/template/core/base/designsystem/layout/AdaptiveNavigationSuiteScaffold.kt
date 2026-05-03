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

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag

/**
 * A responsive scaffold that adapts the navigation UI (drawer, rail, or bottom bar)
 * based on the current window size and device posture.
 *
 * This composable wraps [NavigationSuiteScaffold] and automatically determines the most suitable
 * navigation layout using [WindowSizeClass] and [WindowAdaptiveInfo]. It is ideal for
 * creating adaptive applications that behave consistently across phones, tablets, and foldables.
 *
 * @param navigationSuiteItems A lambda used to define navigation destinations via [NavigationSuiteScope].
 *
 * @param modifier Modifier to be applied to the scaffold. *(Default: [Modifier])*
 *
 * @param layoutType Optional override for the navigation layout type.
 * If not provided, the layout is inferred automatically. *(Default: null)*
 *
 * @param navigationSuiteColors The color configuration for navigation components,
 * such as rail or drawer. *(Default: [NavigationSuiteDefaults.colors()])*
 *
 * @param containerColor The background color of the scaffold container.
 *(Default: [NavigationSuiteScaffoldDefaults.containerColor])*
 *
 * @param contentColor The color applied to content within the scaffold.
 *(Default: [NavigationSuiteScaffoldDefaults.contentColor])*
 *
 * @param testTag Optional testTag for the root NavigationSuiteScaffold.
 *
 * @param content The main content of the screen displayed beside or below the navigation UI.
 */

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveNavigationSuiteScaffold(
    navigationSuiteItems: NavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    layoutType: NavigationSuiteType? = null,
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = NavigationSuiteScaffoldDefaults.containerColor,
    contentColor: Color = NavigationSuiteScaffoldDefaults.contentColor,
    testTag: String? = null,
    content: @Composable () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = calculateWindowSizeClass()

    val customNavSuiteType =
        layoutType ?: if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
            NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }

    NavigationSuiteScaffold(
        modifier = modifier.then(Modifier.testTag(testTag ?: "KptAdaptiveNavigationSuiteScaffold")),
        layoutType = customNavSuiteType,
        navigationSuiteColors = navigationSuiteColors,
        containerColor = containerColor,
        contentColor = contentColor,
        navigationSuiteItems = navigationSuiteItems,
        content = content,
    )
}
