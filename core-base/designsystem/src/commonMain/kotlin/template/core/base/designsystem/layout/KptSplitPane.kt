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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import template.core.base.designsystem.theme.KptTheme

@Composable
fun KptSplitPane(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    initialSplitRatio: Float = 0.5f,
    minLeftWidth: Dp = 200.dp,
    minRightWidth: Dp = 200.dp,
    resizable: Boolean = true,
    dividerColor: Color = KptTheme.colorScheme.outline,
    dividerWidth: Dp = 1.dp,
) {
    var splitRatio by remember { mutableFloatStateOf(initialSplitRatio) }

    Row(
        modifier = modifier
            .fillMaxSize()
            .testTag("KptSplitPane"),
    ) {
        Box(
            modifier = Modifier.weight(splitRatio),
        ) {
            leftContent()
        }

        VerticalDivider(
            thickness = dividerWidth,
            color = dividerColor,
        )

        Box(
            modifier = Modifier.weight(1f - splitRatio),
        ) {
            rightContent()
        }
    }
}
