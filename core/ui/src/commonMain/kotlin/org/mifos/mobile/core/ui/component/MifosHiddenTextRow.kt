/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.core_ui_money_in
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun MifosHiddenTextRow(
    title: String,
    hiddenText: String,
    hiddenColor: Color,
    hidingText: String,
    visibilityIconId: DrawableResource,
    visibilityOffIconId: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isHidden by remember { mutableStateOf(true) }

    Row(
        modifier.clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .alpha(0.7f)
                .weight(1f),
        )
        Text(
            text = if (isHidden) {
                hidingText
            } else {
                hiddenText
            },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = hiddenColor,
        )
        IconButton(
            onClick = { isHidden = !isHidden },
            modifier = Modifier
                .padding(start = 6.dp)
                .size(24.dp),
        ) {
            Icon(
                painter = if (isHidden) {
                    painterResource(visibilityIconId)
                } else {
                    painterResource(visibilityOffIconId)
                },
                contentDescription = "Show or hide total amount",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun MifosHiddenTextRowPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosHiddenTextRow(
            title = "Title",
            hiddenText = "Hidden Text",
            hiddenColor = MaterialTheme.colorScheme.primary,
            hidingText = "Hiding Text",
            visibilityIconId = Res.drawable.core_ui_money_in,
            visibilityOffIconId = Res.drawable.core_ui_money_in,
            onClick = {},
            modifier = modifier,
        )
    }
}
