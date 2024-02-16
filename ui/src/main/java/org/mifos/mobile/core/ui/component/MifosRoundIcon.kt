package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MifosRoundIcon(
    iconId: Int,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .clip(CircleShape)
    ) {
        Image(
            modifier = Modifier.padding(all = 6.dp),
            painter = painterResource(id = iconId),
            contentDescription = contentDescription
        )
    }
}