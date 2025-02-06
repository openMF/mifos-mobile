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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.about.generated.resources.Res
import mifos_mobile.feature.about.generated.resources.feature_about_policy_url
import mifos_mobile.feature.about.generated.resources.feature_about_privacy_policy
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.feature.about.MyWebView
import org.mifos.mobile.feature.about.openUrl

@Composable
internal fun PrivacyPolicyScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.feature_about_privacy_policy),
        backPress = navigateBack,
        modifier = modifier,
        content = {
            WebView(
                url = stringResource(Res.string.feature_about_policy_url),
            )
        },
    )
}

// @SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebView(
    url: String,
    modifier: Modifier = Modifier,
) {
    var isLoading by remember { mutableStateOf(true) }

    Column(modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        MyWebView(
            htmlContent = url,
            isLoading = { isLoading = it },
            modifier = Modifier.fillMaxWidth(),
            onUrlClicked = { url -> openUrl(url) },
        )
        if (isLoading) {
            MifosProgressIndicator()
        }
    }
}
