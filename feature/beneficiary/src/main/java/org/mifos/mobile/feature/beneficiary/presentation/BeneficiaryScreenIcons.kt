/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.beneficiary.R

@Composable
internal fun BeneficiaryScreenIcons(
    addIconClicked: () -> Unit,
    scanIconClicked: () -> Unit,
    uploadIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
        ) {
            RenderIconAndText(
                icon = R.drawable.ic_beneficiary_add_48px,
                text = stringResource(id = R.string.add),
                iconDescription = stringResource(id = R.string.add),
                iconClick = addIconClicked,
            )

            RenderIconAndText(
                icon = R.drawable.ic_qrcode_scan_gray_dark,
                text = stringResource(id = R.string.scan),
                iconDescription = stringResource(id = R.string.scan),
                iconClick = scanIconClicked,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            RenderIconAndText(
                icon = R.drawable.ic_file_upload_black_24dp,
                text = stringResource(id = R.string.upload_qr_code),
                iconDescription = stringResource(id = R.string.upload_qr_code),
                iconClick = uploadIconClicked,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun IconsScreenPreview() {
    MifosMobileTheme {
        Surface {
            BeneficiaryScreenIcons(
                addIconClicked = {},
                scanIconClicked = {},
                uploadIconClicked = {},
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}
