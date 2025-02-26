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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.third_party_transfer
import mifos_mobile.feature.home.generated.resources.transfer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

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
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(Res.string.transfer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateToTransfer()
                        },
                )
                HorizontalDivider()
                Text(
                    text = stringResource(Res.string.third_party_transfer),
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

@Preview
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
