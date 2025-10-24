/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.chargeDetails

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.core.ui.generated.resources.ic_icon_success
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.charge_details
import mifos_mobile.feature.client_charge.generated.resources.paid_on
import mifos_mobile.feature.client_charge.generated.resources.paid_success_message
import mifos_mobile.feature.client_charge.generated.resources.partial_amount_paid_on
import mifos_mobile.feature.client_charge.generated.resources.pay_outstanding
import mifos_mobile.feature.client_charge.generated.resources.ref_no
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect
import mifos_mobile.core.ui.generated.resources.Res as uiRes

/**
 * ChargeDetailScreen is a composable function that displays the details of a charge.
 *
 * @param onNavigateBack A lambda function that is called when the user navigates back.
 * @param modifier Modifier for the composable.
 * @param viewModel The ChargeDetailsViewModel for the screen.
 */
@Composable
internal fun ChargeDetailScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChargeDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            ChargeDetailsEvent.NavigateBack -> onNavigateBack.invoke()
        }
    }

    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.charge_details),
        onNavigateBack = {
            viewModel.trySendAction(ChargeDetailsAction.NavigateBack)
        },
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
                MifosDetailsCard(keyValuePairs = state.details)
                Spacer(Modifier.height(DesignToken.padding.extraExtraLarge))
                if (state.isPaid) {
                    ChargeDetailsPaidComponent(
                        refNo = state.refNo,
                        paidOn = state.paidOn,
                    )
                } else {
                    ChargeDetailsUnPaidComponent(
                        amountPaidOn = state.paidOn,
                        onPayOutStanding = {
                            viewModel.trySendAction(ChargeDetailsAction.PayOutStanding)
                        },
                    )
                }
            }
        },
    )
}

/**
 * ChargeDetailsPaidComponent is a composable function that displays the details of a paid charge.
 *
 * @param refNo Reference number of the charge.
 * @param paidOn Date when the charge was paid.
 * @param modifier Modifier for the composable.
 */
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
            text = stringResource(Res.string.paid_success_message),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(DesignToken.padding.medium))
        Image(
            modifier = Modifier
                .height(60.dp)
                .width(60.dp),
            painter = painterResource(uiRes.drawable.ic_icon_success),
            contentDescription = "Status icon",
        )
        Spacer(Modifier.height(DesignToken.padding.medium))
        Text(
            text = stringResource(Res.string.ref_no, refNo),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(DesignToken.padding.small))
        if (paidOn.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.paid_on, paidOn),
                style = MifosTypography.bodySmallEmphasized,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * ChargeDetailsUnPaidComponent is a composable function that displays the details of an unpaid charge.
 *
 * @param amountPaidOn Date when the charge was paid.
 * @param onPayOutStanding A lambda function that is called when the user pays the outstanding amount.
 * @param modifier Modifier for the composable.
 */
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
            Text(stringResource(Res.string.pay_outstanding))
        }
        Spacer(Modifier.height(DesignToken.padding.large))
        if (amountPaidOn.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.partial_amount_paid_on, amountPaidOn),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
