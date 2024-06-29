package org.mifos.mobile.ui.beneficiary.presentation

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.guarantor.R


/**
 * this is a reusable composable function that is made up of a text and icon composable
 * some of the intake parameters are
 * @param[icon]
 * @param[icondescription]
 * @param[text]
 * @param[iconClick]
 *
 * */
@Composable
fun RenderIconAndText(
    @DrawableRes icon: Int,
    icondescription: String,
    text: String,
    iconClick: () -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            tint = MaterialTheme.colorScheme.onSurface,
            painter = painterResource(id = icon),
            contentDescription = icondescription,
            modifier = Modifier
                .height(85.dp)
                .width(85.dp)
                .clickable(
                    onClick = iconClick
                )
        )

        Text(
            text,
            color = MaterialTheme.colorScheme.onSurface
        )
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
fun IconsAndTextPreview() {
    MifosMobileTheme {
        RenderIconAndText(
            icon = R.drawable.ic_qrcode_scan_gray_dark,
            text = stringResource(id = R.string.scan),
            icondescription = stringResource(id = R.string.scan),
            iconClick = {}
        )
    }
}