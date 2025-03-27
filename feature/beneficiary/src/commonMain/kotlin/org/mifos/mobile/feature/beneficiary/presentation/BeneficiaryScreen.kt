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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.add_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.add_beneficiary_option
import mifos_mobile.feature.beneficiary.generated.resources.select_mode
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

@Composable
internal fun BeneficiaryScreen(
    topAppbarNavigateBack: () -> Unit,
    addIconClicked: () -> Unit,
    scanIconClicked: () -> Unit,
    uploadIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.add_beneficiary),
        backPress = topAppbarNavigateBack,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                stringResource(Res.string.select_mode),
                style = MaterialTheme.typography.labelMedium,
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.add_beneficiary_option),
                style = MaterialTheme.typography.labelMedium,
            )

            BeneficiaryScreenIcons(
                addIconClicked = { addIconClicked() },
                scanIconClicked = { scanIconClicked() },
                uploadIconClicked = { uploadIconClicked() },
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun BeneficiaryScreenPreview() {
    MifosMobileTheme {
        BeneficiaryScreen({}, {}, {}, {})
    }
}
