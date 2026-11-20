/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.privacyNotice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_auth_mifos_logo
import mifos_mobile.feature.auth.generated.resources.feature_privacy_notice_accept
import mifos_mobile.feature.auth.generated.resources.feature_privacy_notice_body
import mifos_mobile.feature.auth.generated.resources.feature_privacy_notice_decline
import mifos_mobile.feature.auth.generated.resources.feature_privacy_notice_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import template.core.base.designsystem.theme.KptTheme

@Composable
fun PrivacyNoticeScreen(
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBar = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(KptTheme.spacing.sm),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.feature_auth_mifos_logo),
                    contentDescription = null,
                    modifier = Modifier.height(KptTheme.spacing.xxl),
                )
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(Res.string.feature_privacy_notice_title),
                style = KptTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = KptTheme.spacing.md,
                    end = KptTheme.spacing.md,
                    bottom = KptTheme.spacing.sm,
                ),
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = KptTheme.spacing.md),
                ) {
                    Text(
                        text = stringResource(Res.string.feature_privacy_notice_body),
                        style = KptTheme.typography.bodyMedium,
                    )

                    Spacer(modifier = Modifier.height(KptTheme.spacing.xxl))
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    KptTheme.colorScheme.background.copy(alpha = 0.9f),
                                    KptTheme.colorScheme.background,
                                ),
                            ),
                        )
                        .padding(
                            start = KptTheme.spacing.md,
                            end = KptTheme.spacing.md,
                            top = KptTheme.spacing.lg,
                            bottom = KptTheme.spacing.md,
                        ),
                    horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
                ) {
                    MifosButton(
                        onClick = onDeclineClick,
                        modifier = Modifier.weight(1f),
                        shape = KptTheme.shapes.medium,
                    ) {
                        Text(stringResource(Res.string.feature_privacy_notice_decline))
                    }
                    MifosButton(
                        onClick = onAcceptClick,
                        modifier = Modifier.weight(1f),
                        shape = KptTheme.shapes.medium,
                    ) {
                        Text(stringResource(Res.string.feature_privacy_notice_accept))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PrivacyNoticeScreenPreview() {
    PrivacyNoticeScreen(
        onAcceptClick = {},
        onDeclineClick = {},
    )
}
