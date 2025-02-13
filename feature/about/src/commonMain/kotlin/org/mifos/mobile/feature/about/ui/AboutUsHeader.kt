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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.about.generated.resources.Res
import mifos_mobile.feature.about.generated.resources.feature_about_app_name
import mifos_mobile.feature.about.generated.resources.feature_about_description
import mifos_mobile.feature.about.generated.resources.mifos_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.ui.utils.DevicePreview

@DevicePreview
@Composable
internal fun AboutUsHeader(
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Image(
            painter = painterResource(resource = Res.drawable.mifos_logo),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        )

        Text(
            text = stringResource(resource = Res.string.feature_about_app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        )

        Text(
            text = stringResource(resource = Res.string.feature_about_description),
            style = MaterialTheme.typography.titleSmall.copy(),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )
    }
}
