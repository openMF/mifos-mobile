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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_logo_content_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_mifos
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_all_rights_reserved
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_app_name
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_logo_content_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_mifos_electonic_banking
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_topbar_title
import mifos_mobile.feature.settings.generated.resources.feature_settings_appinfo_version
import mifos_mobile.feature.settings.generated.resources.mifo_app_info_icon
import mifos_mobile.feature.settings.generated.resources.mifos_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.feature.settings.util.appVersion

/**
 * The main composable for the App Info screen, which acts as a stateful wrapper
 * around the core UI content.
 *
 * @param onBackClick Lambda to handle back navigation events.
 * @param navigateToPrivacyPolicy Lambda to navigate to the Privacy Policy screen.
 * @param modifier The [Modifier] to be applied to this screen.
 * @param navigateToTermsAndConditions Lambda to navigate to the Terms and Conditions screen.
 */
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

/**
 * Renders the stateless UI content for the App Info screen. This includes details
 * about the app, version, and links to legal documents.
 *
 * @param onBackClick Lambda to handle back navigation from the top bar.
 * @param navigateToPrivacyPolicy Lambda to navigate to the Privacy Policy screen.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param navigateToTermsAndConditions Lambda to navigate to the Terms and Conditions screen.
 */
@Suppress("UnusedParameter")
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
        modifier = modifier,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal = DesignToken.padding.largeIncreased,
                )
                .padding(top = DesignToken.padding.largeIncreased)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            MifosCard(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.lightBlueBackground,
                ),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DesignToken.padding.extraLarge)
                            .padding(top = DesignToken.padding.extraLarge),
                        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(DesignToken.padding.small),
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.mifos_icon),
                                contentDescription =
                                stringResource(Res.string.feature_settings_about_logo_content_description),
                                modifier = Modifier
                                    .size(DesignToken.sizes.iconExtraLarge),
                            )
                            Text(
                                text = stringResource(Res.string.feature_settings_about_mifos),
                                color = MaterialTheme.colorScheme.primary,
                                style = MifosTypography.titleLarge,
                            )
                        }

                        Text(
                            text = stringResource(Res.string.feature_settings_appinfo_mifos_electonic_banking),
                            style = MifosTypography.titleMediumEmphasized,
                            color = AppColors.customBlack,
                        )
                        Text(
                            text = stringResource(Res.string.feature_settings_appinfo_all_rights_reserved),
                            style = MifosTypography.bodySmall,
                            color = AppColors.customBlack,
                        )

                        Text(
                            text = stringResource(Res.string.feature_settings_appinfo_app_name),
                            style = MifosTypography.titleMediumEmphasized,
                            modifier = Modifier.padding(top = DesignToken.padding.small),
                            color = AppColors.customBlack,
                        )

                        Column {
                            listOf(
                                stringResource(Res.string.feature_settings_appinfo_all_rights_reserved),
                                stringResource(Res.string.feature_settings_appinfo_version) + " " + appVersion(),
                            ).forEach { point ->
                                Text(
                                    text = point,
                                    style = MifosTypography.bodySmall,
                                    modifier = Modifier.padding(
                                        vertical = DesignToken.padding.small,
                                    ),
                                    color = AppColors.customBlack,
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.mifo_app_info_icon),
                            contentDescription =
                            stringResource(Res.string.feature_settings_appinfo_logo_content_description),
                            modifier = Modifier
                                .size(150.dp)
                                .align(Alignment.BottomEnd).zIndex(0f),
                        )
                    }
                }
            }

            // TODO: commenting because we don't have privacy policy and terms and conditions screens
            // Uncomment once those screens are done and add navigaton accordingly
//            Column(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(top = DesignToken.padding.medium),
//                verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
//            ) {
//                MifosOutlinedButton(
//                    content = {
//                        Text(stringResource(Res.string.feature_settings_appinfo_privacy_policy))
//                    },
//                    onClick = navigateToPrivacyPolicy,
//                    modifier=Modifier.fillMaxWidth()
//                )
//                MifosOutlinedButton(
//                    content = {
//                        Text(stringResource(Res.string.feature_settings_appinfo_terms_conditions))
//                    },
//                    onClick = navigateToTermsAndConditions,
//                    modifier=Modifier.fillMaxWidth()
//                )
//            }
        }
    }
}
