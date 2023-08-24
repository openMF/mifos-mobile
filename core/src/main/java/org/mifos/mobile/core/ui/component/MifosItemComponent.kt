package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MifosItemContent(name: Int, iconUrl: Int?) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        iconUrl?.let { painterResource(id = it) }?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
            )
        }
        Text(
            text = stringResource(id = name),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
        )
    }
}