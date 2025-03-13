/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.account_number
import mifos_mobile.feature.beneficiary.generated.resources.account_type
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_name
import mifos_mobile.feature.beneficiary.generated.resources.client_name
import mifos_mobile.feature.beneficiary.generated.resources.office_name
import mifos_mobile.feature.beneficiary.generated.resources.transfer_limit
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosTitleDescSingleLineEqual

@Composable
internal fun BeneficiaryDetailContent(
    state: BeneficiaryDetailState?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        MifosTitleDescSingleLineEqual(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(4.dp),
            title = stringResource(Res.string.beneficiary_name),
            description = state?.beneficiary?.name.toString(),
        )

        MifosTitleDescSingleLineEqual(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(4.dp),
            title = stringResource(Res.string.account_number),
            description = state?.beneficiary?.accountNumber.toString(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        MifosCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(Res.string.client_name),
                    description = state?.beneficiary?.clientName.toString(),
                )

                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(Res.string.account_type),
                    description = state?.beneficiary?.accountType?.value ?: "",
                )

                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(Res.string.transfer_limit),
                    description = state?.beneficiary?.transferLimit.toString(),
                )

                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(Res.string.office_name),
                    description = state?.beneficiary?.officeName.toString(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBeneficiaryDetailContent() {
    MifosMobileTheme {
        BeneficiaryDetailContent(
            state = BeneficiaryDetailState(beneficiaryDialog = null),
            modifier = Modifier,
        )
    }
}
