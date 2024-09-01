/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(
    notificationCount: Int,
    openNavigationDrawer: () -> Unit,
    openNotifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.home))
        },
        actions = {
            IconButton(
                onClick = {
                    openNotifications()
                },
            ) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.TopEnd,
                ) {
                    Icon(
                        imageVector = MifosIcons.Notifications,
                        contentDescription = null,
                    )

                    if (notificationCount > 0) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Red)
                                .padding(2.dp)
                                .size(8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = notificationCount.toString(),
                                color = Color.White,
                                fontSize = 6.sp,
                            )
                        }
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    openNavigationDrawer()
                },
            ) {
                Icon(
                    imageVector = MifosIcons.NavigationDrawer,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier,
    )
}

@DevicePreviews
@Composable
private fun HomeTopBarPreview() {
    HomeTopBar(
        openNavigationDrawer = {},
        openNotifications = {},
        notificationCount = 2,
    )
}
