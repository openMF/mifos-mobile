package org.mifos.mobile.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.home.R


@Composable
fun TransferDialog(
    onDismissRequest: () -> Unit,
    navigateToTransfer: () -> Unit,
    navigateToThirdPartyTransfer: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.transfer),
                    modifier = Modifier.clickable {
                        navigateToTransfer()
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.third_party_transfer),
                    modifier = Modifier.clickable {
                        navigateToThirdPartyTransfer()
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TransferDialogPreview() {
    MifosMobileTheme {
        TransferDialog(
            onDismissRequest = {},
            navigateToTransfer = {},
            navigateToThirdPartyTransfer = {}
        )
    }
}