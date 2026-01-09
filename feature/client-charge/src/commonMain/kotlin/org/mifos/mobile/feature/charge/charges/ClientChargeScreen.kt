/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.charges

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.account_label
import mifos_mobile.feature.client_charge.generated.resources.account_type
import mifos_mobile.feature.client_charge.generated.resources.account_type_loan
import mifos_mobile.feature.client_charge.generated.resources.account_type_savings
import mifos_mobile.feature.client_charge.generated.resources.account_type_shares
import mifos_mobile.feature.client_charge.generated.resources.all_accounts
import mifos_mobile.feature.client_charge.generated.resources.apply_filters
import mifos_mobile.feature.client_charge.generated.resources.charge_status
import mifos_mobile.feature.client_charge.generated.resources.clear_all
import mifos_mobile.feature.client_charge.generated.resources.database_warning
import mifos_mobile.feature.client_charge.generated.resources.error_no_charge
import mifos_mobile.feature.client_charge.generated.resources.filter_charges
import mifos_mobile.feature.client_charge.generated.resources.select_account
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.charge.components.ChargeFilterUtil
import org.mifos.mobile.feature.charge.components.ClientChargeItem
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun ClientChargeScreen(
    navigateBack: () -> Unit,
    onChargeClick: (charge: Charge) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClientChargeViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ClientChargeEvent.Navigate -> navigateBack.invoke()

            is ClientChargeEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is ClientChargeEvent.OnChargeClick -> {
                onChargeClick(event.charge)
            }
        }
    }
    ClientChargeScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ClientChargeDialogs(
        dialogState = state.dialogState,
        onDismissRequest = remember(viewModel) {
            { viewModel.trySendAction(ClientChargeAction.OnDismissDialog) }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientChargeScreen(
    state: ClientChargeState,
    modifier: Modifier = Modifier,
    onAction: (ClientChargeAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(state.topBarTitleResId),
                            style = KptTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        state.selectedAccountNo?.let { accountNo ->
                            Text(
                                text = accountNo,
                                style = KptTheme.typography.bodySmall,
                                color = KptTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ClientChargeAction.OnNavigate) }) {
                        Icon(imageVector = MifosIcons.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(ClientChargeAction.ToggleFilter) }) {
                        Icon(
                            imageVector = MifosIcons.Filter,
                            contentDescription = "Filter",
                            tint = if (
                                state.activeFilter != ChargeFilterUtil.ALL || state.selectedAccountNo != null
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
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state.uiState) {
                ScreenUiState.Empty -> {
                    EmptyDataView(
                        modifier = Modifier.fillMaxSize(),
                        image = Res.drawable.database_warning,
                        error = Res.string.error_no_charge,
                    )
                }

                is ScreenUiState.Error -> {
                    MifosErrorComponent(
                        isRetryEnabled = true,
                        message = stringResource(state.uiState.message),
                        onRetry = { onAction(ClientChargeAction.Retry) },
                    )
                }

                ScreenUiState.Loading -> MifosProgressIndicator()

                ScreenUiState.Network -> {
                    MifosErrorComponent(
                        isNetworkConnected = state.networkStatus,
                        isRetryEnabled = true,
                        onRetry = { onAction(ClientChargeAction.Retry) },
                    )
                }

                ScreenUiState.Success -> {
                    ClientChargeContent(
                        modifier = Modifier.padding(DesignToken.padding.large),
                        chargesList = state.charges,
                        onChargeClick = {
                            onAction(ClientChargeAction.OnChargeClick(it))
                        },
                    )
                }
                else -> { }
            }
        }

        if (state.showFilter) {
            ModalBottomSheet(
                onDismissRequest = { onAction(ClientChargeAction.ToggleFilter) },
                sheetState = sheetState,
                containerColor = KptTheme.colorScheme.surface,
            ) {
                ChargeFilterSheetContent(
                    state = state,
                    onApply = { accountObj, type, filter ->
                        onAction(
                            ClientChargeAction.ApplyFilter(
                                accountObj,
                                type,
                                filter,
                            ),
                        )
                    },
                    onClear = {
                        onAction(ClientChargeAction.ClearFilter)
                    },
                )
            }
        }
    }
}

@Composable
private fun ClientChargeContent(
    chargesList: List<Charge>,
    onChargeClick: (charge: Charge) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = chargesList) { charge ->
            ClientChargeItem(charge = charge, onChargeClick = { onChargeClick(charge) })
        }
    }
}

@Composable
private fun ClientChargeDialogs(
    dialogState: ClientChargeState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is ClientChargeState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = dialogState.message,
                ),
                onDismissRequest = onDismissRequest,
            )
        }
        null -> Unit
    }
}

