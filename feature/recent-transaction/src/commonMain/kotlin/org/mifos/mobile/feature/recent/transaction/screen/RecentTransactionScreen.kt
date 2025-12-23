/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionAction
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionEvent
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionUiState
import org.mifos.mobile.feature.recent.transaction.utils.TransactionFilterType
import org.mifos.mobile.feature.recent.transaction.viewmodel.RecentTransactionViewModel
import template.core.base.designsystem.theme.KptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentTransactionScreen(
    navigateBack: () -> Unit,
    navigateToDetails: (String, String, Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecentTransactionViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is RecentTransactionEvent.NavigateToDetails -> {
                    navigateToDetails(event.transactionId, event.accountType, event.accountId)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    imageVector = MifosIcons.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }

                            Text(
                                text = "Transaction History",
                                style = KptTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(end = KptTheme.spacing.sm),
                            )
                        }
                        state.selectedAccount?.let { account ->
                            Text(
                                text = account.accountNo ?: "Selected Account",
                                style = KptTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = KptTheme.colorScheme.onSurfaceVariant,

                            )
                        }
                    }
                },

                actions = {
                    IconButton(onClick = {
                        viewModel.handleAction(RecentTransactionAction.ToggleFilter)
                    }) {
                        Icon(
                            imageVector = MifosIcons.Filter,
                            contentDescription = "Filter",
                            tint =
                            if (
                                state.filterType != TransactionFilterType.ALL
                            ) {
                                KptTheme.colorScheme.primary
                            } else {
                                KptTheme.colorScheme.onSurface
                            },
                        )
                    }
                },
            )
        },
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            when (val viewState = state.viewState) {
                is RecentTransactionUiState.ViewState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is RecentTransactionUiState.ViewState.Error -> {
                    ErrorScreen(
                        message = viewState.message ?: "Unknown Error",
                        onRetry = { viewModel.handleAction(RecentTransactionAction.LoadInitial) },
                    )
                }
                is RecentTransactionUiState.ViewState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No recent transactions found.")
                    }
                }
                is RecentTransactionUiState.ViewState.Content -> {
                    TransactionList(
                        transactions = viewState.list,
                        onTransactionClick = { transaction ->
                            viewModel.handleAction(RecentTransactionAction.OnTransactionClick(transaction))
                        },
                    )
                }
            }
        }

        if (state.showFilter) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.handleAction(RecentTransactionAction.ToggleFilter) },
                sheetState = sheetState,
                containerColor = KptTheme.colorScheme.surface,
            ) {
                TransactionFilterSheetContent(
                    accounts = state.accounts,
                    currentAccount = state.selectedAccount,
                    currentFilterType = state.filterType,
                    onApply = { account, type ->
                        viewModel.handleAction(RecentTransactionAction.ApplyFilter(account, type))
                    },
                    onClear = {
                        viewModel.handleAction(RecentTransactionAction.ClearFilter)
                    },
                )
            }
        }
    }
}

/**
 * NEW: The redesigned TransactionItem composable matching the image.
 */
@Composable
fun TransactionItem(
    transaction: Transactions,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val isCredit = transaction.transactionType?.deposit == true
    val amountColor = if (isCredit) Color(0xFF0A7E48) else Color.Red

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = KptTheme.spacing.md, vertical = DesignToken.padding.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
        ) {
            Text(
                text = if (isCredit) "CREDIT" else "DEBIT",
                style = KptTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = KptTheme.colorScheme.onSurface,
            )
            Text(
                text = formatDate(transaction.date),
                style = KptTheme.typography.bodyMedium,
                color = KptTheme.colorScheme.onSurfaceVariant,
            )
        }

        Text(
            text = "${if (isCredit) "+ " else "- "}${CurrencyFormatter.format(
                transaction.amount ?: 0.0,
                transaction.currency?.code ?: "USD",
                8,
            )}",
            style = KptTheme.typography.bodyLarge.copy(
                color = amountColor,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

/**
 * Helper to format date list e.g. [2025, 9, 15] into "15 Sep 2025"
 */
private fun formatDate(dateList: List<Int>?): String {
    if (dateList == null || dateList.size < 3) return "Invalid Date"
    val year = dateList[0]
    val month = when (dateList[1]) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> ""
    }
    val day = dateList[2]
    return "$day $month $year"
}

@Composable
fun TransactionList(
    transactions: List<Transactions>,
    modifier: Modifier = Modifier,
    onTransactionClick: (Transactions) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        itemsIndexed(
            items = transactions,
        ) { index, transaction ->

            TransactionItem(
                transaction = transaction,
                onClick = { onTransactionClick(transaction) },
            )

            if (index != transactions.size - 1) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = KptTheme.spacing.md),
                    color = KptTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = message, color = KptTheme.colorScheme.error)
        Button(onClick = onRetry) {
            Text("Retry")
        }
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
fun TransactionFilterSheetContent(
    accounts: List<SavingAccount>,
    currentAccount: SavingAccount?,
    currentFilterType: TransactionFilterType,
    onApply: (SavingAccount, TransactionFilterType) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedAccount by remember { mutableStateOf(currentAccount ?: accounts.firstOrNull()) }
    var selectedType by remember { mutableStateOf(currentFilterType) }
    var isAccountDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DesignToken.spacing.dp24, vertical = KptTheme.spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Filter Transactions",
                style = KptTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                ),
            )
            TextButton(onClick = onClear) {
                Text(
                    text = "Clear All",
                    style = KptTheme.typography.bodyMedium.copy(
                        color = KptTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = KptTheme.spacing.sm))

        Text(
            text = "Filter By Account :",
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                            text = "A/c No: ${selectedAccount?.accountNo ?: "Select Account"}",
                            style = KptTheme.typography.bodyLarge
                                .copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(modifier = Modifier.height(KptTheme.spacing.xs))
                        Text(
                            text = "Balance: ${selectedAccount?.accountBalance ?: "0.0"} " +
                                (selectedAccount?.currency?.code ?: ""),
                            style = KptTheme.typography.bodySmall.copy(color = Color.Gray),
                        )
                    }
                    Icon(
                        imageVector = MifosIcons.ArrowDropDown,
                        contentDescription = "Select Account",
                        tint = Color.Black,
                    )
                }
            }

            DropdownMenu(
                expanded = isAccountDropdownExpanded,
                onDismissRequest = { isAccountDropdownExpanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White),
            ) {
                accounts.forEach { account ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(text = account.productName ?: "Account", fontWeight = FontWeight.Bold)
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
            text = "Transaction Type:",
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(bottom = DesignToken.padding.medium),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            FilterOptionChip(
                label = "All",
                isSelected = selectedType == TransactionFilterType.ALL,
                onClick = { selectedType = TransactionFilterType.ALL },
                modifier = Modifier.weight(1f),
            )
            FilterOptionChip(
                label = "Debits",
                isSelected = selectedType == TransactionFilterType.DEBIT,
                onClick = { selectedType = TransactionFilterType.DEBIT },
                modifier = Modifier.weight(1f),
            )
            FilterOptionChip(
                label = "Credits",
                isSelected = selectedType == TransactionFilterType.CREDIT,
                onClick = { selectedType = TransactionFilterType.CREDIT },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.dp40))

        Button(
            onClick = {
                if (selectedAccount != null) {
                    onApply(selectedAccount!!, selectedType)
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
                text = "Apply Filters",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.dp24))
    }
}

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
