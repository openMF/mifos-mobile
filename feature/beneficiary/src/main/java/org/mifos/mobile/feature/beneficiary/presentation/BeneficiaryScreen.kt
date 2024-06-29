package org.mifos.mobile.feature.beneficiary.presentation

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.guarantor.R


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BeneficiaryScreen(
    topAppbarNavigateback: () -> Unit,
    addiconClicked: () -> Unit,
    scaniconClicked: () -> Unit,
    uploadIconClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            MifosTopBar(
                navigateBack = topAppbarNavigateback,
                title = {
                    Text(text = stringResource(id = R.string.add_beneficiary))
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(10.dp)
        ) {
            Text(
                stringResource(id = R.string.select_mode),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.add_beneficiary_option),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            BeneficiaryScreenIcons(
                modifier = Modifier.padding(top = 20.dp),
                addIconclicked = addiconClicked,
                scanIconClicked = scaniconClicked,
                uploadIconClicked = uploadIconClicked
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun BeneficiaryScreenPreview() {
    MifosMobileTheme {
        BeneficiaryScreen({}, {}, {}, {})
    }
}