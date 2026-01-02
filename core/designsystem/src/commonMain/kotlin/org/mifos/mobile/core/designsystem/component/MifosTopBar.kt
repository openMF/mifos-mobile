/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.theme.KptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosTopBar(
    topBarTitle: String,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = topBarTitle,
                style = KptTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
            ) {
                Icon(
                    imageVector = MifosIcons.Chevron,
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = KptTheme.colorScheme.surface,
        ),
        actions = actions,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosTopBar(
    topBarTitle: String,
    showNavigationIcon: Boolean,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = topBarTitle,
                style = KptTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(
                    onClick = onNavigationIconClick,
                ) {
                    Icon(
                        imageVector = MifosIcons.Chevron,
                        contentDescription = "Back",
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = KptTheme.colorScheme.surface,
        ),
        actions = actions,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosRoundedTopAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    brandIcon: DrawableResource? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            if (brandIcon == null) {
                Text(
                    text = title,
                    style = MifosTypography.titleMedium,
                    color = KptTheme.colorScheme.onBackground,
                )
            }
        },
        actions = actions,
        navigationIcon = {
            if (brandIcon != null) {
                Box(
                    modifier = Modifier.padding(DesignToken.padding.medium),
                ) {
                    Image(
                        painter = painterResource(brandIcon),
                        contentDescription = "Brand Icon",
                        modifier = Modifier
                            .size(DesignToken.sizes.imageDp96, DesignToken.sizes.imageDp28)
                            .align(Alignment.TopStart),
                    )
                }
            } else {
                IconButton(
                    onClick = onNavigateBack,
                ) {
                    Icon(
                        imageVector = MifosIcons.Chevron,
                        contentDescription = null,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = KptTheme.colorScheme.surface,
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = DesignToken.elevation.elevation,
                shape = DesignToken.shapes.topBar,
                spotColor = KptTheme.colorScheme.onSurface,
                ambientColor = KptTheme.colorScheme.onSurface,
            )
            .clip(DesignToken.shapes.topBar)
            .background(KptTheme.colorScheme.surface),
    )
}

@Preview
@Composable
fun PreviewMbsRoundedTopAppBar() {
    MifosRoundedTopAppBar(
        title = "TopAppBar",
        onNavigateBack = {},
    )
}

@Preview
@Composable
fun MbsTopBarPreview() {
    MifosTopBar(
        topBarTitle = "Title",
        onNavigationIconClick = {},
    )
}
