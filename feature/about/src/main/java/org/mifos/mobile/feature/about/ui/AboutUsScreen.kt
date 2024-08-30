/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.about.ui

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.AboutUsItem
import org.mifos.mobile.core.model.enums.AboutUsListItemId
import org.mifos.mobile.core.ui.component.AboutUsItemCard
import org.mifos.mobile.core.ui.component.MifosItemCard
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.about.R
import java.util.Calendar

@Composable
fun AboutUsScreen(
    navigateToItem: (AboutUsItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))
            AboutUsHeader()
        }
        items(getAboutUsItem(context)) { item ->
            MifosItemCard(
                modifier = Modifier.padding(bottom = 8.dp),
                onClick = { navigateToItem(item) },
            ) {
                item.title?.let {
                    AboutUsItemCard(
                        title = it,
                        subtitle = item.subtitle,
                        iconUrl = item.iconUrl,
                    )
                }
            }
        }
    }
}

fun getAboutUsItem(context: Context): List<AboutUsItem> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val copyrightText = context.getString(R.string.copy_right_mifos).replace("%1\$s", currentYear.toString())

    return listOf(
        AboutUsItem(
            title = context.getString(R.string.app_version_text),
            itemId = AboutUsListItemId.APP_VERSION_TEXT,
        ),
        AboutUsItem(
            title = context.getString(R.string.official_website),
            iconUrl = R.drawable.ic_website,
            itemId = AboutUsListItemId.OFFICE_WEBSITE,
        ),
        AboutUsItem(
            title = context.getString(R.string.licenses),
            iconUrl = R.drawable.ic_law_icon,
            itemId = AboutUsListItemId.LICENSES,
        ),
        AboutUsItem(
            title = context.getString(R.string.privacy_policy),
            iconUrl = R.drawable.ic_privacy_policy,
            itemId = AboutUsListItemId.PRIVACY_POLICY,
        ),
        AboutUsItem(
            title = context.getString(R.string.sources),
            iconUrl = R.drawable.ic_source_code,
            itemId = AboutUsListItemId.SOURCE_CODE,
        ),
        AboutUsItem(
            title = copyrightText,
            subtitle = R.string.license_string_with_value,
            itemId = AboutUsListItemId.LICENSES_STRING_WITH_VALUE,
        ),
    )
}

@DevicePreviews
@Composable
fun AboutScreenPreview() {
    MifosMobileTheme {
        AboutUsScreen(
            navigateToItem = {},
        )
    }
}
