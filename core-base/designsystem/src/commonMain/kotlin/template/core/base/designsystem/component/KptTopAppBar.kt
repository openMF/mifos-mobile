/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
@file:OptIn(ExperimentalMaterial3Api::class)

package template.core.base.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import template.core.base.designsystem.core.KptTopAppBarConfiguration
import template.core.base.designsystem.core.TopAppBarAction
import template.core.base.designsystem.core.TopAppBarVariant
import template.core.base.designsystem.theme.KptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KptTopAppBar(configuration: KptTopAppBarConfiguration) {
    val finalModifier = configuration.modifier
        .testTag(configuration.testTag ?: "KptTopAppBar")
        .let { mod ->
            if (configuration.contentDescription != null) {
                mod.semantics { contentDescription = configuration.contentDescription }
            } else {
                mod
            }
        }

    val titleContent: @Composable () -> Unit = {
        Column {
            Text(
                text = configuration.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            configuration.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = KptTheme.typography.bodySmall,
                    color = KptTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

    val navigationIconContent: @Composable () -> Unit = {
        configuration.navigationIcon?.let { icon ->
            IconButton(
                onClick = configuration.onNavigationIonClick ?: {},
                enabled = configuration.onNavigationIonClick != null,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Navigation",
                )
            }
        }
    }

    val actionsContent: @Composable RowScope.() -> Unit = {
        configuration.actions.forEach { action ->
            IconButton(
                onClick = action.onClick,
                enabled = action.enabled,
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.contentDescription,
                )
            }
        }
    }

    when (configuration.variant) {
        TopAppBarVariant.Small -> TopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )

        TopAppBarVariant.CenterAligned -> CenterAlignedTopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.centerAlignedTopAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )

        TopAppBarVariant.Medium -> MediumTopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.mediumTopAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )

        TopAppBarVariant.Large -> LargeTopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.largeTopAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )
    }
}

@Composable
fun KptTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    variant: TopAppBarVariant = TopAppBarVariant.Small,
) {
    KptTopAppBar(
        KptTopAppBarConfiguration(
            title = title,
            modifier = modifier,
            variant = variant,
        ),
    )
}

@Composable
fun KptTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    variant: TopAppBarVariant = TopAppBarVariant.Small,
    actions: List<TopAppBarAction> = emptyList(),
) {
    KptTopAppBar(
        KptTopAppBarConfiguration(
            title = title,
            modifier = modifier,
            variant = variant,
            navigationIcon = navigationIcon,
            onNavigationIonClick = onNavigationIconClick,
            actions = actions,
        ),
    )
}

@Composable
fun KptTopAppBar(
    title: String,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    variant: TopAppBarVariant = TopAppBarVariant.Small,
    actions: List<TopAppBarAction> = emptyList(),
) {
    KptTopAppBar(
        KptTopAppBarConfiguration(
            title = title,
            modifier = modifier,
            variant = variant,
            navigationIcon = if (showNavigationIcon) navigationIcon else null,
            onNavigationIonClick = onNavigationIconClick,
            actions = actions,
        ),
    )
}

@Composable
fun KptTopAppBar(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onNavigationIconClick: (() -> Unit)? = null,
    navigationIcon: ImageVector? = if (onNavigationIconClick != null) Icons.AutoMirrored.Filled.ArrowBack else null,
    variant: TopAppBarVariant = TopAppBarVariant.Small,
) {
    KptTopAppBar(
        KptTopAppBarConfiguration(
            title = title,
            subtitle = subtitle,
            modifier = modifier,
            variant = variant,
            navigationIcon = navigationIcon,
            onNavigationIonClick = onNavigationIconClick,
        ),
    )
}

@Composable
fun KptTopAppBar(
    title: String,
    actionIcon: ImageVector,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionContentDescription: String = "Action",
    onNavigationIconClick: (() -> Unit)? = null,
    navigationIcon: ImageVector? = if (onNavigationIconClick != null) Icons.AutoMirrored.Filled.ArrowBack else null,
    variant: TopAppBarVariant = TopAppBarVariant.Small,
) {
    KptTopAppBar(
        KptTopAppBarConfiguration(
            title = title,
            modifier = modifier,
            variant = variant,
            navigationIcon = navigationIcon,
            onNavigationIonClick = onNavigationIconClick,
            actions = listOf(
                TopAppBarAction(actionIcon, actionContentDescription, onActionClick),
            ),
        ),
    )
}

@Composable
fun KptSearchAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    onSearchClick: (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(placeholder) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                } else {
                    null
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            onSearchClick?.let { onClick ->
                IconButton(onClick = onClick) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        },
        modifier = modifier.testTag("KptSearchAppBar"),
    )
}

@Composable
fun KptProfileAppBar(
    title: String,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onNavigationIconClick: (() -> Unit)? = null,
) {
    KptTopAppBar(
        KptTopAppBarConfiguration(
            title = title,
            subtitle = subtitle,
            modifier = modifier,
            navigationIcon = if (onNavigationIconClick != null) Icons.AutoMirrored.Filled.ArrowBack else null,
            onNavigationIonClick = onNavigationIconClick,
            actions = listOf(
                TopAppBarAction(
                    icon = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    onClick = onProfileClick,
                ),
            ),
        ),
    )
}

@Composable
fun KptSettingsAppBar(
    title: String = "Settings",
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: (() -> Unit)? = null,
    onMoreClick: (() -> Unit)? = null,
) {
    val actions = mutableListOf<TopAppBarAction>()

    onSearchClick?.let {
        actions.add(TopAppBarAction(Icons.Default.Search, "Search", it))
    }

    onMoreClick?.let {
        actions.add(TopAppBarAction(Icons.Default.MoreVert, "More options", it))
    }

    KptTopAppBar(
        KptTopAppBarConfiguration(
            title = title,
            modifier = modifier,
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationIonClick = onNavigationIconClick,
            actions = actions,
        ),
    )
}

@Composable
fun KptSmallTopAppBar(
    title: String,
    onNavigationIconClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    onNavigationIconClick?.let {
        KptTopAppBar(
            title = title,
            onNavigationIconClick = it,
            modifier = modifier,
            variant = TopAppBarVariant.Small,
        )
    } ?: KptTopAppBar(title, modifier, TopAppBarVariant.Small)
}

@Composable
fun KptCenterAlignedTopAppBar(
    title: String,
    onNavigationIconClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) = onNavigationIconClick?.let {
    KptTopAppBar(
        title = title,
        onNavigationIconClick = it,
        modifier = modifier,
        variant = TopAppBarVariant.CenterAligned,
    )
} ?: KptTopAppBar(title, modifier, TopAppBarVariant.CenterAligned)

@Composable
fun KptMediumTopAppBar(
    title: String,
    onNavigationIconClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) = KptTopAppBar(title, modifier, TopAppBarVariant.Medium)

@Composable
fun KptLargeTopAppBar(
    title: String,
    onNavigationIconClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) = KptTopAppBar(title, modifier, TopAppBarVariant.Large)
