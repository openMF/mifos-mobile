/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.savings_account
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.theme.KptTheme

@Composable
fun MifosActionCard(
    title: StringResource,
    subTitle: StringResource,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = DesignToken.padding.medium),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = KptTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier
                    .background(
                        color = KptTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                        shape = CircleShape,
                    )
                    .padding(KptTheme.spacing.sm),

            )

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(title),
                    style = MifosTypography.titleSmallEmphasized,
                    color = KptTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(subTitle),
                    style = MifosTypography.bodySmall,
                    color = KptTheme.colorScheme.secondary,
                )
            }

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

            Icon(
                imageVector = MifosIcons.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(DesignToken.sizes.iconDp20),
            )
        }
    }
}

@Preview
@Composable
private fun Savings_Action_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(KptTheme.spacing.md),
        ) {
            MifosActionCard(
                title = Res.string.savings_account,
                subTitle = Res.string.savings_account,
                icon = MifosIcons.Money,
                onClick = {},
            )
        }
    }
}
