/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_logo_content_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_mifos
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_point_1
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_point_2
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_point_3
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_what_does_mifos_do
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_who_are_we
import mifos_mobile.feature.settings.generated.resources.feature_settings_about_who_are_we_desc
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_about_us
import mifos_mobile.feature.settings.generated.resources.ic_icon_money_transfer
import mifos_mobile.feature.settings.generated.resources.mifos_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

/**
 * The main entry point for the "About Us" screen. This composable function serves as a wrapper
 * around the actual content, allowing for separation of concerns and easier testing.
 *
 * @param modifier The [Modifier] to be applied to the screen.
 * @param onBackClick A lambda function to be invoked when the back button is pressed,
 *   triggering navigation to the previous screen.
 */
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    AboutScreenContent(
        modifier = modifier,
        onBackClick = onBackClick,
    )
}

/**
 * Renders the UI content for the "About Us" screen. This includes the screen's scaffold,
 * top bar, and the informational content about the Mifos Initiative.
 *
 * @param modifier The [Modifier] to be applied to the content layout.
 * @param onBackClick A lambda function to handle the back navigation event from the scaffold.
 */
@Composable
internal fun AboutScreenContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    MifosElevatedScaffold(
        onNavigateBack = onBackClick,
        topBarTitle = stringResource(Res.string.feature_settings_action_about_us),
        modifier = modifier,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    DesignToken.padding.largeIncreased,
                ).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MifosCard(
                Modifier
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.lightBlueBackground,
                ),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(DesignToken.padding.extraLargeIncreased),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
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
                        text = stringResource(Res.string.feature_settings_about_who_are_we),
                        style = MifosTypography.titleSmallEmphasized,
                        color = AppColors.customBlack,
                    )
                    Text(
                        text = stringResource(Res.string.feature_settings_about_who_are_we_desc),
                        style = MifosTypography.bodySmall,
                        color = AppColors.customBlack,
                    )

                    Text(
                        text = stringResource(Res.string.feature_settings_about_what_does_mifos_do),
                        style = MifosTypography.titleSmallEmphasized,
                        color = AppColors.customBlack,
                        modifier = Modifier.padding(top = DesignToken.padding.small),
                    )

                    Column {
                        listOf(
                            stringResource(Res.string.feature_settings_about_point_1),
                            stringResource(Res.string.feature_settings_about_point_2),
                            stringResource(Res.string.feature_settings_about_point_3),
                        ).forEach { point ->
                            Text(
                                text = "• $point",
                                style = MifosTypography.bodySmall,
                                color = AppColors.customBlack,
                                modifier = Modifier.padding(vertical = 2.dp),
                            )
                        }
                    }
                }
                Image(
                    painter =
                    painterResource(Res.drawable.ic_icon_money_transfer),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

/**
 * A Jetpack Compose preview function for the [AboutScreenContent]. This allows for
 * visualizing the UI component in Android Studio's preview pane without needing to
 * run the entire application.
 */
@Preview
@Composable
internal fun AboutScreenContentPreview() {
    MifosMobileTheme {
        AboutScreenContent(onBackClick = {})
    }
}
