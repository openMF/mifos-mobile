/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun MifosRadioButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val radioBorderColor = if (selected) {
        AppColors.primaryBlue
    } else {
        AppColors.borderColor
    }

    val borderColor = if (selected) {
        AppColors.primaryBlue
    } else {
        AppColors.borderColorOne
    }

    val textStyle = if(selected) {
        MifosTypography.titleSmallEmphasized
    } else {
        MifosTypography.titleSmall
    }

    Row(
        modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = DesignToken.shapes.medium,
            )
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(
                DesignToken.padding.large,
            ),
        horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled,
            colors = RadioButtonColors(
                selectedColor = radioBorderColor,
                unselectedColor = radioBorderColor,
                disabledSelectedColor = radioBorderColor,
                disabledUnselectedColor = radioBorderColor,
            ),
        )
        Text(
            text = label,
            style = textStyle,
        )
    }
}

@Preview
@Composable
fun Radio_Button_Preview() {
    MifosMobileTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.medium),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium)
        ) {
            MifosRadioButton(
                label = "Telugu",
                selected = true,
                onClick = { },
                modifier = Modifier,
            )

            MifosRadioButton(
                label = "English",
                selected = false,
                onClick = { },
                modifier = Modifier,
            )

            MifosRadioButton(
                label = "Telugu",
                selected = true,
                enabled = false,
                onClick = { },
                modifier = Modifier,
            )

            MifosRadioButton(
                label = "English",
                selected = false,
                enabled = false,
                onClick = { },
                modifier = Modifier,
            )
        }
    }
}
