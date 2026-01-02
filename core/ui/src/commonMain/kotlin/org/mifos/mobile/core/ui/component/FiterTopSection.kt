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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.feature_savings_apply
import mifos_mobile.core.ui.generated.resources.feature_savings_filter
import mifos_mobile.core.ui.generated.resources.feature_savings_reset
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.theme.KptTheme

@Composable
fun FilterTopSection(
    isAnyFilterSelected: Boolean,
    resetFilters: () -> Unit,
    onApplyFilter: () -> Unit,
    dismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resetColor = if (isAnyFilterSelected) {
        KptTheme.colorScheme.primary
    } else {
        KptTheme.colorScheme.inversePrimary
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
        ) {
            Icon(
                modifier = Modifier
                    .size(DesignToken.sizes.iconDp20)
                    .clickable { dismissDialog.invoke() },
                imageVector = MifosIcons.Dismiss,
                contentDescription = null,
            )
            Text(
                text = stringResource(Res.string.feature_savings_filter),
                style = MifosTypography.titleMedium,
                color = KptTheme.colorScheme.onBackground,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                DesignToken.spacing
                    .largeIncreased,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        isAnyFilterSelected,
                    ) { resetFilters.invoke() },
                horizontalArrangement = Arrangement.spacedBy(
                    DesignToken.spacing
                        .extraSmall,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(

                    text = stringResource(Res.string.feature_savings_reset),
                    style = MifosTypography.bodySmallEmphasized,
                    color = resetColor,
                )

                Icon(
                    modifier = Modifier.size(DesignToken.sizes.iconDp20),
                    imageVector = MifosIcons.ArrowCounterClockWise,
                    contentDescription = null,
                    tint = resetColor,
                )
            }

            Row(
                modifier = Modifier
                    .clickable { onApplyFilter.invoke() },
                horizontalArrangement = Arrangement.spacedBy(
                    DesignToken.spacing
                        .extraSmall,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(

                    text = stringResource(Res.string.feature_savings_apply),
                    style = MifosTypography.bodySmallEmphasized,
                    color = KptTheme.colorScheme.primary,
                )

                Icon(
                    modifier = Modifier.size(DesignToken.sizes.iconDp20),
                    imageVector = MifosIcons.CheckMark,
                    contentDescription = null,
                )
            }
        }
    }
}
