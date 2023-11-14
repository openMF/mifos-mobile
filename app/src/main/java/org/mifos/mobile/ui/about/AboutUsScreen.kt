package org.mifos.mobile.ui.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.mifos.mobile.core.ui.component.AboutUsItemCard
import org.mifos.mobile.core.ui.component.MifosItemCard
import org.mifos.mobile.ui.enums.AboutUsListItemId
import java.util.*

@SuppressWarnings("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutUsScreen(viewModel: AboutUsViewModel) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))
            AboutUsHeader()
        }
        items(viewModel.aboutUsItems) { item ->
            MifosItemCard(
                modifier = Modifier.padding(bottom = 8.dp),
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
                item.title?.let {
                    AboutUsItemCard(
                        title = it,
                        subtitle = item.subtitle,
                        iconUrl = item.iconUrl
                    )
                }
            }
        }
    }

}

@Composable
@Preview
fun AboutScreenPreview() {
    AboutUsScreen(AboutUsViewModel())
}