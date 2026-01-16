/*
 * Copyright 2026 Mifos Initiative
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.mifos.mobile.core.model.SavingStatus
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
import template.core.base.designsystem.theme.KptTheme

/**
 * A stateful composable that serves as the entry point for the "Savings Account Details" screen.
 *
 * This function connects to the [SavingsAccountDetailsViewModel] to observe UI state and handle
 * one-time events. It is responsible for orchestrating navigation to other screens based on
 * user actions and ViewModel events.
 *
 * @param navigateBack A lambda function to handle back navigation events.
 * @param navigateToTransferScreen A lambda to navigate to the fund transfer screen.
 * @param navigateToUpdateScreen A lambda to navigate to the account update screen (e.g., for deposits).
 * @param navigateToClientChargeScreen A lambda to navigate to the screen showing charges for the account.
 * @param navigateToSavingsAccountTransactionScreen A lambda to navigate to the transaction history screen.
 * @param navigateToQrCodeScreen A lambda to navigate to the QR code display screen.
 * @param viewModel The ViewModel responsible for the screen's logic and state, typically provided by Koin.
 */
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

/**
 * A stateless composable that renders the main UI for the "Savings Account Details" screen.
 *
 * It conditionally displays content based on the [ScreenUiState] (e.g., loading, error, success)
 * and arranges the various UI components like the action bar, details grid, and action cards.
 *
 * @param state The current [SavingsAccountDetailsState] to render.
 * @param onAction A callback to send actions (like retry or navigate back) to the ViewModel.
 * @param modifier The [Modifier] to be applied to the layout.
 */
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
                        .padding(KptTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
                ) {
                    ActionBar(
                        isUpdatable = state.isUpdatable,
                        onAction = onAction,
                    )

                    AccountDetailsGrid(
                        details = state.displayItems,
                    )

                    if (state.transactionList.isNotEmpty()) {
                        AccountDetailsGrid(
                            label = "Last Transactions",
                            details = state.transactionList,
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

/**
 * Renders an action bar, currently containing an "Update" action.
 *
 * The action's appearance and clickability are determined by the [isUpdatable] flag.
 *
 * @param onAction A callback to send the [SavingsAccountDetailsAction.OnUpdateAccount] action.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param isUpdatable A boolean indicating if the update action should be enabled.
 */
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
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
        ) {
            Text(
                text = stringResource(Res.string.feature_account_action_update),
                color = if (isUpdatable) {
                    KptTheme.colorScheme.primary
                } else {
                    KptTheme.colorScheme.inversePrimary
                },
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.EditRegular,
                contentDescription = null,
                tint = if (isUpdatable) {
                    KptTheme.colorScheme.primary
                } else {
                    KptTheme.colorScheme.inversePrimary
                },

            )
        }
    }
}

/**
 * Renders a grid of account details using [MifosLabelValueCard]s.
 *
 * The layout uses [FlowRow] to wrap items into a new row if they exceed the screen width,
 * with a maximum of two items per row. It can also display an optional section label.
 *
 * @param label An optional string for the section's title.
 * @param details A list of [LabelValueItem]s to display in the grid.
 */
@Composable
internal fun AccountDetailsGrid(
    label: String? = null,
    details: List<LabelValueItem>? = emptyList(),
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
                color = KptTheme.colorScheme.onSurface,
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
                        color = if (item.label == Res.string.feature_savings_status_label) {
                            when (item.value) {
                                SavingStatus.ACTIVE.status -> AppColors.customEnable
                                SavingStatus.SUBMIT_AND_PENDING_APPROVAL.status -> AppColors.customYellow
                                SavingStatus.INACTIVE.status -> KptTheme.colorScheme.error
                                else -> KptTheme.colorScheme.onSurface
                            }
                        } else {
                            KptTheme.colorScheme.onBackground
                        },
                    )
                }
            }
        }
    }
}

/**
 * Renders a section with a list of actionable cards for the savings account.
 *
 * It filters the available actions based on the [visibleActions] set and displays them
 * using [MifosActionCard].
 *
 * @param visibleActions A set of [SavingsActionItems] that should be displayed.
 * @param onActionClick A callback invoked with the route string of the clicked action.
 */
@Composable
internal fun SavingsAccountActions(
    visibleActions: Set<SavingsActionItems>,
    onActionClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
    ) {
        Text(
            text = "Actions",
            style = MifosTypography.labelLargeEmphasized,
            color = KptTheme.colorScheme.onSurface,
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

/**
 * A data class to represent a simple key-value pair for display in the UI.
 *
 * @property label The string resource for the label (key).
 * @property value The string value to be displayed.
 */
data class LabelValueItem(
    val label: StringResource,
    val value: String,
)

/**
 * A Jetpack Compose preview for the [SavingsAccountDetailsContent].
 *
 * This provides a design-time visualization of the account details screen in Android Studio,
 * configured with a default success state.
 */
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
