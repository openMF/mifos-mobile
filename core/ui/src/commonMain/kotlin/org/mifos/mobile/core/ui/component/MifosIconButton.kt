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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MifosIconButton(
    label: String,
    trailingIcon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    MifosButton(
        onClick = onClick,
        shape = DesignToken.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.primaryBlue,
            contentColor = AppColors.customWhite,
        ),
        modifier = modifier
            .width(327.dp)
            .padding(horizontal = DesignToken.padding.small),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MifosTypography.bodyMedium,
                    color = AppColors.customWhite,
                )
                Spacer(modifier = Modifier.width(DesignToken.padding.small))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = label,
                    tint = AppColors.customWhite,
                )
            }
        },
    )
}

@DevicePreview
@Composable
fun MifosIconButtonPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosIconButton(
            modifier = modifier,
            label = "Button",
            trailingIcon = MifosIcons.ArrowRight,
            onClick = {},
        )
    }
}
