package org.mifos.mobile.feature.beneficiary.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.beneficiary.R
import org.mifos.mobile.ui.beneficiary.presentation.RenderIconAndText

@Composable
fun BeneficiaryScreenIcons(
    modifier: Modifier = Modifier,
    addIconclicked: () -> Unit,
    scanIconClicked: () -> Unit,
    uploadIconClicked: () -> Unit
) {

    Column(modifier = modifier) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            RenderIconAndText(
                icon = R.drawable.ic_beneficiary_add_48px,
                text = stringResource(id = R.string.add),
                icondescription = stringResource(id = R.string.add),
                iconClick = addIconclicked
            )

            RenderIconAndText(
                icon = R.drawable.ic_qrcode_scan_gray_dark,
                text = stringResource(id = R.string.scan),
                icondescription = stringResource(id = R.string.scan),
                iconClick = scanIconClicked
            )

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            RenderIconAndText(
                icon = R.drawable.ic_file_upload_black_24dp,
                text = stringResource(id = R.string.upload_qr_code),
                icondescription = stringResource(id = R.string.upload_qr_code),
                iconClick = uploadIconClicked
            )

        }
    }
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun IconsScreenPreview() {
    MifosMobileTheme {

        BeneficiaryScreenIcons(
            modifier = Modifier.padding(top = 20.dp),
            addIconclicked = {},
            scanIconClicked = {},
            uploadIconClicked = {}
        )

    }
}