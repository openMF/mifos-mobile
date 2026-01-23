/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.charges

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.database_warning
import mifos_mobile.feature.client_charge.generated.resources.error_no_charge
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_account_label
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_account_type
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_account_type_loan
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_account_type_savings
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_account_type_shares
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_all_accounts
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_apply_filters
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_charge_status
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_clear_all
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_filter_charges
import mifos_mobile.feature.client_charge.generated.resources.feature_client_charges_select_account
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Charge
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

    val title = stringResource(state.topBarTitleResId) +
        (state.selectedAccountNo?.let { " - $it" } ?: "")

    MifosElevatedScaffold(
        modifier = modifier,
        topBarTitle = title,
        onNavigateBack = { onAction(ClientChargeAction.OnNavigate) },
        actions = {
            IconButton(onClick = { onAction(ClientChargeAction.ToggleFilter) }) {
                Icon(
                    imageVector = MifosIcons.Filter,
                    contentDescription = "Filter",
                )
            }
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
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
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
                            modifier = Modifier.padding(KptTheme.spacing.lg),
                            chargesList = state.charges,
                            onChargeClick = {
                                onAction(ClientChargeAction.OnChargeClick(it))
                            },
                        )
                    }

                    else -> {}
                }
            }

            if (state.showFilter) {
                ModalBottomSheet(
                    onDismissRequest = { onAction(ClientChargeAction.ToggleFilter) },
                    sheetState = sheetState,
                ) {
                    ChargeFilterSheetContent(
                        state = state,
                        onApply = { target, filter ->
                            onAction(
                                ClientChargeAction.ApplyFilter(
                                    target = target,
                                    filter = filter,
                                ),
                            )
                        },
                        onClear = {
                            onAction(ClientChargeAction.ClearFilter)
                        },
                    )
                }
            }
        },
    )
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
    onApply: (ChargeAccountTarget, ChargeFilterUtil) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val savingsLabel = stringResource(Res.string.feature_client_charges_account_type_savings)
    val loanLabel = stringResource(Res.string.feature_client_charges_account_type_loan)
    val sharesLabel = stringResource(Res.string.feature_client_charges_account_type_shares)

    var selectedTabLabel by rememberSaveable {
        mutableStateOf(
            when {
                state.selectedLoanAccount != null || state.chargeType == ChargeType.LOAN -> loanLabel
                state.selectedShareAccount != null || state.chargeType == ChargeType.SHARE -> sharesLabel
                else -> savingsLabel
            },
        )
    }

    var selectedTarget by remember {
        mutableStateOf(
            when {
                state.selectedSavingsAccount != null ->
                    ChargeAccountTarget.Savings(state.selectedSavingsAccount)

                state.selectedLoanAccount != null ->
                    ChargeAccountTarget.Loan(state.selectedLoanAccount)

                state.selectedShareAccount != null ->
                    ChargeAccountTarget.Share(state.selectedShareAccount)

                else -> ChargeAccountTarget.AllAccounts
            },
        )
    }

    var selectedFilter by remember { mutableStateOf(state.activeFilter) }

    val currentAccountList: List<ChargeAccountTarget> = when (selectedTabLabel) {
        savingsLabel -> state.savingsAccounts.map { ChargeAccountTarget.Savings(it) }
        loanLabel -> state.loanAccounts.map { ChargeAccountTarget.Loan(it) }
        sharesLabel -> state.shareAccounts.map { ChargeAccountTarget.Share(it) }
        else -> emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = KptTheme.spacing.xl,
                vertical = KptTheme.spacing.lg,
            ),
    ) {
        FilterHeader(onClear = onClear)

        HorizontalDivider(modifier = Modifier.padding(vertical = KptTheme.spacing.sm))

        if (state.canSwitchAccounts) {
            AccountTypeSection(
                selectedTabLabel = selectedTabLabel,
                onTabSelected = { newTab ->
                    selectedTabLabel = newTab
                    selectedTarget = ChargeAccountTarget.AllAccounts
                },
            )

            if (currentAccountList.isNotEmpty()) {
                AccountDropdownSection(
                    accounts = currentAccountList,
                    selectedTarget = selectedTarget,
                    onTargetSelected = { selectedTarget = it },
                )
            }
        }

        ChargeStatusSection(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
        )

        Spacer(modifier = Modifier.height(KptTheme.spacing.md))

        FilterApplyButton(
            onClick = {
                onApply(selectedTarget, selectedFilter)
            },
        )
        Spacer(modifier = Modifier.height(KptTheme.spacing.lg))
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
            text = stringResource(Res.string.feature_client_charges_filter_charges),
            style = KptTheme.typography.titleLarge,
        )
        TextButton(onClick = onClear) {
            Text(
                text = stringResource(Res.string.feature_client_charges_clear_all),
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
            text = stringResource(Res.string.feature_client_charges_account_type),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(vertical = KptTheme.spacing.sm),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
            val types = listOf(
                stringResource(Res.string.feature_client_charges_account_type_savings),
                stringResource(Res.string.feature_client_charges_account_type_loan),
                stringResource(Res.string.feature_client_charges_account_type_shares),
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
        Spacer(modifier = Modifier.height(KptTheme.spacing.lg))
    }
}

@Composable
private fun AccountDropdownSection(
    accounts: List<ChargeAccountTarget>,
    selectedTarget: ChargeAccountTarget,
    onTargetSelected: (ChargeAccountTarget) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(Res.string.feature_client_charges_select_account),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(bottom = KptTheme.spacing.sm),
        )

        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = true },
                shape = KptTheme.shapes.medium,
                border = BorderStroke(DesignToken.strokes.thin, color = KptTheme.colorScheme.onSecondaryContainer),
                elevation = CardDefaults.cardElevation(KptTheme.elevation.level0),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(KptTheme.spacing.md),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val (_, accNo) = getAccountDetails(selectedTarget)
                    Text(
                        text = accNo ?: stringResource(
                            Res.string.feature_client_charges_all_accounts,
                        ),
                        style = KptTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    )
                    Icon(
                        imageVector = MifosIcons.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier.fillMaxWidth(0.9f),
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Res.string.feature_client_charges_all_accounts),
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    onClick = {
                        onTargetSelected(ChargeAccountTarget.AllAccounts)
                        isExpanded = false
                    },
                )

                accounts.forEach { account ->
                    val (productName, accountNo) = getAccountDetails(account)
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = productName
                                        ?: stringResource(
                                            Res.string.feature_client_charges_account_label,
                                        ),
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = accountNo ?: "",
                                    style = KptTheme.typography.bodySmall,
                                )
                            }
                        },
                        onClick = {
                            onTargetSelected(account)
                            isExpanded = false
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(KptTheme.spacing.sm))
    }
}

