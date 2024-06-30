package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MonitorListItemWithIcon(
    modifier: Modifier = Modifier,
    titleId: Int,
    subTitleId: Int,
    iconId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MifosRoundIcon(
            iconId = iconId,
            modifier = Modifier.size(39.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(id = subTitleId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .alpha(0.7f)
                    .fillMaxWidth(),
            )
        }
    }
}
