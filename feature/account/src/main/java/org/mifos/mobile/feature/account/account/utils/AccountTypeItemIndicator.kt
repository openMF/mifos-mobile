package org.mifos.mobile.feature.account.account.utils

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
fun AccountTypeItemIndicator(color: Color) {
    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        Canvas(
            modifier = Modifier
                .height(60.dp)
                .width(5.dp)
        ) {
            val radius = 10.dp.toPx()
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width - radius, 0f)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        offset = Offset(size.width - radius, 0f),
                        size = Size(radius, radius)
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(size.width, size.height - radius)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        offset = Offset(size.width - radius, size.height - radius),
                        size = Size(radius, radius)
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = path,
                color = color
            )
        }
    }
}