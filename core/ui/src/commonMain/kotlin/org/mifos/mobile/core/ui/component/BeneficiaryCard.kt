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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import org.jetbrains.compose.resources.painterResource
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MifosBeneficiaryTopCard(
    beneficiary: Beneficiary?,
    modifier: Modifier = Modifier,
) {
    MifosCustomCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier
                    .matchParentSize(),
                painter = painterResource(Res.drawable.ic_icon_dashboard),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Row(
                Modifier.padding(DesignToken.padding.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // pass BitMap also once fetching client Image
                MifosUserImage(
                    username = beneficiary?.name ?: "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(DesignToken.sizes.iconExtraLarge)
                        .background(Color.LightGray),
                    bitmap = null,
                )
                Spacer(modifier = Modifier.width(DesignToken.padding.medium))
                Column {
                    Text(
                        text = beneficiary?.name ?: "",
                        style = MifosTypography.titleMediumEmphasized,
                        color = AppColors.customWhite,
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
