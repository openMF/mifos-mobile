/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.error_fetching_beneficiary_template
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun BeneficiaryApplicationScreen(
    navigateBack: () -> Unit,
    navigateToConfirmationScreen: (beneficiaryId: Int, beneficiary: BeneficiaryPayload, beneficiaryState: BeneficiaryState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryApplicationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryApplicationEvent.Navigate -> navigateBack.invoke()
            is BeneficiaryApplicationEvent.SubmitBeneficiary -> {
                navigateToConfirmationScreen(state.beneficiaryId, event.payload, event.state)
            }
        }
    }

    BeneficiaryApplicationScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun BeneficiaryApplicationDialogs(
    state: BeneficiaryApplicationState,
    onAction: (BeneficiaryApplicationAction) -> Unit,
) {
    when (state.dialogState) {
        BeneficiaryApplicationState.DialogState.Loading -> MifosProgressIndicator()

        is BeneficiaryApplicationState.DialogState.Error -> {
            MifosErrorComponent(
                isRetryEnabled = true,
                onRetry = { onAction(BeneficiaryApplicationAction.OnRetry) },
                message = stringResource(Res.string.error_fetching_beneficiary_template),
            )
        }

        BeneficiaryApplicationState.DialogState.Network -> {
            MifosErrorComponent(
                isNetworkConnected = !state.networkUnavailable,
            )
        }

        null -> Unit
    }
}

@Composable
private fun BeneficiaryApplicationScreen(
    state: BeneficiaryApplicationState,
    onAction: (BeneficiaryApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(state.topBarTitle),
        onNavigateBack = { onAction(BeneficiaryApplicationAction.OnNavigate) },
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
                BeneficiaryApplicationContent(
                    state = state,
                    onAction = onAction,
                )
            }
        },
    )
    BeneficiaryApplicationDialogs(
        state = state,
        onAction = onAction,
    )
}

@Preview
@Composable
private fun BeneficiaryApplicationScreenPreview() {
    MifosMobileTheme {
//        BeneficiaryApplicationScreen(
//            state = BeneficiaryApplicationState(
//                dialogState = null,
//                beneficiaryState = BeneficiaryState.CREATE_QR,
//            ),
//            onAction = { },
//            modifier = Modifier,
//        )
    }
}
