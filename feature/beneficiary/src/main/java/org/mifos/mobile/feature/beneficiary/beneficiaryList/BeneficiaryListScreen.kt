/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.FloatingActionButtonContent
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.beneficiary.R

@Composable
internal fun BeneficiaryListScreen(
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.beneficiaryListUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    BeneficiaryListScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        addBeneficiaryClicked = addBeneficiaryClicked,
        onBeneficiaryItemClick = onBeneficiaryItemClick,
        retryLoadingBeneficiary = viewModel::loadBeneficiaries,
        isRefreshing = isRefreshing,
        refresh = viewModel::refresh,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BeneficiaryListScreen(
    uiState: BeneficiaryListUiState,
    isRefreshing: Boolean,
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Int) -> Unit,
    retryLoadingBeneficiary: () -> Unit,
    refresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current

    MifosScaffold(
        topBarTitleResId = R.string.beneficiaries,
        navigateBack = navigateBack,
        modifier = modifier,
        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = addBeneficiaryClicked,
            contentColor = MaterialTheme.colorScheme.onBackground,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_white_24dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceBright,
                )
            },
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            PullToRefreshBox(
                state = pullRefreshState,
                onRefresh = refresh,
                isRefreshing = isRefreshing,
            ) {
                when (uiState) {
                    BeneficiaryListUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is BeneficiaryListUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isRetryEnabled = true,
                            onRetry = retryLoadingBeneficiary,
                            message = stringResource(R.string.error_fetching_beneficiaries),
                        )
                    }

                    is BeneficiaryListUiState.Success -> {
                        if (uiState.beneficiaries.isEmpty()) {
                            EmptyDataView(
                                modifier = Modifier.fillMaxSize(),
                                icon = R.drawable.ic_error_black_24dp,
                                error = R.string.no_beneficiary_found_please_add,
                            )
                        } else {
                            ShowBeneficiary(
                                beneficiaryList = uiState.beneficiaries,
                                onClick = onBeneficiaryItemClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

internal class BeneficiaryListScreenPreviewProvider :
    PreviewParameterProvider<BeneficiaryListUiState> {
    private val beneficiaryList = listOf(
        Beneficiary(
            id = 982098302,
            name = "John Doe",
            officeName = "Mifos Head Office",
            clientName = "Jane Smith",
            accountType = null,
            accountNumber = "1234567890",
            transferLimit = 1000.00,
        ),
        Beneficiary(
            id = 982098302,
            name = "Alice Johnson",
            officeName = "Mifos Branch 1",
            clientName = "Bob Smith",
            accountType = null,
            accountNumber = "0987654321",
            transferLimit = 500.00,
        ),
    )
    override val values: Sequence<BeneficiaryListUiState>
        get() = sequenceOf(
            BeneficiaryListUiState.Success(beneficiaryList),
            BeneficiaryListUiState.Loading,
            BeneficiaryListUiState.Error(null),
        )
}

@DevicePreviews
@Composable
private fun PreviewBeneficiaryListScreen(
    @PreviewParameter(BeneficiaryListScreenPreviewProvider::class)
    beneficiaryUiState: BeneficiaryListUiState,
) {
    MifosMobileTheme {
        BeneficiaryListScreen(
            uiState = beneficiaryUiState,
            navigateBack = {},
            addBeneficiaryClicked = {},
            onBeneficiaryItemClick = { },
            isRefreshing = false,
            retryLoadingBeneficiary = {},
            refresh = {},
        )
    }
}