@Composable
private fun ChargeStatusSection(
    selectedFilter: ChargeFilterUtil,
    onFilterSelected: (ChargeFilterUtil) -> Unit,
) {
    Column {
        Text(
            text = stringResource(Res.string.feature_client_charges_charge_status),
            style = KptTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(bottom = KptTheme.spacing.md),
        )

        val filtersFirstRow = listOf(ChargeFilterUtil.ALL, ChargeFilterUtil.PAID)
        val filtersSecondRow = listOf(ChargeFilterUtil.PENDING, ChargeFilterUtil.WAIVED)

        Column(verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
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
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
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
        modifier = Modifier.fillMaxWidth(),
        shape = KptTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(containerColor = KptTheme.colorScheme.primary),
    ) {
        Text(
            text = stringResource(Res.string.feature_client_charges_apply_filters),
            fontStyle = KptTheme.typography.bodyLarge.fontStyle,
        )
    }
}

private fun getAccountDetails(target: ChargeAccountTarget): Pair<String?, String?> {
    return when (target) {
        is ChargeAccountTarget.Savings -> target.account.productName to target.account.accountNo
        is ChargeAccountTarget.Loan -> target.account.productName to target.account.accountNo
        is ChargeAccountTarget.Share -> target.account.productName to target.account.accountNo
        ChargeAccountTarget.AllAccounts -> null to null
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
        modifier = modifier.height(KptTheme.spacing.xl),
        shape = KptTheme.shapes.extraLarge,
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
