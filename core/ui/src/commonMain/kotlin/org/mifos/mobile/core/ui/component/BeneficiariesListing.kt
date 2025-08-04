/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.icon_next
import org.jetbrains.compose.resources.painterResource
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MifosBeneficiariesCard(
    beneficiary: Beneficiary,
    onBeneficiaryClick: (Beneficiary) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBeneficiaryClick(beneficiary) }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Logo section
            MifosUserImage(
                username = beneficiary.name,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .background(Color.Gray),
                bitmap = null,
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Content section
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = beneficiary.name!!,
                    style = MifosTypography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${beneficiary.accountType?.value}: ${beneficiary.accountNumber}",
                    style = MifosTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = beneficiary.officeName!!,
                    style = MifosTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                modifier = Modifier
                    .size(20.dp),
                onClick = {
                    onBeneficiaryClick.invoke(beneficiary)
                },
            ) {
                Icon(
                    painter = painterResource(resource = Res.drawable.icon_next),
                    contentDescription = "Next",
                )
            }
        }
    }
}

@DevicePreview
@Composable
fun MifosBeneficiariesCardPreview() {
    MifosMobileTheme {
        MifosBeneficiariesCard(
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
            onBeneficiaryClick = {},
        )
    }
}
