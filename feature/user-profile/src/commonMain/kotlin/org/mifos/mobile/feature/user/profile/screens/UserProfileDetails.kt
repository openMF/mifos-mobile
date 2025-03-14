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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mifos_mobile.feature.user_profile.generated.resources.Res
import mifos_mobile.feature.user_profile.generated.resources.ic_cake_24dp
import mifos_mobile.feature.user_profile.generated.resources.ic_gender_24dp
import mifos_mobile.feature.user_profile.generated.resources.ic_phone_24dp
import mifos_mobile.feature.user_profile.generated.resources.user_details
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.client.Client

@Composable
internal fun UserProfileDetails(
    userDetails: Client,
    modifier: Modifier = Modifier,
) {
    val hasUserDetails = userDetails.mobileNo != null ||
        userDetails.dobDate.isNotEmpty() ||
        userDetails.gender?.name != null

    if (hasUserDetails) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.user_details),
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
            )

            userDetails.mobileNo?.let { mobileNumber ->
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Icon(
                        modifier = Modifier.padding(top = 8.dp),
                        painter = painterResource(Res.drawable.ic_phone_24dp),
                        contentDescription = null,
                    )
                    Text(
                        text = mobileNumber,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            userDetails.dobDate.takeIf { it.isNotEmpty() }?.let { dob ->
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Icon(
                        modifier = Modifier.padding(top = 8.dp),
                        painter = painterResource(Res.drawable.ic_cake_24dp),
                        contentDescription = null,
                    )
                    Text(
                        text = DateHelper.getDateAsString(dob),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            userDetails.gender?.name?.let { genderName ->
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Icon(
                        modifier = Modifier.padding(top = 8.dp),
                        painter = painterResource(Res.drawable.ic_gender_24dp),
                        contentDescription = null,
                    )
                    Text(
                        text = genderName,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UserProfileDetailsPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        UserProfileDetails(
            userDetails = Client(),
            modifier = modifier,
        )
    }
}
