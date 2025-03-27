/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.third_party_transfer.generated.resources.Res
import mifos_mobile.feature.third_party_transfer.generated.resources.third_party_transfer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun ThirdPartyTransferScreen(
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    reviewTransfer: (ReviewTransferPayload, TransferType, TransferSuccessDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ThirdPartyTransferViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            ThirdPartyTransferEvent.AddBeneficiary -> addBeneficiary.invoke()
            ThirdPartyTransferEvent.Navigate -> navigateBack.invoke()
            is ThirdPartyTransferEvent.ReviewTransfer -> {
                reviewTransfer(
                    event.reviewTransferPayload,
                    TransferType.TPT,
                    TransferSuccessDestination.HOME,
                )
            }
        }
    }

    ThirdPartyTransferScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun ThirdPartyTransferDialog(
    state: ThirdPartyTransferState,
) {
    when (state.dialogState) {
        is ThirdPartyTransferState.DialogState.Error -> MifosErrorComponent(
            message = state.dialogState.message,
            isNetworkConnected = state.isOnline,
        )

        is ThirdPartyTransferState.DialogState.Loading -> MifosProgressIndicator(modifier = Modifier.fillMaxSize())

        null -> Unit
    }
}

@Composable
private fun ThirdPartyTransferScreen(
    state: ThirdPartyTransferState,
    onAction: (ThirdPartyTransferAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.third_party_transfer),
        backPress = { onAction(ThirdPartyTransferAction.OnNavigate) },
        modifier = Modifier,
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
            if (!state.beneficiaries.isNullOrEmpty() && !state.fromAccountDetail.isNullOrEmpty
                    () && !state.toAccountOption.isNullOrEmpty()
            ) {
                ThirdPartyTransferContent(
                    state = state,
                    onAction = onAction,
                    modifier = modifier,
                )
            }
        }
    }
    ThirdPartyTransferDialog(
        state = state,
    )
}

@Preview
@Composable
private fun ThirdPartyTransferScreenPreview() {
    MifosMobileTheme {
        ThirdPartyTransferScreen(
            state = ThirdPartyTransferState(dialogState = null),
            onAction = { },
            modifier = Modifier,
        )
    }
}
