package org.mifos.mobile.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.CopyrightItemContent
import org.mifos.mobile.core.ui.component.MifosItemCard
import org.mifos.mobile.core.ui.component.MifosItemContent
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.PrivacyPolicyActivity
import org.mifos.mobile.utils.Constants.LICENSE_LINK
import org.mifos.mobile.utils.Constants.SOURCE_CODE_LINK
import org.mifos.mobile.utils.Constants.WEBSITE_LINK
import org.mifos.mobile.utils.StartActivity
import java.util.*

@SuppressWarnings("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutUsScreen(viewModel: AboutUsViewModel = viewModel()) {
    val context = StartActivity(LocalContext.current)

    MifosMobileTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp, 56.dp, 16.dp, 16.dp)
            ) {
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
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.about_app_description),
                    style = MaterialTheme.typography.titleSmall.copy(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(viewModel.aboutUsList) { item ->
                        MifosItemCard(onClick = {
                            when (item.name) {
                                R.string.official_website -> {
                                    context.startActivity(WEBSITE_LINK)
                                }
                                R.string.licenses -> {
                                    context.startActivity(LICENSE_LINK)
                                }
                                R.string.privacy_policy -> {
                                    context.startActivity(PrivacyPolicyActivity::class.java)
                                }
                                R.string.sources -> {
                                    context.startActivity(SOURCE_CODE_LINK)
                                }
                                R.string.license_string_with_value -> {
                                    context.startActivity(OssLicensesMenuActivity::class.java)
                                }
                            }
                        }) {
                            AboutUsItemCard(
                                name = item.name,
                                iconUrl = item.iconUrl
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AboutUsItemCard(
    name: Int,
    iconUrl: Int? = null,
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val copyrightText =
        LocalContext.current.getString(R.string.copy_right_mifos)
            .replace("%1\$s", currentYear.toString())

    if (name != R.string.license_string_with_value) {
        MifosItemContent(name = name, iconUrl = iconUrl)
    } else {
        CopyrightItemContent(name = name, copyrightText = copyrightText)
    }
}

@Composable
@Preview
fun AboutScreenPreview() {
    AboutUsScreen()
}