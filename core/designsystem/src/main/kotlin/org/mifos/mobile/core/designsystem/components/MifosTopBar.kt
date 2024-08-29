/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import org.mifos.mobile.core.designsystem.icons.MifosIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosTopBar(
    navigateBack: () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
            ) {
                Icon(
                    imageVector = MifosIcons.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme()) {
                Color(0xFF1B1B1F)
            } else {
                Color(0xFFFEFBFF)
            },
        ),
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosTopBarTitle(
    @StringRes
    topBarTitleResId: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(id = topBarTitleResId)) },
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
            ) {
                Icon(
                    imageVector = MifosIcons.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme()) {
                Color(0xFF1B1B1F)
            } else {
                Color(0xFFFEFBFF)
            },
        ),
    )
}
