/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.accountChoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_signup_account_choice_subtitle
import mifos_mobile.feature.auth.generated.resources.feature_signup_account_choice_title
import mifos_mobile.feature.auth.generated.resources.no
import mifos_mobile.feature.auth.generated.resources.yes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.feature.auth.registration.MifosLogoHeader
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun AccountChoiceScreen(
    onExistingCustomer: () -> Unit,
    onNewCustomer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        modifier = modifier,
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(KptTheme.spacing.md)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
        ) {
            MifosLogoHeader()

            Spacer(modifier = Modifier.height(KptTheme.spacing.sm))

            Text(
                text = stringResource(Res.string.feature_signup_account_choice_title),
                style = MifosTypography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() },
            )

            Text(
                text = stringResource(Res.string.feature_signup_account_choice_subtitle),
                style = MifosTypography.bodyMedium,
                color = KptTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(KptTheme.spacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
            ) {
                MifosButton(
                    onClick = onExistingCustomer,
                    modifier = Modifier.weight(1f),
                    shape = KptTheme.shapes.medium,
                ) {
                    Text(text = stringResource(Res.string.yes))
                }

                MifosButton(
                    onClick = onNewCustomer,
                    modifier = Modifier.weight(1f),
                    shape = KptTheme.shapes.medium,
                ) {
                    Text(text = stringResource(Res.string.no))
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountChoiceScreenPreview() {
    MifosMobileTheme {
        AccountChoiceScreen(
            onExistingCustomer = {},
            onNewCustomer = {},
        )
    }
}
