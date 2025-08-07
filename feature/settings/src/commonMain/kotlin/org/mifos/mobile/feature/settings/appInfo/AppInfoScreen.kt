/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.appInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_all_rights_reserved
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_app_name
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_logo_content_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_privacy_policy
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_terms_conditions
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_topbar_title
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_version
import mifos_mobile.feature.settings.generated.resources.mifos_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedButton
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
internal fun AppInfoScreen(
    onBackClick: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToTermsAndConditions: () -> Unit,
) {
    AppInfoContent(
        modifier = modifier,
        onBackClick = onBackClick,
        navigateToPrivacyPolicy = navigateToPrivacyPolicy,
        navigateToTermsAndConditions = navigateToTermsAndConditions,
    )
}

@Composable
internal fun AppInfoContent(
    onBackClick: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToTermsAndConditions: () -> Unit,
) {
    MifosElevatedScaffold(
        onNavigateBack = onBackClick,
        topBarTitle = stringResource(Res.string.feature_settings_appinfo_topbar_title),
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    horizontal = DesignToken.padding.large,
                    vertical = DesignToken.padding.small,
                ),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            MifosCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.lightBlueBackground,
                ),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(DesignToken.padding.large),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
                ) {
                    Image(
                        painter = painterResource(Res.drawable.mifos_icon),
                        contentDescription =
                        stringResource(Res.string.feature_settings_appinfo_logo_content_description),
                        modifier = Modifier
                            .padding(vertical = DesignToken.padding.large)
                            .fillMaxWidth(0.7f)
                            .aspectRatio(5.59f),
                    )

                    Text(
                        text = stringResource(Res.string.feature_settings_appinfo_app_name),
                        style = MifosTypography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(Res.string.feature_settings_appinfo_all_rights_reserved),
                        style = MifosTypography.bodySmall,
                    )

                    Text(
                        text = stringResource(Res.string.feature_settings_appinfo_app_name),
                        style = MifosTypography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = DesignToken.padding.small),
                    )

                    Column {
                        listOf(
                            stringResource(Res.string.feature_settings_appinfo_all_rights_reserved),
                            stringResource(Res.string.feature_settings_appinfo_version),
                        ).forEach { point ->
                            Text(
                                text = point,
                                style = MifosTypography.bodySmall,
                                modifier = Modifier.padding(vertical = DesignToken.padding.small),
                            )
                        }
                    }
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = DesignToken.padding.medium),
                verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
            ) {
                MifosOutlinedButton(
                    content = {
                        Text(stringResource(Res.string.feature_settings_appinfo_privacy_policy))
                    },
                    onClick = navigateToPrivacyPolicy,
                )
                MifosOutlinedButton(
                    content = {
                        Text(stringResource(Res.string.feature_settings_appinfo_terms_conditions))
                    },
                    onClick = navigateToTermsAndConditions,
                )
            }
        }
    }
}
