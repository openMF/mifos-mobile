/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import template.core.base.designsystem.theme.KptTheme

@Composable
fun KptShimmerLoadingBox(
    modifier: Modifier = Modifier,
    shape: Shape = KptTheme.shapes.small,
    shimmerColor: Color = KptTheme.colorScheme.surfaceVariant,
    highlightColor: Color = KptTheme.colorScheme.surface,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerTranslateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )

    Box(
        modifier = modifier
            .background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    colors = listOf(
                        shimmerColor,
                        highlightColor,
                        shimmerColor,
                    ),
                    startX = shimmerTranslateAnim - 200f,
                    endX = shimmerTranslateAnim,
                ),
                shape = shape,
            )
            .testTag("KptShimmerLoadingBox"),
    )
}

@Composable
fun KptShimmerListItem(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KptShimmerLoadingBox(
            modifier = Modifier
                .size(40.dp),
            shape = CircleShape,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            KptShimmerLoadingBox(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.7f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            KptShimmerLoadingBox(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth(0.5f),
            )
        }
    }
}
