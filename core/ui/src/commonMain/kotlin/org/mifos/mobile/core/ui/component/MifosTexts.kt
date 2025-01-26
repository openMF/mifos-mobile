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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.core_ui_money_in
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun MifosTextTitleDescSingleLine(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = title,
            modifier = Modifier
                .alpha(0.7f),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = description,
        )
    }
}

@Composable
fun MifosTextTitleDescDoubleLine(
    title: String,
    description: String,
    descriptionStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .alpha(0.7f)
                .fillMaxWidth(),
        )
        Text(
            text = description,
            style = descriptionStyle,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun MifosTextTitleDescDrawableSingleLine(
    title: String,
    description: String,
    imageResId: DrawableResource,
    modifier: Modifier = Modifier,
    imageSize: Dp = 14.dp,
    onDrawableClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = title,
            modifier = Modifier
                .weight(1f)
                .alpha(0.7f),
        )
        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = description,
        )
        Spacer(modifier = Modifier.width(5.dp))
        Image(
            painter = painterResource(imageResId),
            contentDescription = "Image",
            modifier = Modifier
                .size(imageSize)
                .clickable { onDrawableClick() },
        )
    }
}

@Composable
fun MifosTitleDescSingleLineEqual(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = title,
            modifier = Modifier
                .alpha(0.7f)
                .weight(1f),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = description,
            modifier = Modifier.weight(1f),
        )
    }
}

@DevicePreviews
@Composable
private fun MifosTextTitleDescSingleLinePreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosTextTitleDescSingleLine(
            title = "MifosTextTitleDescSingleLine Title",
            description = "MifosTextTitleDescSingleLine Description",
            modifier = modifier,
        )
    }
}

@DevicePreviews
@Composable
private fun MifosTextTitleDescDoubleLinePreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosTextTitleDescDoubleLine(
            title = "MifosTextTitleDescDoubleLine Title",
            description = "MifosTextTitleDescDoubleLine Description",
            descriptionStyle = MaterialTheme.typography.bodyMedium,
            modifier = modifier,
        )
    }
}

@DevicePreviews
@Composable
private fun MifosTextTitleDescDrawableSingleLinePreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosTextTitleDescDrawableSingleLine(
            title = "MifosTextTitleDescDrawableSingleLine Title",
            description = "MifosTextTitleDescDrawableSingleLine Description",
            imageResId = Res.drawable.core_ui_money_in,
            modifier = modifier,
        )
    }
}

@DevicePreviews
@Composable
private fun MifosTitleDescSingleLineEqualPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosTitleDescSingleLineEqual(
            title = "MifosTitleDescSingleLineEqual Title",
            description = "MifosTitleDescSingleLineEqual Description",
            modifier = modifier,
        )
    }
}
