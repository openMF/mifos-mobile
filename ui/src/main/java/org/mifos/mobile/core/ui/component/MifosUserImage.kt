package org.mifos.mobile.core.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

/**
 * @author pratyush
 * @since 20/12/2023
 */

@Composable
fun MifosUserImage(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    username: String? = null
) {
    if (bitmap == null) {
        MifosTextUserImage(
            modifier = modifier,
            text = username?.firstOrNull()?.toString() ?: "M"
        )
    } else {
        Image(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
        )
    }
}
