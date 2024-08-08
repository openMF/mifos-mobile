package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.ui.theme.Blue700

@Composable
fun MifosLinkText(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isUnderlined: Boolean = true
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = if(isUnderlined) TextDecoration.Underline else null
        ),
        modifier = modifier
            .padding(vertical = 2.dp)
            .clickable {
                onClick()
            },
    )
}