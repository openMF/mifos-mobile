/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_account_title
import mifos_mobile.feature.accounts.generated.resources.feature_filters_count
import mifos_mobile.feature.accounts.generated.resources.feature_savings_apply
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter
import mifos_mobile.feature.accounts.generated.resources.feature_savings_reset
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.rememberMifosPullToRefreshState
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.model.FilterType
import org.mifos.mobile.feature.accounts.viewmodel.AccountsAction
import org.mifos.mobile.feature.accounts.viewmodel.AccountsEvent
import org.mifos.mobile.feature.accounts.viewmodel.AccountsState
import org.mifos.mobile.feature.accounts.viewmodel.AccountsViewModel
import org.mifos.mobile.feature.savingsaccount.SavingsAccountScreen

@Composable
internal fun AccountsScreen(
    navigateBack: () -> Unit,
    onAccountClicked: (accountType: String, accountId: Long) -> Unit,
    viewModel: AccountsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is AccountsEvent.AccountClicked -> {
                onAccountClicked(event.accountType, event.accountId)
            }
            AccountsEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    AccountScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    AccountsDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun AccountsDialog(
    state: AccountsState,
    onAction: (AccountsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.dialogState) {
        is AccountsState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.dialogState.message,
                ),
                onDismissRequest = { onAction(AccountsAction.DismissDialog) },
            )
        }
        AccountsState.DialogState.Filters -> SavingsAccountFilters(
            state = state,
            onAction = onAction,
            modifier = modifier,
        )
        AccountsState.DialogState.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )
        null -> {}
    }
}

@Composable
internal fun SavingsAccountFilters(
    state: AccountsState,
    onAction: (AccountsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isTypeExpanded by rememberSaveable { mutableStateOf(true) }
    var isStatusExpanded by rememberSaveable { mutableStateOf(true) }

    val resetColor = if (state.isAnyFilterSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.inversePrimary
    }

    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_account_title),
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
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.large),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onAction(AccountsAction.DismissDialog) },
                        imageVector = MifosIcons.Dismiss,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(Res.string.feature_savings_filter),
                        style = MifosTypography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        DesignToken.spacing
                            .largeIncreased,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                state.isAnyFilterSelected,
                            ) { onAction(AccountsAction.ResetFilters) },
                        horizontalArrangement = Arrangement.spacedBy(
                            DesignToken.spacing
                                .extraSmall,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(

                            text = stringResource(Res.string.feature_savings_reset),
                            style = MifosTypography.bodySmallEmphasized,
                            color = resetColor,
                        )

                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = MifosIcons.ArrowCounterClockWise,
                            contentDescription = null,
                            tint = resetColor,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clickable { onAction(AccountsAction.GetFilterResults) },
                        horizontalArrangement = Arrangement.spacedBy(
                            DesignToken.spacing
                                .extraSmall,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(

                            text = stringResource(Res.string.feature_savings_apply),
                            style = MifosTypography.bodySmallEmphasized,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = MifosIcons.CheckMark,
                            contentDescription = null,
                        )
                    }
                }
            }

            Spacer(Modifier.height(DesignToken.spacing.largeIncreased))

            HorizontalDivider(modifier = Modifier.height(1.dp))
            FilterSection(
                title = "Type",
                filtersSelected = state.accountTypeFiltersCount ?: 0,
                isExpanded = isTypeExpanded,
                onToggle = { isTypeExpanded = !isTypeExpanded },
                filters = state.checkboxOptions.filter { it.type == FilterType.ACCOUNT_TYPE },
                onCheckChanged = { label ->
                    onAction(AccountsAction.ToggleCheckbox(label, FilterType.ACCOUNT_TYPE))
                },
            )

            FilterSection(
                title = "Status",
                filtersSelected = state.accountStatusFiltersCount ?: 0,
                isExpanded = isStatusExpanded,
                onToggle = { isStatusExpanded = !isStatusExpanded },
                filters = state.checkboxOptions.filter { it.type == FilterType.ACCOUNT_STATUS },
                onCheckChanged = { label ->
                    onAction(AccountsAction.ToggleCheckbox(label, FilterType.ACCOUNT_STATUS))
                },
            )
        }
    }
}

@Composable
internal fun FilterSection(
    title: String,
    filtersSelected: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    filters: List<CheckboxStatus>,
    onCheckChanged: (StringResource) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = DesignToken.spacing.extraLargeIncreased,
                end = DesignToken.spacing.small,
                top = DesignToken.padding.medium,
                bottom = DesignToken.padding.medium,
            ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MifosTypography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (filtersSelected != 0) {
                        Text(
                            text = stringResource(Res.string.feature_filters_count, filtersSelected),
                            style = MifosTypography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .width(DesignToken.sizes.iconSmall)
                            .height(DesignToken.sizes.iconSmall),
                        imageVector = if (isExpanded) MifosIcons.ChevronUp else MifosIcons.ChevronDown,
                        contentDescription = null,
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 40 }) + fadeOut(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    filters.forEach { filter ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = filter.isChecked,
                                onCheckedChange = { onCheckChanged(filter.statusLabel) },
                            )
                            Spacer(modifier = Modifier.width(DesignToken.spacing.extraSmall))
                            Text(
                                text = stringResource(filter.statusLabel),
                                style = MifosTypography.labelMediumEmphasized,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.height(1.dp))
    }
}

@Composable
internal fun AccountScreenContent(
    state: AccountsState,
    onAction: (AccountsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.isRefreshing
    val pullToRefreshState = rememberMifosPullToRefreshState(
        isEnabled = true,
        isRefreshing = isRefreshing,
        onRefresh = {
            onAction(AccountsAction.Refresh)
        },
    )

    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_account_title),
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
        when (state.accountType) {
            AccountType.SAVINGS -> {
                val typeFilters = state.selectedFilters.filter { it.type == FilterType.ACCOUNT_TYPE }
                val statusFilters = state.selectedFilters.filter { it.type == FilterType.ACCOUNT_STATUS }
                SavingsAccountScreen(
                    navigateBack = { onAction(AccountsAction.OnNavigateBack) },
                    refreshSignal = state.refreshSignal,
                    onLoadingCompleted = {
                        onAction(AccountsAction.RefreshCompleted)
                    },
                    onAccountClicked = { accountType, accountId ->
                        onAction(AccountsAction.OnAccountClicked(accountId, accountType))
                    },
                    accountTypeFilters = typeFilters.map { it.statusLabel },
                    accountStatusFilters = statusFilters.map { it.statusLabel },
                    filtersClicked = { onAction(AccountsAction.ToggleFilter) },
                )
            }
            AccountType.LOAN -> {}
            AccountType.SHARE -> {}
        }
    }
}

@DevicePreview
@Composable
internal fun ClientAccountsScreenPreview() {
    MifosMobileTheme {
        AccountScreenContent(
            state = AccountsState(dialogState = null),
            onAction = {},
        )
    }
}
