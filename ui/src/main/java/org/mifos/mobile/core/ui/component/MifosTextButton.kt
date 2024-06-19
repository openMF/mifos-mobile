package org.mifos.mobile.core.ui.component

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun MifosTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    textResId: Int,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        content = {
            Text(text = stringResource(id = textResId))
        }
    )
}

@Composable
fun MifosOutlinedTextButton(
    onClick: () -> Unit,
    textResId: Int,
) {
    Button(
        onClick = { onClick() },
        content = {
            Text(text = stringResource(id = textResId))
        }
    )
}

@Composable
fun MifosIconTextButton(
    imageVector: ImageVector,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = { onClick.invoke() },
        enabled = enabled
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = imageVector, contentDescription = null)
            Text(text = text)
        }
    }
}