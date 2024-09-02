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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
internal fun ThirdPartyTransferScreen(
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    reviewTransfer: (ReviewTransferPayload, TransferType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ThirdPartyTransferViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiData by viewModel.thirdPartyTransferUiData.collectAsStateWithLifecycle()

    ThirdPartyTransferScreen(
        uiState = uiState,
        uiData = uiData,
        navigateBack = navigateBack,
        addBeneficiary = addBeneficiary,
        reviewTransfer = { reviewTransfer(it, TransferType.TPT) },
        modifier = modifier,
    )
}

@Composable
private fun ThirdPartyTransferScreen(
    uiState: ThirdPartyTransferUiState,
    uiData: ThirdPartyTransferUiData,
    navigateBack: () -> Unit,
    addBeneficiary: () -> Unit,
    reviewTransfer: (ReviewTransferPayload) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    MifosScaffold(
        topBarTitleResId = R.string.third_party_transfer,
        navigateBack = navigateBack,
        modifier = modifier,
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                when (uiState) {
                    is ThirdPartyTransferUiState.ShowUI -> {
                        ThirdPartyTransferContent(
                            accountOption = uiData.fromAccountDetail,
                            toAccountOption = uiData.toAccountOption,
                            beneficiaryList = uiData.beneficiaries,
                            navigateBack = navigateBack,
                            addBeneficiary = addBeneficiary,
                            reviewTransfer = reviewTransfer,
                        )
                    }

                    is ThirdPartyTransferUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is ThirdPartyTransferUiState.Error -> {
                        MifosErrorComponent(
                            message = uiState.errorMessage
                                ?: stringResource(id = R.string.error_fetching_third_party_transfer_template),
                            isNetworkConnected = Network.isConnected(context),
                        )
                    }
                }
            }
        },
    )
}

internal class SavingsMakeTransferUiStatesPreviews :
    PreviewParameterProvider<ThirdPartyTransferUiState> {
    override val values: Sequence<ThirdPartyTransferUiState>
        get() = sequenceOf(
            ThirdPartyTransferUiState.ShowUI,
            ThirdPartyTransferUiState.Error(""),
            ThirdPartyTransferUiState.Loading,
        )
}

@DevicePreviews
@Composable
private fun ThirdPartyTransferScreenPreview(
    @PreviewParameter(SavingsMakeTransferUiStatesPreviews::class)
    thirdPartyTransferUiState: ThirdPartyTransferUiState,
) {
    MifosMobileTheme {
        ThirdPartyTransferScreen(
            uiState = thirdPartyTransferUiState,
            uiData = ThirdPartyTransferUiData(),
            navigateBack = {},
            addBeneficiary = {},
            reviewTransfer = {},
        )
    }
}
