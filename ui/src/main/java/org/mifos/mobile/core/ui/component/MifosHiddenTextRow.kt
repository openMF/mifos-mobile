package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MifosHiddenTextRow(
    modifier: Modifier = Modifier,
    title: String,
    hiddenText: String,
    hiddenColor: Color,
    hidingText: String,
    visibilityIconId: Int,
    visibilityOffIconId: Int,
    onClick: () -> Unit
) {
    var isHidden by remember { mutableStateOf(true) }
    Row(modifier.clickable { onClick.invoke() }, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .alpha(0.7f)
                .weight(1f)
        )
        Text(
            text = if (isHidden) hidingText
            else hiddenText,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = hiddenColor
        )
        IconButton(
            onClick = { isHidden = !isHidden },
            modifier = Modifier
                .padding(start = 6.dp)
                .size(24.dp)
        ) {
            Icon(
                painter = if (isHidden) painterResource(id = visibilityIconId)
                else painterResource(id = visibilityOffIconId),
                contentDescription = "Show or hide total amount",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}