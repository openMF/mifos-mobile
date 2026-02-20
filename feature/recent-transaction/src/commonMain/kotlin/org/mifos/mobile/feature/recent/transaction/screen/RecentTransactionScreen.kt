/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mifos_mobile.feature.recent_transaction.generated.resources.Res
import mifos_mobile.feature.recent_transaction.generated.resources.account_number_label
import mifos_mobile.feature.recent_transaction.generated.resources.all
import mifos_mobile.feature.recent_transaction.generated.resources.apply_filters
import mifos_mobile.feature.recent_transaction.generated.resources.balance
import mifos_mobile.feature.recent_transaction.generated.resources.clear_all
import mifos_mobile.feature.recent_transaction.generated.resources.credits
import mifos_mobile.feature.recent_transaction.generated.resources.debits
import mifos_mobile.feature.recent_transaction.generated.resources.filter
import mifos_mobile.feature.recent_transaction.generated.resources.filter_by_account
import mifos_mobile.feature.recent_transaction.generated.resources.filter_transactions
import mifos_mobile.feature.recent_transaction.generated.resources.no_transaction
import mifos_mobile.feature.recent_transaction.generated.resources.recent_transactions
import mifos_mobile.feature.recent_transaction.generated.resources.select_account
import mifos_mobile.feature.recent_transaction.generated.resources.transaction_type
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.rememberMifosPullToRefreshState
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.TransactionScreenItem
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.recent.transaction.viewmodel.RecentTransactionAction
import org.mifos.mobile.feature.recent.transaction.viewmodel.RecentTransactionEvent
import org.mifos.mobile.feature.recent.transaction.viewmodel.RecentTransactionUiState
import org.mifos.mobile.feature.recent.transaction.viewmodel.RecentTransactionViewModel
import org.mifos.mobile.feature.recent.transaction.viewmodel.TransactionFilterType
import template.core.base.designsystem.theme.KptTheme
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Main entry point for the Recent Transaction screen.
 * * Orchestrates state observation from [RecentTransactionViewModel], navigation
 * event handling, and the visibility of modal components.
 *
 * @param navigateBack Callback to return to the previous screen.
 * @param navigateToDetails Callback to navigate to a specific transaction detail.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecentTransactionScreen(
    navigateBack: () -> Unit,
    navigateToDetails: (String, String, Long) -> Unit,
    viewModel: RecentTransactionViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is RecentTransactionEvent.NavigateToDetails -> {
                navigateToDetails(event.transactionId, event.accountType, event.accountId)
            }

            is RecentTransactionEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    RecentTransactionScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    RecentTransactionScreenDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        sheetState = sheetState,
    )
}

/**
 * Core UI layout for the Recent Transaction screen.
 *
 * Displays a grouped list of transactions with support for pull-to-refresh,
 * empty/error states, and filter triggering.
 *
 * @param state The current [RecentTransactionUiState] to be rendered.
 * @param onAction Callback to propagate UI interactions to the ViewModel.
 */
