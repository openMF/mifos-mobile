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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.ui.utils.DevicePreview
import template.core.base.designsystem.theme.KptTheme

@Composable
fun MifosBeneficiariesCard(
    beneficiary: Beneficiary,
    onBeneficiaryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBeneficiaryClick() }
            .padding(
                KptTheme.spacing.md,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MifosUserImage(
                username = beneficiary.name,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(DesignToken.sizes.iconExtraLarge)
                    .background(Color.Gray.copy(alpha = 0.5f)),
                bitmap = null,
            )

            Spacer(modifier = Modifier.width(DesignToken.padding.medium))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
            ) {
                Text(
                    text = beneficiary.name ?: "",
                    style = MifosTypography.titleSmallEmphasized,
                )

                Text(
                    text = "${beneficiary.accountType?.value}: ${beneficiary.accountNumber}",
                    style = MifosTypography.bodySmall,
                )

                Text(
                    text = beneficiary.officeName ?: "",
                    style = MifosTypography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.width(DesignToken.padding.medium))

            Icon(
                imageVector = MifosIcons.ChevronRight,
                contentDescription = "Next",
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
            )
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
