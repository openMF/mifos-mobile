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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.beneficiary.R

@Composable
internal fun BeneficiaryScreen(
    topAppbarNavigateBack: () -> Unit,
    addIconClicked: () -> Unit,
    scanIconClicked: () -> Unit,
    uploadIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            MifosTopBar(
                navigateBack = topAppbarNavigateBack,
                title = {
                    Text(text = stringResource(id = R.string.add_beneficiary))
                },
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(10.dp),
        ) {
            Text(
                stringResource(id = R.string.select_mode),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.add_beneficiary_option),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )

            BeneficiaryScreenIcons(
                addIconClicked = addIconClicked,
                scanIconClicked = scanIconClicked,
                uploadIconClicked = uploadIconClicked,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@DevicePreviews
@Composable
private fun BeneficiaryScreenPreview() {
    MifosMobileTheme {
        BeneficiaryScreen({}, {}, {}, {})
    }
}
