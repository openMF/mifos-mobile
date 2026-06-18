/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.pocket.generated.resources.Res
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_empty_action
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_empty_description
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_empty_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun EmptyPocketContent(
    onLinkFirstAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(DesignToken.padding.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            modifier = Modifier.size(180.dp),
            shape = CircleShape,
            color = KptTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = MifosIcons.SavingsAccount,
                    contentDescription = null,
                    tint = KptTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

        Text(
            text = stringResource(Res.string.feature_pocket_empty_title),
            style = MifosTypography.headlineSmallEmphasized,
            color = KptTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        Text(
            text = stringResource(Res.string.feature_pocket_empty_description),
            style = MifosTypography.bodyLarge,
            color = KptTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

        MifosButton(
            onClick = onLinkFirstAccount,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = KptTheme.colorScheme.primary,
                contentColor = KptTheme.colorScheme.onPrimary,
            ),
            text = {
                Text(
                    text = stringResource(Res.string.feature_pocket_empty_action),
                    style = MifosTypography.labelLarge,
                )
            },
        )
    }
}

@Preview
@Composable
fun EmptyPocketContentPreview() {
    MifosMobileTheme(darkTheme = false) {
        EmptyPocketContent(
            onLinkFirstAccount = {},
        )
    }
}
