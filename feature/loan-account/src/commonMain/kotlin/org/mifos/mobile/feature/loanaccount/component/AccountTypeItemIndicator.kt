/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun AccountTypeItemIndicator(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(Color.Transparent),
    ) {
        Canvas(
            modifier = Modifier
                .height(60.dp)
                .width(5.dp),
        ) {
            val radius = 10.dp.toPx()
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width - radius, 0f)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        offset = Offset(size.width - radius, 0f),
                        size = Size(radius, radius),
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false,
                )
                lineTo(size.width, size.height - radius)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        offset = Offset(size.width - radius, size.height - radius),
                        size = Size(radius, radius),
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false,
                )
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = path,
                color = color,
            )
        }
    }
}
