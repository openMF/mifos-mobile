package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun MifosErrorComponent(
    isNetworkConnected: Boolean = true,
    message: String? = null,
    isEmptyData: Boolean = false,
    isRetryEnabled: Boolean = false,
    onRetry: () -> Unit = {}
) {
    when {
        !isNetworkConnected -> NoInternetComponent(isRetryEnabled = isRetryEnabled) { onRetry() }
        else -> EmptyDataComponent(
            isEmptyData = isEmptyData,
            message = message,
            isRetryEnabled = isRetryEnabled,
            onRetry = onRetry
        )
    }
}

@Composable
fun NoInternetComponent(
    modifier: Modifier = Modifier.fillMaxSize(),
    isRetryEnabled: Boolean = false,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            imageVector = Icons.Filled.WifiOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            text = stringResource(id = R.string.no_internet),
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isRetryEnabled) {
            FilledTonalButton(onClick = { onRetry.invoke() }) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
fun EmptyDataComponent(
    modifier: Modifier = Modifier.fillMaxSize(),
    isEmptyData: Boolean = false,
    message: String? = null,
    isRetryEnabled: Boolean = false,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            imageVector = Icons.Filled.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = message ?: if (isEmptyData) stringResource(id = R.string.no_data) else stringResource(id = R.string.something_went_wrong),
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )

        if (isRetryEnabled) {
            FilledTonalButton(
                modifier = Modifier.padding(top = 8.dp),
                onClick = { onRetry.invoke() }
            ) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun NoInternetPreview() {
    MifosMobileTheme {
        NoInternetComponent()
    }
}

@Preview(showSystemUi = true)
@Composable
fun EmptyDataPreview() {
    MifosMobileTheme {
        EmptyDataComponent()
    }
}