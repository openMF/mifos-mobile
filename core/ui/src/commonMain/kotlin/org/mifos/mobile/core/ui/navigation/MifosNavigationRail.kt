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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.DesignToken

@Composable
fun MifosNavigationRail(
    navigationItems: List<NavigationItem>,
    selectedItem: NavigationItem?,
    onClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = NavigationRailDefaults.windowInsets,
) {
    Surface(
        color = Color.White,
        contentColor = Color.Unspecified,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(insets = windowInsets)
                .widthIn(min = 80.dp)
                .padding(vertical = DesignToken.padding.extraSmall)
                .selectableGroup()
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = DesignToken.spacing.large,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            navigationItems.forEach { navigationItem ->
                MifosNavigationRailItem(
                    contentDescriptionRes = navigationItem.contentDescriptionRes,
                    selectedIconRes = navigationItem.iconResSelected,
                    unselectedIconRes = navigationItem.iconRes,
                    isSelected = navigationItem == selectedItem,
                    label = navigationItem.labelRes,
                    onClick = { onClick(navigationItem) },
                    modifier = Modifier.testTag(tag = navigationItem.testTag),
                )
            }
        }
    }
}
