/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.account_number_label
import mifos_mobile.feature.beneficiary.generated.resources.account_type_label
import mifos_mobile.feature.beneficiary.generated.resources.account_type_loan
import mifos_mobile.feature.beneficiary.generated.resources.account_type_savings
import mifos_mobile.feature.beneficiary.generated.resources.account_type_share
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_name_label
import mifos_mobile.feature.beneficiary.generated.resources.confirm_details
import mifos_mobile.feature.beneficiary.generated.resources.office_label
import mifos_mobile.feature.beneficiary.generated.resources.transfer_limit_label
import mifos_mobile.feature.beneficiary.generated.resources.validate_details
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun BeneficiaryApplicationConfirmationScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: BeneficiaryApplicationConfirmationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryApplicationConfirmationEvent.Navigate -> {
                navigateBack.invoke()
            }
            is BeneficiaryApplicationConfirmationEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }

            is BeneficiaryApplicationConfirmationEvent.NavigateToAuthenticate -> {
                navigateToAuthenticateScreen.invoke()
            }
        }
    }
    BeneficiaryApplicationConfirmationScreenContent(
        state = state,
        onAction = remember {
            {
                viewModel.trySendAction(it)
            }
        },
    )
    BeneficiaryApplicationConfirmationScreenDialogs(
        state = state,
    )
}

@Composable
private fun BeneficiaryApplicationConfirmationScreenDialogs(
    state: BeneficiaryApplicationConfirmationState,
) {
    when (state.dialogState) {
        BeneficiaryApplicationConfirmationState.DialogState.Loading -> MifosProgressIndicator()
        BeneficiaryApplicationConfirmationState.DialogState.Network -> {
            MifosErrorComponent(
                isNetworkConnected = !state.networkUnavailable,
            )
        }
        null -> Unit
    }
}

@Composable
fun BeneficiaryApplicationConfirmationScreenContent(
    state: BeneficiaryApplicationConfirmationState,
    modifier: Modifier = Modifier,
    onAction: (BeneficiaryApplicationConfirmationAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(state.topBarTitle),
        onNavigateBack = { onAction(BeneficiaryApplicationConfirmationAction.OnNavigate) },
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
        content = {
            if (state.dialogState == null) {
                Column(
                    Modifier.padding(DesignToken.padding.large),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
                ) {
                    Text(
                        text = stringResource(Res.string.validate_details),
                        style = MifosTypography.labelLargeEmphasized,
                    )

                    MifosDetailsCard(state.details)
                    MifosButton(
                        onClick = {
                            onAction(BeneficiaryApplicationConfirmationAction.SubmitBeneficiary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Text(stringResource(Res.string.confirm_details))
                        },
                    )
                }
            }
        },
    )
}
