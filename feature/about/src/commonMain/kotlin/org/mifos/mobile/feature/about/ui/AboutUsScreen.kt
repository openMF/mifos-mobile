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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.about.generated.resources.Res
import mifos_mobile.feature.about.generated.resources.feature_about_app_version
import mifos_mobile.feature.about.generated.resources.feature_about_copyright_mifos
import mifos_mobile.feature.about.generated.resources.feature_about_law_icon
import mifos_mobile.feature.about.generated.resources.feature_about_license
import mifos_mobile.feature.about.generated.resources.feature_about_licenses
import mifos_mobile.feature.about.generated.resources.feature_about_official_website
import mifos_mobile.feature.about.generated.resources.feature_about_privacy_policy
import mifos_mobile.feature.about.generated.resources.feature_about_source_code
import mifos_mobile.feature.about.generated.resources.feature_about_sources
import mifos_mobile.feature.about.generated.resources.feature_about_website
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.AboutUsListItemId
import org.mifos.mobile.core.ui.component.AboutUsItemCard
import org.mifos.mobile.core.ui.component.MifosItemCard
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.feature.about.AboutUsItem

@Composable
internal fun AboutUsScreen(
    navigateToItem: (AboutUsItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentYear = remember { DateHelper.currentDate.year }
    val aboutUsItems = remember { getAboutUsItems() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))
            AboutUsHeader()
        }

        items(items = aboutUsItems, key = { item -> item.itemId }) { item ->
            MifosItemCard(
                modifier = Modifier.padding(bottom = 8.dp),
                onClick = { navigateToItem(item) },
            ) {
                AboutUsItemCard(
                    title = if (item.itemId == AboutUsListItemId.LICENSES_STRING_WITH_VALUE) {
                        stringResource(item.title).replace("%1\$s", currentYear.toString())
                    } else {
                        stringResource(item.title)
                    },
                    subtitle = item.subtitle,
                    iconUrl = item.iconUrl,
                )
            }
        }
    }
}

private fun getAboutUsItems(): List<AboutUsItem> {
    return listOf(
        AboutUsItem(
            title = Res.string.feature_about_app_version,
            itemId = AboutUsListItemId.APP_VERSION_TEXT,
        ),
        AboutUsItem(
            title = Res.string.feature_about_official_website,
            iconUrl = Res.drawable.feature_about_website,
            itemId = AboutUsListItemId.OFFICE_WEBSITE,
        ),
        AboutUsItem(
            title = Res.string.feature_about_licenses,
            iconUrl = Res.drawable.feature_about_law_icon,
            itemId = AboutUsListItemId.LICENSES,
        ),
        AboutUsItem(
            title = Res.string.feature_about_privacy_policy,
            iconUrl = Res.drawable.feature_about_privacy_policy,
            itemId = AboutUsListItemId.PRIVACY_POLICY,
        ),
        AboutUsItem(
            title = Res.string.feature_about_sources,
            iconUrl = Res.drawable.feature_about_source_code,
            itemId = AboutUsListItemId.SOURCE_CODE,
        ),
        AboutUsItem(
            title = Res.string.feature_about_copyright_mifos,
            subtitle = Res.string.feature_about_license,
            itemId = AboutUsListItemId.LICENSES_STRING_WITH_VALUE,
        ),
    )
}

@DevicePreview
@Composable
fun AboutScreenPreview() {
    MifosMobileTheme {
        AboutUsScreen(
            navigateToItem = {},
        )
    }
}
