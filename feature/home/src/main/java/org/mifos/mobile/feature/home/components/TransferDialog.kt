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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransferDialog(
    onDismissRequest: () -> Unit,
    navigateToTransfer: () -> Unit,
    navigateToThirdPartyTransfer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.transfer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateToTransfer()
                        },
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.third_party_transfer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateToThirdPartyTransfer()
                        },
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun TransferDialogPreview() {
    MifosMobileTheme {
        TransferDialog(
            onDismissRequest = {},
            navigateToTransfer = {},
            navigateToThirdPartyTransfer = {},
        )
    }
}
