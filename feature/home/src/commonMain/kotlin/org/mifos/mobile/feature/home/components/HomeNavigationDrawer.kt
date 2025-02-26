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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.feature.home.viewmodel.HomeNavigationItems

@Composable
internal fun HomeNavigationDrawer(
    username: String,
    drawerState: DrawerState,
    userBitmap: ByteArray?,
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
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
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(1f),
                        )
                    }

                    items(
                        items = HomeNavigationItems.entries.toTypedArray(),
                        itemContent = { item ->
                            NavigationDrawerItem(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                label = {
                                    Row {
                                        Icon(
                                            imageVector = item.imageVector,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = stringResource(item.nameResId))
                                    }
                                },
                                selected = item == HomeNavigationItems.Home,
                                onClick = { navigateItem(item) },
                            )
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

@Preview
@Composable
private fun HomeNavigationDrawerPreview() {
    MifosMobileTheme {
        HomeNavigationDrawer(
            username = "Mifos",
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            userBitmap = ByteArray(0),
            navigateItem = {},
            content = {},
        )
    }
}
