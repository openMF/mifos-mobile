/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

/**
 * Represents a user-interactable item to navigate a user via the bottom app bar or navigation rail.
 */
interface NavigationItem {
    /**
     * The resource ID for the icon representing the tab when it is selected.
     */
    val iconResSelected: ImageVector

    /**
     * Resource id for the icon representing the tab.
     */
    val iconRes: ImageVector

    /**
     * Resource id for the label describing the tab.
     */
    val labelRes: StringResource

    /**
     * Resource id for the content description describing the tab.
     */
    val contentDescriptionRes: StringResource

    /**
     * Route of the tab's graph.
     */
    val graphRoute: String

    /**
     * Route of the tab's start destination.
     */
    val startDestinationRoute: String

    /**
     * The test tag of the tab.
     */
    val testTag: String
}
