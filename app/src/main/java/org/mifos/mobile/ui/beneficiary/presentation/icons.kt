package org.mifos.mobile.ui.beneficiary.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme


/***
 *this is a composable that is made up of multiple icons from the res folder,
 * the composable takes in parameters such as
 * @param[addIconclicked] for taking actions when the add icon is clicked
 * @param[scanIconClicked]  for taking icons when the scan icon is clicked
 * @param[uploadIconClicked] for taking actions when the upload icon is clicked
 * @param[modifier] for modifying the composable.
 * Reference the preview function to see a preview of the screen
 *
 * */

@Composable
fun multipleIcons(
    modifier: Modifier = Modifier,
    addIconclicked:()->Unit,
    scanIconClicked:()->Unit,
    uploadIconClicked:()->Unit
) {

    Column(modifier=modifier) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            iconandText(
                icon = R.drawable.ic_beneficiary_add_48px,
                text = stringResource(id = R.string.add),
                icondescription = stringResource(id = R.string.add),
                iconClick = addIconclicked
            )

            iconandText(
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
            iconandText(
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
fun iconsscreenpreview() {
    MifosMobileTheme{
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            multipleIcons(
                modifier = Modifier.padding(top = 20.dp),
                addIconclicked = {},
                scanIconClicked = {},
                uploadIconClicked = {}
            )

        }
    }
}