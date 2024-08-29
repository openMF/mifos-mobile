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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.R
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun UserProfileField(
    @StringRes text: Int,
    @DrawableRes icon: Int,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = text),
            color = Color(0xFF8E9099),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )
    }
    HorizontalDivider(color = if (isSystemInDarkTheme()) Color(0xFF8E9099) else Color.Black)
}

@Composable
fun UserProfileField(
    @StringRes label: Int,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = label),
            color = Color(0xFF8E9099),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = value,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            style = TextStyle(fontSize = 14.sp),
        )
    }
    HorizontalDivider(color = Color(0xFF8E9099))
}

@DevicePreviews
@Composable
private fun UserProfileFieldPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        UserProfileField(
            text = R.string.core_common_working,
            icon = R.drawable.core_common_circular_background,
            onClick = {},
            modifier = modifier,
        )
    }
}

@DevicePreviews
@Composable
private fun UserProfileFieldValuePreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        UserProfileField(
            label = R.string.core_common_working,
            value = "UserProfileFieldValue",
            modifier = modifier,
        )
    }
}