@Composable
internal fun RecentTransactionScreenContent(
    state: RecentTransactionUiState,
    onAction: (RecentTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullToRefreshState = rememberMifosPullToRefreshState(
        isEnabled = true,
        isRefreshing = state.isRefreshing,
        onRefresh = {
            onAction(RecentTransactionAction.Refresh)
        },
    )

    MifosElevatedScaffold(
        onNavigateBack = {
            onAction(RecentTransactionAction.OnNavigateBackClick)
        },
        topBarTitle = stringResource(Res.string.recent_transactions),
        pullToRefreshState = pullToRefreshState,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(KptTheme.spacing.md),
        ) {
            if (state.viewState != ScreenUiState.Loading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = state.selectedAccount?.accountNo.orEmpty(),
                        style = MifosTypography.bodyMedium,
                        modifier = Modifier.padding(vertical = DesignToken.padding.medium),
                    )

                    IconButton(
                        onClick = { onAction(RecentTransactionAction.ToggleFilter) },
                    ) {
                        Icon(
                            imageVector = MifosIcons.Filter,
                            contentDescription = stringResource(Res.string.filter),
                            tint =
                            if (state.filterType != TransactionFilterType.ALL) {
                                KptTheme.colorScheme.primary
                            } else {
                                KptTheme.colorScheme.onSurface
                            },
                        )
                    }
                }
            }

            when (state.viewState) {
                ScreenUiState.Empty -> {
                    EmptyDataView(
                        error = Res.string.no_transaction,
                        icon = MifosIcons.Info,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is ScreenUiState.Error -> {
                    MifosErrorComponent(
                        isRetryEnabled = true,
                        message = stringResource(state.viewState.message),
                        onRetry = { onAction(RecentTransactionAction.Refresh) },
                    )
                }

                ScreenUiState.Loading -> MifosProgressIndicator()

                ScreenUiState.Network -> {
                    MifosErrorComponent(
                        isNetworkConnected = state.networkStatus,
                        isRetryEnabled = true,
                        onRetry = { onAction(RecentTransactionAction.Refresh) },
                    )
                }

                ScreenUiState.Success -> {
                    LazyColumn {
                        state.groupedTransactions.forEach { (date, transactions) ->
                            item(key = date) {
                                Text(
                                    text = date,
                                    style = MifosTypography.labelLargeEmphasized,
                                    modifier = Modifier.padding(vertical = DesignToken.padding.medium),
                                )
                            }

                            items(
                                items = transactions,
                                key = { transaction ->
                                    transaction.id
                                        ?: "${date}_${transaction.amount}_${transaction.typeValue}"
                                },
                            ) { transaction ->
                                TransactionScreenItem(
                                    title = transaction.typeValue.orEmpty(),
                                    date = DateHelper.getDateAsString(transaction.date),
                                    time = "",
                                    transactionAmount = CurrencyFormatter.format(
                                        balance = transaction.amount,
                                        currencyCode = transaction.currency,
                                        maximumFractionDigits = 3,
                                    ),
                                    isCredited = transaction.isCredit,
                                    onClick = {
                                        transaction.id?.let {
                                            onAction(
                                                RecentTransactionAction.OnTransactionClick(it),
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }
                }

                else -> Unit
            }
        }
    }
}

/**
 * Composable function for the Account Transactions Dialog.
 *
 * @param state The current state of the Account Transactions Screen.
 * @param onAction The function to be called when an action is performed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecentTransactionScreenDialog(
    state: RecentTransactionUiState,
    onAction: (RecentTransactionAction) -> Unit,
    sheetState: SheetState,
) {
    when (state.dialogState) {
        RecentTransactionUiState.DialogState.Filters -> {
            ModalBottomSheet(
                onDismissRequest = { onAction(RecentTransactionAction.DismissFilter) },
                sheetState = sheetState,
                containerColor = KptTheme.colorScheme.surface,
            ) {
                TransactionFilterSheetContent(
                    state = state,
                    onAction = onAction,
                )
            }
        }

        null -> Unit
    }
}

/**
 * The Filter Sheet Content matching the UI design:
 * - Header with "Clear All"
 * - Card-style Account Selector
 * - Chip-style Type Selector (All/Debit/Credit)
 * - Large "Apply Filters" button
 */
@Composable
internal fun TransactionFilterSheetContent(
    state: RecentTransactionUiState,
    onAction: (RecentTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedAccount by rememberSaveable { mutableStateOf(state.selectedAccount) }
    var selectedType by rememberSaveable { mutableStateOf(state.filterType) }
    var isAccountDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(KptTheme.spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.filter_transactions),
                style = KptTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                ),
            )
            TextButton(
                enabled = state.hasActiveFilters,
                onClick = {
                    onAction(RecentTransactionAction.ClearFilter)
                },
            ) {
                Text(
                    text = stringResource(Res.string.clear_all),
                    style = KptTheme.typography.bodyMedium.copy(
                        color = KptTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = KptTheme.spacing.sm))

        Text(
            text = stringResource(Res.string.filter_by_account),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(vertical = DesignToken.padding.medium),
        )

        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isAccountDropdownExpanded = true },
                shape = KptTheme.shapes.medium,
                border = BorderStroke(DesignToken.strokes.thin, Color.Gray.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(
                    containerColor = KptTheme.colorScheme.surface,
                    contentColor = KptTheme.colorScheme.onSurface,
                ),
                elevation = CardDefaults.cardElevation(KptTheme.elevation.level0),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(KptTheme.spacing.md),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = stringResource(
                                Res.string.account_number_label,
                                selectedAccount?.accountNo
                                    ?: stringResource(Res.string.select_account),
                            ),
                            style = KptTheme.typography.bodyLarge
                                .copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(modifier = Modifier.height(KptTheme.spacing.xs))
                        Text(
                            text = stringResource(
                                Res.string.balance,
                                selectedAccount?.accountBalance?.toString() ?: "0.0",
                                selectedAccount?.currency?.code.orEmpty(),
                            ),
                            style = KptTheme.typography.bodySmall.copy(color = Color.Gray),
                        )
                    }
                    Icon(
                        imageVector = MifosIcons.ArrowDropDown,
                        contentDescription = stringResource(Res.string.select_account),
                        tint = KptTheme.colorScheme.onSurface,
                    )
                }
            }

            DropdownMenu(
                expanded = isAccountDropdownExpanded,
                onDismissRequest = { isAccountDropdownExpanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f),
            ) {
                state.accounts.forEach { account ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = account.productName ?: stringResource(Res.string.select_account),
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(text = account.accountNo ?: "", style = KptTheme.typography.bodySmall)
                            }
                        },
                        onClick = {
                            selectedAccount = account
                            isAccountDropdownExpanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))

        Text(
            text = stringResource(Res.string.transaction_type),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(bottom = DesignToken.padding.medium),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            FilterOptionChip(
                label = stringResource(Res.string.all),
                isSelected = selectedType == TransactionFilterType.ALL,
                onClick = { selectedType = TransactionFilterType.ALL },
                modifier = Modifier.weight(1f),
            )
            FilterOptionChip(
                label = stringResource(Res.string.debits),
                isSelected = selectedType == TransactionFilterType.DEBIT,
                onClick = { selectedType = TransactionFilterType.DEBIT },
                modifier = Modifier.weight(1f),
            )
            FilterOptionChip(
                label = stringResource(Res.string.credits),
                isSelected = selectedType == TransactionFilterType.CREDIT,
                onClick = { selectedType = TransactionFilterType.CREDIT },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.dp40))

        Button(
            onClick = {
                if (selectedAccount != null) {
                    onAction(RecentTransactionAction.ApplyFilter(selectedAccount!!, selectedType))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.buttonDp50),
            shape = DesignToken.shapes.dp25,
            colors = ButtonDefaults.buttonColors(
                containerColor = KptTheme.colorScheme.primary,
            ),
        ) {
            Text(
                text = stringResource(Res.string.apply_filters),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.dp24))
    }
}

/**
 * A specialized selection chip used for toggling filter categories.
 *
 * @param label The text displayed on the chip.
 * @param isSelected Whether the chip is currently in the active state.
 * @param onClick Triggered when the user interacts with the chip.
 */
@Composable
fun FilterOptionChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(DesignToken.sizes.surfaceDp40),
        shape = DesignToken.shapes.largeIncreased,
        color = if (isSelected) KptTheme.colorScheme.primary else KptTheme.colorScheme.surface,
        border = if (!isSelected) {
            BorderStroke(
                DesignToken.strokes.thin,
                KptTheme.colorScheme.outline.copy(alpha = 0.5f),
            )
        } else {
            null
        },
        onClick = onClick,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = label,
                color = if (isSelected) KptTheme.colorScheme.onPrimary else KptTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}
