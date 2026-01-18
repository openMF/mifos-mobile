/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.amount_due
import mifos_mobile.feature.client_charge.generated.resources.amount_paid
import mifos_mobile.feature.client_charge.generated.resources.database_checkmark
import mifos_mobile.feature.client_charge.generated.resources.database_warning
import mifos_mobile.feature.client_charge.generated.resources.error_no_charge
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_charge_id
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.Charge
import template.core.base.designsystem.theme.KptTheme

/**
 * Composable function that displays a charge item.
 *
 * @param charge The charge to be displayed.
 * @param onChargeClick A lambda function that is called when the user clicks on a charge.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun ClientChargeItem(
    charge: Charge,
    onChargeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currencyRepresentation = charge.currency?.code ?: ""
    Row(
        modifier
            .fillMaxWidth()
            .clickable {
                onChargeClick()
            }
            .padding(vertical = KptTheme.spacing.md),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = if (charge.isChargePaid) {
                painterResource(Res.drawable.database_checkmark)
            } else {
                painterResource(Res.drawable.database_warning)
            },
            contentDescription = "Charges Symbol",
            tint = KptTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier
                .size(DesignToken.sizes.iconExtraLarge)
                .background(
                    color = KptTheme.colorScheme.background.copy(alpha = 0.3f),
                    shape = CircleShape,
                )
                .padding(KptTheme.spacing.sm),

        )
        Spacer(Modifier.width(KptTheme.spacing.md))
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = charge.name ?: "",
                style = MifosTypography.titleSmallEmphasized,
            )
            // TODO: in Figma account Number is there instead of charge id. Refactor it
            Text(
                text = stringResource(
                    Res.string.feature_client_charges_charge_id,
                    charge.chargeId.toString(),
                ),
                style = MifosTypography.bodySmall,
            )

            Text(
                text = if (!charge.dueDate.isEmpty() && charge.dueDate.size >= 3) {
                    DateHelper.getDateAsString(charge.dueDate.mapNotNull { it })
                } else {
                    stringResource(Res.string.error_no_charge)
                },
                style = MifosTypography.bodySmall,
            )
        }
        Spacer(Modifier.width(KptTheme.spacing.md))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
        ) {
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = if (charge.isChargePaid) {
                        stringResource(Res.string.amount_paid)
                    } else {
                        stringResource(Res.string.amount_due)
                    },
                    style = MifosTypography.labelSmall,
                    color = if (charge.isChargePaid) {
                        AppColors.customEnable
                    } else {
                        KptTheme.colorScheme.error
                    },
                )
                Text(
                    text = if (currencyRepresentation.isNotEmpty()) {
                        if (charge.isChargePaid) {
                            CurrencyFormatter.format(
                                charge.amountPaid,
                                currencyRepresentation,
                                2,
                            )
                        } else {
                            CurrencyFormatter.format(
                                charge.amount,
                                currencyRepresentation,
                                2,
                            )
                        }
                    } else {
                        ""
                    },
                    style = MifosTypography.labelSmall,
                    color = if (charge.isChargePaid) {
                        AppColors.customEnable
                    } else {
                        KptTheme.colorScheme.error
                    },
                )
            }
            Icon(
                imageVector = MifosIcons.ChevronRight,
                contentDescription = "Navigation Icon",
                modifier = Modifier.size(DesignToken.sizes.iconDp20),
            )
        }
    }
}

@Preview
@Composable
private fun ClientChargesItemPreview() {
    MifosMobileTheme {
        ClientChargeItem(
            charge = Charge(),
            onChargeClick = {},
        )
    }
}
