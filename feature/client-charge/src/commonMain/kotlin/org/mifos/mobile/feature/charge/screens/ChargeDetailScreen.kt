/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_success
import org.jetbrains.compose.resources.painterResource
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.feature.charge.components.ChargeDetailsCard

@Composable
fun ChargeDetailScreen(
    modifier: Modifier = Modifier,
    isPaid: Boolean = false,
) {
    val sampleDetails = mapOf(
        "Charge Name" to "Client Registration Fee",
        "Charge Type" to "Flat",
        "Currency" to "USD",
        "Amount" to "100.00",
        "Due Date" to "28 July 2045",
    )

    MifosElevatedScaffold(
        topBarTitle = "Charge Details",
        onNavigateBack = {},
        modifier = modifier,
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        vertical = DesignToken.padding.extraLarge,
                        horizontal = DesignToken.padding.large,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ChargeDetailsCard(keyValuePairs = sampleDetails)
                Spacer(Modifier.height(DesignToken.padding.extraExtraLarge))
                if (isPaid) {
                    ChargeDetailsPaidComponent(
                        refNo = "&%^&BHB",
                        paidOn = "jndjdnb",
                    )
                } else {
                    ChargeDetailsUnPaidComponent(
                        amountPaidOn = "30-10-2025",
                        onPayOutStanding = {},
                    )
                }
            }
        },
    )
}

@Composable
fun ChargeDetailsPaidComponent(
    refNo: String,
    paidOn: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DesignToken.padding.largeIncreased),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "You have successfully paid this charge completely",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(DesignToken.padding.medium))
        Image(
            modifier = Modifier
                .height(60.dp)
                .width(60.dp),
            painter = painterResource(Res.drawable.ic_icon_success),
            contentDescription = "Status icon",
        )
        Spacer(Modifier.height(DesignToken.padding.medium))
        Text(
            text = "Ref. No. $refNo",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(DesignToken.padding.small))
        Text(
            text = "Paid On: $paidOn",
            style = MifosTypography.bodySmallEmphasized,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun ChargeDetailsUnPaidComponent(
    amountPaidOn: String,
    onPayOutStanding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MifosButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.buttonHeight),
            shape = DesignToken.shapes.medium,
            onClick = onPayOutStanding,
        ) {
            Text("Pay Outstanding")
        }
        Spacer(Modifier.height(DesignToken.padding.large))
        Text(
            text = "Partial Amount Paid On : $amountPaidOn",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
