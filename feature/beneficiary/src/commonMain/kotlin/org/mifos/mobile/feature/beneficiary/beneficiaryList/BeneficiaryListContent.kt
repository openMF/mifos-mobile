/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.component.MifosBeneficiariesCard

@Composable
fun ShowBeneficiary(
    beneficiaryList: List<Beneficiary>,
    onClick: (beneficiaryId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        LazyColumn {
            items(beneficiaryList) { beneficiary ->
                MifosBeneficiariesCard(
                    beneficiary = beneficiary,
                    onBeneficiaryClick = { onClick(beneficiary.id ?: -1) },
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBeneficiaryListEmpty() {
    val beneficiary = Beneficiary(name = "Victor", id = 242344343, officeName = "Main office")
    MifosMobileTheme {
        MifosBeneficiariesCard(
            beneficiary = beneficiary,
            onBeneficiaryClick = {},
        )
    }
}
