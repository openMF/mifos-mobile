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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosTopBarTitleComposable
import org.mifos.mobile.core.designsystem.icon.MifosIcons

@Composable
fun UserProfileTopBar(
    text: StringResource,
    home: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosTopBarTitleComposable(
        title = {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(text),
                    style = MaterialTheme.typography.titleMedium,
                )
                Icon(
                    imageVector = MifosIcons.Edit,
                    contentDescription = "User Profile Icon",
                )
            }
        },
        navigateBack = home,
    )
}
