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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import org.jetbrains.compose.resources.painterResource
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MifosBeneficiaryTopCard(
    modifier: Modifier = Modifier,
    beneficiary: Beneficiary?,
) {
    MifosCard(
        modifier = Modifier
            .size(320.dp, 150.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = modifier
                    .matchParentSize(),
                painter = painterResource(Res.drawable.ic_icon_dashboard),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MifosUserImage(
                    username = beneficiary?.name,
                    modifier = modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .background(Color.Gray),
                    bitmap = null,
                )
                Spacer(modifier = modifier.width(16.dp))
                Column {
                    Text(
                        text = beneficiary!!.name!!,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@DevicePreview
@Composable
fun BeneficiaryTopCardPreview() {
    MifosMobileTheme {
        MifosBeneficiaryTopCard(
            beneficiary = Beneficiary(
                name = "Rajesh Bajaj",
                accountNumber = "135678976666",
                officeName = "Head Office",
                clientName = "Client Name",
                accountType = AccountType(
                    id = 1,
                    code = "accountType.loan",
                    value = "Loan Account",
                ),
                transferLimit = 123456789.122,
            ),
        )
    }
}
