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
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.CopyrightItemContent
import org.mifos.mobile.core.ui.component.MifosItemCard
import org.mifos.mobile.core.ui.component.MifosItemContent
import org.mifos.mobile.ui.enums.AboutUsListItemId
import java.util.*

@SuppressWarnings("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutUsScreen(viewModel: AboutUsViewModel = viewModel()) {

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
            items(viewModel.aboutUsItems) { item ->
                MifosItemCard(
                    onClick = {
                        when (item.itemId) {
                            AboutUsListItemId.OFFICE_WEBSITE -> {
                                viewModel.navigateToItem(AboutUsListItemId.OFFICE_WEBSITE)
                            }
                            AboutUsListItemId.LICENSES -> {
                                viewModel.navigateToItem(AboutUsListItemId.LICENSES)
                            }
                            AboutUsListItemId.PRIVACY_POLICY -> {
                                viewModel.navigateToItem(AboutUsListItemId.PRIVACY_POLICY)
                            }
                            AboutUsListItemId.SOURCE_CODE -> {
                                viewModel.navigateToItem(AboutUsListItemId.SOURCE_CODE)
                            }
                            AboutUsListItemId.LICENSES_STRING_WITH_VALUE -> {
                                viewModel.navigateToItem(AboutUsListItemId.LICENSES_STRING_WITH_VALUE)
                            }
                            else -> {}
                        }
                    }
                ) {
                    AboutUsItemCard(
                        name = item.name,
                        iconUrl = item.iconUrl
                    )
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