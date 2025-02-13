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
import org.mifos.mobile.feature.about.MifosWebView

@Composable
internal fun PrivacyPolicyScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isLoading by remember { mutableStateOf(true) }

    MifosScaffold(
        topBarTitle = stringResource(Res.string.feature_about_privacy_policy),
        backPress = navigateBack,
        modifier = modifier,
        content = {
            WebView(
                url = stringResource(Res.string.feature_about_policy_url),
                isLoading = isLoading,
                onLoadingChange = { isLoading = it },
            )
        },
    )
}

@Composable
private fun WebView(
    url: String,
    isLoading: Boolean,
    onLoadingChange: (isLoading: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        MifosWebView(
            htmlContent = url,
            onLoadingChange = onLoadingChange,
            modifier = Modifier.fillMaxWidth(),
        )
        if (isLoading) {
            MifosProgressIndicator()
        }
    }
}
