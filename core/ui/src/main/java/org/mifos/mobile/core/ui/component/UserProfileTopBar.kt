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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.designsystem.icons.MifosIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileTopBar(
    text: Int,
    home: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier.height(64.dp),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = text),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    style = TextStyle(fontSize = 24.sp),
                )
                Icon(
                    imageVector = MifosIcons.Edit,
                    contentDescription = null,
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        navigationIcon = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = MifosIcons.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = {
                        home.invoke()
                    }),
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme()) {
                Color(
                    0xFF1B1B1F,
                )
            } else {
                Color(0xFFFEFBFF)
            },
        ),
    )
}
