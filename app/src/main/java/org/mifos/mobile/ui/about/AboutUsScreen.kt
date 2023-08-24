package org.mifos.mobile.ui.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.PrivacyPolicyActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import org.mifos.mobile.utils.Constants.LICENSE_LINK
import org.mifos.mobile.utils.Constants.SOURCE_CODE_LINK
import org.mifos.mobile.utils.Constants.WEBSITE_LINK
import java.util.*

@SuppressWarnings("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutUsScreen(viewModel: AboutUsViewModel = viewModel()){
    val context = LocalContext.current

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

                viewModel.aboutUsList.forEach { item ->
                    AboutUsItemCard(
                        name = item.name,
                        iconUrl = item.iconUrl,
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        onClick = {
                            when (item.name) {
                                R.string.official_website -> {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW, Uri.parse(WEBSITE_LINK)
                                        )
                                    )
                                }
                                R.string.licenses -> {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW, Uri.parse(LICENSE_LINK)
                                        )
                                    )
                                }
                                R.string.privacy_policy -> {
                                    context.startActivity(
                                        Intent(
                                            context, PrivacyPolicyActivity::class.java
                                        )
                                    )
                                }
                                R.string.sources -> {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW, Uri.parse(SOURCE_CODE_LINK)
                                        )
                                    )
                                }
                                R.string.license_string_with_value -> {
                                    context.startActivity(
                                        Intent(
                                            context, OssLicensesMenuActivity::class.java
                                        )
                                    )
                                }
                            }
                        })
                }
            }
        }
    }
}

@Composable
fun AboutUsItemCard(
    name: Int,
    iconUrl: Int? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    elevation: Dp = 1.dp,
) {
    val context = LocalContext.current
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val copyrightText =
        context.getString(R.string.copy_right_mifos).replace("%1\$s", currentYear.toString())
    Card(
        shape = shape,
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
    ) {
        if (name != R.string.license_string_with_value) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                iconUrl?.let { painterResource(id = it) }?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                    )
                }
                Text(
                    text = stringResource(id = name),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                )

            }
        } else {
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
    }
}

@Composable
@Preview
fun AboutScreenPreview() {
    AboutUsScreen()
}