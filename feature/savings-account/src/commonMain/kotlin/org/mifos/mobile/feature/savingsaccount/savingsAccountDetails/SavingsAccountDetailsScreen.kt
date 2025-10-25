/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_update
import mifos_mobile.feature.savings_account.generated.resources.feature_account_details_top_bar_title
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_status_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.component.MifosActionCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosLabelValueCard
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.savingsaccount.components.SavingsActionItems
import org.mifos.mobile.feature.savingsaccount.components.savingsAccountActions

@Composable
internal fun SavingsAccountDetailsScreen(
    navigateBack: () -> Unit,
    navigateToTransferScreen: (Long) -> Unit,
    navigateToUpdateScreen: (Long, String?, String?, String?, String?) -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToSavingsAccountTransactionScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
    viewModel: SavingsAccountDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            SavingsAccountDetailsEvent.NavigateBack -> navigateBack.invoke()

            is SavingsAccountDetailsEvent.NavigateToAction -> {
                when {
                    event.route == Constants.CHARGES -> {
                        navigateToClientChargeScreen(ChargeType.SAVINGS.name, uiState.accountId)
                    }

                    event.route == Constants.TRANSFER -> {
                        navigateToTransferScreen(uiState.accountId)
                    }

                    event.route == Constants.TRANSACTIONS -> {
                        navigateToSavingsAccountTransactionScreen(uiState.accountId)
                    }

                    event.route == Constants.QR_CODE -> {
                        navigateToQrCodeScreen(viewModel.getQrString())
                    }
                }
            }

            SavingsAccountDetailsEvent.UpdateAccount -> {
                navigateToUpdateScreen.invoke(
                    uiState.accountId,
                    uiState.accountNumber,
                    uiState.clientName,
                    uiState.submissionDate,
                    uiState.product,
                )
            }
        }
    }

    SavingsAccountDetailsContent(
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun SavingsAccountDetailsContent(
    state: SavingsAccountDetailsState,
    onAction: (SavingsAccountDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(SavingsAccountDetailsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_account_details_top_bar_title),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(SavingsAccountDetailsAction.OnRetry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(SavingsAccountDetailsAction.OnRetry) },
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
                    ActionBar(
                        isUpdatable = state.isUpdatable,
                        onAction = onAction,
                    )

                    AccountDetailsGrid(
                        details = state.displayItems,
                        isActive = state.isActive,
                    )

                    if (state.transactionList.isNotEmpty()) {
                        AccountDetailsGrid(
                            label = "Last Transactions",
                            details = state.transactionList,
                            isActive = state.isActive,
                        )
                    }

                    val visibleActions = state.savingStatus?.allowedActions ?: emptySet()

                    SavingsAccountActions(
                        visibleActions = visibleActions,
                        onActionClick = {
                            onAction(SavingsAccountDetailsAction.OnNavigateToAction(it))
                        },
                    )
                }
            }
            else -> { }
        }
    }
}

@Composable
internal fun ActionBar(
    onAction: (SavingsAccountDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
    isUpdatable: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = DesignToken.padding.medium),
        horizontalArrangement = Arrangement.End,
    ) {
        Row(
            modifier = Modifier.clickable(isUpdatable) {
                onAction(SavingsAccountDetailsAction.OnUpdateAccount)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            Text(
                text = stringResource(Res.string.feature_account_action_update),
                color = if (isUpdatable) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.inversePrimary
                },
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.EditRegular,
                contentDescription = null,
                tint = if (isUpdatable) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.inversePrimary
                },

            )
        }
    }
}

@Composable
internal fun AccountDetailsGrid(
    label: String? = null,
    details: List<LabelValueItem>? = emptyList(),
    isActive: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
    ) {
        if (label != null) {
            Text(
                text = label,
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
                            .height(64.dp)
                            .weight(1f),
                        label = stringResource(item.label),
                        value = item.value,
                        color = if (isActive && item.label == Res.string.feature_savings_status_label) {
                            AppColors
                                .customEnable
                        } else {
                            MaterialTheme
                                .colorScheme.onBackground
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun SavingsAccountActions(
    visibleActions: Set<SavingsActionItems>,
    onActionClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
    ) {
        Text(
            text = "Actions",
            style = MifosTypography.labelLargeEmphasized,
            color = MaterialTheme.colorScheme.onSurface,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            visibleActions.forEach { item ->
                MifosActionCard(
                    title = item.title,
                    subTitle = item.subTitle,
                    icon = item.icon,
                    onClick = {
                        onActionClick(item.route)
                    },
                )
            }
        }
    }
}

data class LabelValueItem(
    val label: StringResource,
    val value: String,
)

@Preview
@Composable
private fun Account_Details_Overview() {
    MifosMobileTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SavingsAccountDetailsContent(
                state = SavingsAccountDetailsState(
                    items = savingsAccountActions,
                    dialogState = null,
                ),
                onAction = {},
            )
        }
    }
}
