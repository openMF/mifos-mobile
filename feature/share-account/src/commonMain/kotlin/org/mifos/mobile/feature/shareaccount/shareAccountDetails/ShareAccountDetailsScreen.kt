/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.shareAccountDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_details_action
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_details_top_bar_title
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_status
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.LoanStatus
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.component.MifosActionCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosLabelValueCard
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.shareaccount.component.ShareActionItems
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun ShareAccountDetailsScreen(
    navigateBack: () -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToShareAccountTransactionScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
    viewModel: ShareAccountDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            ShareAccountDetailsEvent.NavigateBack -> navigateBack.invoke()

            is ShareAccountDetailsEvent.NavigateToAction -> {
                when (event.route) {
                    Constants.CHARGES -> navigateToClientChargeScreen(
                        ChargeType.SHARE.name,
                        uiState.accountId,
                    )
                    Constants.TRANSACTIONS -> navigateToShareAccountTransactionScreen(
                        uiState.accountId,
                    )
                    Constants.QR_CODE -> navigateToQrCodeScreen(
                        viewModel.getQrString(),
                    )
                }
            }
        }
    }

    ShareAccountDetailsContent(
        state = uiState,
        onAction = remember(viewModel) { { viewModel.trySendAction(it) } },
    )
}

@Composable
internal fun ShareAccountDetailsContent(
    state: ShareAccountDetailsState,
    onAction: (ShareAccountDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(ShareAccountDetailsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_share_account_details_top_bar_title),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier.fillMaxWidth().navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(ShareAccountDetailsAction.OnRetry) },
                )
            }
            ScreenUiState.Loading -> MifosProgressIndicator()
            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(ShareAccountDetailsAction.OnRetry) },
                )
            }
            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(DesignToken.padding.large),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
                ) {
                    AccountDetailsGrid(
                        details = state.displayItems,
                    )

                    ShareAccountActions(
                        visibleActions = state.allowedActions,
                        onActionClick = { onAction(ShareAccountDetailsAction.OnNavigateToAction(it)) },
                    )
                }
            }
            else -> { }
        }
    }
}

@Composable
internal fun AccountDetailsGrid(
    label: StringResource? = null,
    details: List<LabelValueItem>? = emptyList(),
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
    ) {
        if (label != null) {
            Text(
                text = stringResource(label),
                style = MifosTypography.labelLargeEmphasized,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        if (details != null) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                maxItemsInEachRow = 2,
            ) {
                details.forEach { item ->
                    MifosLabelValueCard(
                        modifier = Modifier
                            .height(DesignToken.sizes.cardDp64)
                            .weight(1f),
                        label = stringResource(item.label),
                        value = item.value,
                        color = if (item.label == Res.string.feature_share_account_status) {
                            when (item.value) {
                                LoanStatus.ACTIVE.status -> AppColors.customEnable
                                LoanStatus.SUBMIT_AND_PENDING_APPROVAL.status -> AppColors.customYellow
                                LoanStatus.WITHDRAWN.status, LoanStatus.MATURED.status ->
                                    KptTheme.colorScheme.error
                                else -> KptTheme.colorScheme.secondary
                            }
                        } else {
                            KptTheme.colorScheme.secondary
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun ShareAccountActions(
    visibleActions: Set<ShareActionItems>,
    onActionClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large)) {
        Text(
            text = stringResource(Res.string.feature_share_account_details_action),
            style = MifosTypography.labelLargeEmphasized,
            color = MaterialTheme.colorScheme.onSurface,
        )
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            visibleActions
                .toList()
                .sortedBy { it.route }
                .forEach { item ->
                    MifosActionCard(
                        title = item.title,
                        subTitle = item.subTitle,
                        icon = item.icon,
                        onClick = { onActionClick(item.route) },
                    )
                }
        }
    }
}

data class LabelValueItem(
    val label: StringResource,
    val value: String,
)
