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

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun ColumnScope.MifosNavigationRailItem(
    contentDescriptionRes: StringResource,
    selectedIconRes: ImageVector,
    label: StringResource,
    unselectedIconRes: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRailItem(
        icon = {
            Icon(
                imageVector = if (isSelected) selectedIconRes else unselectedIconRes,
                contentDescription = stringResource(contentDescriptionRes),
            )
        },
        label = {
            Text(
                modifier = Modifier.padding(DesignToken.padding.extraSmall),
                text = stringResource(label),
                style = MifosTypography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        selected = isSelected,
        alwaysShowLabel = true,
        onClick = onClick,
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
        ),

        modifier = modifier,
    )
}
