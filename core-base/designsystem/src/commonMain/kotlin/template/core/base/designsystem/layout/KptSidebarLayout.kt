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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import template.core.base.designsystem.theme.KptTheme

@Composable
fun KptSidebarLayout(
    sidebarContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    configuration: SidebarConfiguration = SidebarConfiguration(),
    sidebarVisible: Boolean = true,
    onSidebarVisibilityChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .testTag("KptSidebarLayout"),
    ) {
        if (configuration.position == SidebarPosition.Start && sidebarVisible) {
            Surface(
                modifier = Modifier.width(configuration.width),
                color = configuration.backgroundColor ?: KptTheme.colorScheme.surface,
                content = sidebarContent,
            )

            if (!configuration.overlay) {
                VerticalDivider(
                    color = configuration.dividerColor ?: KptTheme.colorScheme.outline,
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
        ) {
            content()

            if (configuration.overlay && sidebarVisible) {
                Surface(
                    modifier = Modifier
                        .width(configuration.width)
                        .fillMaxHeight()
                        .align(
                            if (configuration.position == SidebarPosition.Start) {
                                Alignment.CenterStart
                            } else {
                                Alignment.CenterEnd
                            },
                        ),
                    color = configuration.backgroundColor ?: KptTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    content = sidebarContent,
                )
            }
        }

        if (configuration.position == SidebarPosition.End && sidebarVisible) {
            if (!configuration.overlay) {
                VerticalDivider(
                    color = configuration.dividerColor ?: KptTheme.colorScheme.outline,
                )
            }

            Surface(
                modifier = Modifier.width(configuration.width),
                color = configuration.backgroundColor ?: KptTheme.colorScheme.surface,
                content = sidebarContent,
            )
        }
    }
}

@Immutable
data class SidebarConfiguration(
    val width: Dp = 300.dp,
    val position: SidebarPosition = SidebarPosition.Start,
    val collapsible: Boolean = true,
    val overlay: Boolean = false,
    val backgroundColor: Color? = null,
    val dividerColor: Color? = null,
)

enum class SidebarPosition { Start, End }
