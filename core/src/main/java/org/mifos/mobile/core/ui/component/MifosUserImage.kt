package org.mifos.mobile.core.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

/**
 * @author pratyush
 * @since 20/12/2023
 */

@Composable
fun MifosUserImage(
    isDarkTheme: Boolean,
    bitmap: Bitmap
) {
    val backgroundColor = if (isDarkTheme) {
        Color(0xFF9bb1e3)
    } else {
        Color(0xFF325ca8)
    }
    Image(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Profile Image",
        contentScale = ContentScale.Crop,
    )

}