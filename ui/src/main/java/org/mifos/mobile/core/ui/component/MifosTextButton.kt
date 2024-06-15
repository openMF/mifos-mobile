package org.mifos.mobile.core.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun MifosTextButton(
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