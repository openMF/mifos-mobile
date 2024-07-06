package org.mifos.mobile.feature.about.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.feature.about.R

@Preview
@Composable
fun AboutUsHeader() {
    Column {
        Image(
            painter = painterResource(id = R.mipmap.mifos_icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.about_app_description),
            style = MaterialTheme.typography.titleSmall.copy(),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}