package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CopyrightItemContent(name: Int, copyrightText: String) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = copyrightText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
        )
        Text(
            text = stringResource(id = name),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
        )
    }
}