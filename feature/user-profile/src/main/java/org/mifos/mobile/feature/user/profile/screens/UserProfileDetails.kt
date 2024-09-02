/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.user.profile.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.user.profile.R
import org.mifos.mobile.feature.user.profile.utils.UserDetails

@Composable
internal fun UserProfileDetails(
    userDetails: UserDetails,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(top = 16.dp, start = 8.dp),
            text = stringResource(id = R.string.user_details),
            color = MaterialTheme.colorScheme.onSecondary,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Icon(
                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                painter = painterResource(id = R.drawable.ic_phone_24dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null,
            )
            if (userDetails.phoneNumber != null) {
                Text(
                    text = userDetails.phoneNumber,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp),
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Icon(
                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                painter = painterResource(id = R.drawable.ic_cake_24dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null,
            )
            if (userDetails.dob != null) {
                Text(
                    text = userDetails.dob,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp),
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Icon(
                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                painter = painterResource(id = R.drawable.ic_gender_24dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null,
            )
            if (userDetails.gender != null) {
                Text(
                    text = userDetails.gender,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp),
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun UserProfileDetailsPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        UserProfileDetails(
            userDetails = UserDetails(
                userName = "John Doe",
                accountNumber = "123456",
                activationDate = "01/01/2021",
                officeName = "Office Name",
                clientType = "Client Type",
                groups = "Groups",
                clientClassification = "Client Classification",
                phoneNumber = "1234567890",
                dob = "01/01/1990",
                gender = "Male",
            ),
            modifier = modifier,
        )
    }
}
