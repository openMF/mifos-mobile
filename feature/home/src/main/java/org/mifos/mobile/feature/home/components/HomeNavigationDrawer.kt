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

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.home.viewmodel.HomeNavigationItems

@Composable
internal fun HomeNavigationDrawer(
    username: String,
    drawerState: DrawerState,
    userBitmap: Bitmap?,
    navigateItem: (HomeNavigationItems) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        content = content,
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn {
                    item {
                        MifosUserImage(
                            modifier = Modifier
                                .padding(20.dp)
                                .size(84.dp),
                            bitmap = userBitmap,
                            username = username,
                        )
                        Text(
                            text = username,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(1f),
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(
                        items = HomeNavigationItems.entries.toTypedArray(),
                        itemContent = { item ->
                            Spacer(modifier = Modifier.height(12.dp))
                            NavigationDrawerItem(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                label = {
                                    Row {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = item.iconResId),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = stringResource(id = item.nameResId))
                                    }
                                },
                                selected = item == HomeNavigationItems.Home,
                                onClick = { navigateItem(item) },
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if (item == HomeNavigationItems.ManageBeneficiaries) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                )
                            }
                        },
                    )
                }
            }
        },
    )
}

@DevicePreviews
@Composable
private fun HomeNavigationDrawerPreview() {
    MifosMobileTheme {
        HomeNavigationDrawer(
            username = "Avneet",
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            userBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            navigateItem = {},
            content = {},
        )
    }
}