@Composable
fun ChargeFilterSheetContent(
    state: ClientChargeState,
    onApply: (Any?, ChargeType, ChargeFilterUtil) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val savingsLabel = stringResource(Res.string.account_type_savings)
    val loanLabel = stringResource(Res.string.account_type_loan)
    val sharesLabel = stringResource(Res.string.account_type_shares)

    var selectedTabLabel by remember {
        mutableStateOf(
            when {
                state.selectedLoanAccount != null || state.chargeType == ChargeType.LOAN -> loanLabel
                state.selectedShareAccount != null || state.chargeType == ChargeType.SHARE -> sharesLabel
                else -> savingsLabel
            },
        )
    }

    var selectedAccountObject by remember {
        mutableStateOf(
            state.selectedSavingsAccount ?: state.selectedLoanAccount ?: state.selectedShareAccount,
        )
    }

    var selectedFilter by remember { mutableStateOf(state.activeFilter) }

    val currentAccountList: List<Any> = when (selectedTabLabel) {
        savingsLabel -> state.savingsAccounts
        loanLabel -> state.loanAccounts
        sharesLabel -> state.shareAccounts
        else -> emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DesignToken.padding.largeIncreased, vertical = DesignToken.padding.large),
    ) {
        FilterHeader(onClear = onClear)

        HorizontalDivider(modifier = Modifier.padding(vertical = DesignToken.padding.small))

        if (state.canSwitchAccounts) {
            AccountTypeSection(
                selectedTabLabel = selectedTabLabel,
                onTabSelected = { newTab ->
                    selectedTabLabel = newTab
                    selectedAccountObject = null
                },
            )

            if (currentAccountList.isNotEmpty()) {
                AccountDropdownSection(
                    accounts = currentAccountList,
                    selectedAccount = selectedAccountObject,
                    onAccountSelected = { selectedAccountObject = it },
                )
            }
        }

        ChargeStatusSection(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.dp40))

        FilterApplyButton(
            onClick = {
                val targetType = when (selectedTabLabel) {
                    savingsLabel -> ChargeType.SAVINGS
                    loanLabel -> ChargeType.LOAN
                    sharesLabel -> ChargeType.SHARE
                    else -> ChargeType.CLIENT
                }
                onApply(selectedAccountObject, targetType, selectedFilter)
            },
        )
        Spacer(modifier = Modifier.height(DesignToken.spacing.dp24))
    }
}

@Composable
private fun FilterHeader(onClear: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.filter_charges),
            style = KptTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            ),
        )
        TextButton(onClick = onClear) {
            Text(
                text = stringResource(Res.string.clear_all),
                style = KptTheme.typography.bodyMedium.copy(
                    color = KptTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
    }
}

@Composable
private fun AccountTypeSection(
    selectedTabLabel: String,
    onTabSelected: (String) -> Unit,
) {
    Column {
        Text(
            text = stringResource(Res.string.account_type),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(vertical = DesignToken.padding.small),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium)) {
            val types = listOf(
                stringResource(Res.string.account_type_savings),
                stringResource(Res.string.account_type_loan),
                stringResource(Res.string.account_type_shares),
            )
            types.forEach { type ->
                FilterOptionChip(
                    label = type,
                    isSelected = selectedTabLabel == type,
                    onClick = { onTabSelected(type) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Spacer(modifier = Modifier.height(DesignToken.spacing.large))
    }
}

@Composable
private fun AccountDropdownSection(
    accounts: List<Any>,
    selectedAccount: Any?,
    onAccountSelected: (Any?) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(Res.string.select_account),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(bottom = DesignToken.padding.small),
        )

        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = true },
                shape = DesignToken.shapes.medium,
                border = BorderStroke(DesignToken.strokes.thin, Color.Gray.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(DesignToken.elevation.none),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DesignToken.padding.large),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val (_, accNo) = getAccountDetails(selectedAccount)
                    Text(
                        text = accNo ?: "All Accounts",
                        style = KptTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    )
                    Icon(
                        imageVector = MifosIcons.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier.fillMaxWidth(0.9f).background(Color.White),
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Res.string.all_accounts),
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    onClick = {
                        onAccountSelected(null)
                        isExpanded = false
                    },
                )

                accounts.forEach { account ->
                    val (productName, accountNo) = getAccountDetails(account)
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = productName ?: stringResource(Res.string.account_label),
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = accountNo ?: "",
                                    style = KptTheme.typography.bodySmall,
                                )
                            }
                        },
                        onClick = {
                            onAccountSelected(account)
                            isExpanded = false
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))
    }
}

@Composable
private fun ChargeStatusSection(
    selectedFilter: ChargeFilterUtil,
    onFilterSelected: (ChargeFilterUtil) -> Unit,
) {
    Column {
        Text(
            text = stringResource(Res.string.charge_status),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(bottom = DesignToken.padding.medium),
        )

        val filtersFirstRow = listOf(ChargeFilterUtil.ALL, ChargeFilterUtil.PAID)
        val filtersSecondRow = listOf(ChargeFilterUtil.PENDING, ChargeFilterUtil.WAIVED)

        Column(verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
            ) {
                filtersFirstRow.forEach { filter ->
                    FilterOptionChip(
                        label = stringResource(filter.label),
                        isSelected = selectedFilter == filter,
                        onClick = { onFilterSelected(filter) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
            ) {
                filtersSecondRow.forEach { filter ->
                    FilterOptionChip(
                        label = stringResource(filter.label),
                        isSelected = selectedFilter == filter,
                        onClick = { onFilterSelected(filter) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterApplyButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.buttonDp50),
        shape = DesignToken.shapes.dp25,
        colors = ButtonDefaults.buttonColors(containerColor = KptTheme.colorScheme.primary),
    ) {
        Text(
            text = stringResource(Res.string.apply_filters),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

private fun getAccountDetails(account: Any?): Pair<String?, String?> {
    return when (account) {
        is SavingAccount -> account.productName to account.accountNo
        is LoanAccount -> account.productName to account.accountNo
        is ShareAccount -> account.productName to account.accountNo
        else -> null to null
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
                color = if (isSelected) {
                    KptTheme.colorScheme.onPrimary
                } else {
                    KptTheme.colorScheme.onSurface
                },
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@Preview
@Composable
private fun ClientChargeScreenPreview() {
    MifosMobileTheme {
        ClientChargeScreen(
            modifier = Modifier,
            state = ClientChargeState(
                dialogState = null,
                isOnline = false,
                clientId = 1L,
                chargeType = ChargeType.CLIENT,
                chargeTypeId = 1L,
            ),
            onAction = { },
        )
    }
}
