/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsMakeTransfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.deposit
import mifos_mobile.feature.savings.generated.resources.transfer
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect


@Composable
internal fun SavingsMakeTransferScreen(
    onCancelledClicked: () -> Unit,
    navigateBack: () -> Unit,
    reviewTransfer: (ReviewTransferPayload, TransferType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingsMakeTransferViewModel = koinViewModel(),
) {
    val uiState = viewModel.savingsMakeTransferUiState.collectAsStateWithLifecycle()
    val uiData = viewModel.savingsMakeTransferUiData.collectAsStateWithLifecycle()

    SavingsMakeTransferScreen(
        navigateBack = navigateBack,
        onCancelledClicked = onCancelledClicked,
        uiState = uiState.value,
        uiData = uiData.value,
        modifier = modifier,
        reviewTransfer = { reviewTransfer(it, TransferType.SELF) },
    )
}

@Composable
private fun SavingsMakeTransferScreen(
    uiState: SavingsMakeTransferUiState,
    uiData: SavingsMakeTransferUiData,
    navigateBack: () -> Unit,
    reviewTransfer: (ReviewTransferPayload) -> Unit,
    modifier: Modifier = Modifier,
    onCancelledClicked: () -> Unit = {},
) {


    MifosScaffold(
        topBarTitle= stringResource(if (uiData.transferType == Constants.TRANSFER_PAY_TO) {
            Res.string.deposit
        } else {
            Res.string.transfer
        }),
        backPress = navigateBack,
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                SavingsMakeTransferContent(
                    uiData = uiData,
                    reviewTransfer = reviewTransfer,
                    onCancelledClicked = onCancelledClicked,
                )

                when (uiState) {
                    is SavingsMakeTransferUiState.ShowUI -> Unit

                    is SavingsMakeTransferUiState.Loading -> MifosProgressIndicatorOverlay()

                    is SavingsMakeTransferUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = true,
                            isEmptyData = false,
                            isRetryEnabled = false,
                        )
                    }
                }
            }
        },
    )
}

//
//internal class SavingsMakeTransferUiStatesPreviews :
//    PreviewParameterProvider<SavingsMakeTransferUiState> {
//    override val values: Sequence<SavingsMakeTransferUiState>
//        get() = sequenceOf(
//            SavingsMakeTransferUiState.ShowUI,
//            SavingsMakeTransferUiState.Error(""),
//            SavingsMakeTransferUiState.Loading,
//        )
//}
//
//@DevicePreview
//@Composable
//private fun SavingsMakeTransferContentPreview(
//    @PreviewParameter(SavingsMakeTransferUiStatesPreviews::class)
//    savingsMakeTransferUIState: SavingsMakeTransferUiState,
//) {
//    MifosMobileTheme {
//        SavingsMakeTransferScreen(
//            navigateBack = { },
//            onCancelledClicked = { },
//            reviewTransfer = { },
//            uiState = savingsMakeTransferUIState,
//            uiData = SavingsMakeTransferUiData(),
//        )
//    }
//}
