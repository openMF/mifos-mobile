/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccount

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.content_description_filter
import mifos_mobile.feature.savings_account.generated.resources.feature_account_empty_savings_accounts
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_account
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_account_dashboard
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_account_items
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_filter_pending_account
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_no_accounts_found
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.SavingStatus
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosAccountCard
import org.mifos.mobile.core.ui.component.MifosDashboardCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import template.core.base.designsystem.theme.KptTheme

/**
 * A stateful composable that serves as the main entry point for the Savings Account list screen.
 *
 * This screen is responsible for observing state from the [SavingsAccountViewmodel], handling
 * user events, and orchestrating the display of the savings account data. It also manages
 * data loading triggers, including initial launch and external refresh signals.
 *
 * @param navigateBack A lambda to handle the back navigation event.
 * @param onAccountClicked A callback invoked when a savings account item is clicked, providing the account type and ID.
 * @param refreshSignal A signal to trigger a data refresh when its value changes.
 * @param onLoadingCompleted A callback invoked when the data loading process finishes.
 * @param accountTypeFilters A list of string resources representing the applied account type filters.
 * @param accountStatusFilters A list of string resources representing the applied account status filters.
 * @param filtersClicked A lambda to handle the click event on the filter icon.
 * @param viewModel The [SavingsAccountViewmodel] instance for this screen, typically provided by Koin.
 */
@Composable
fun SavingsAccountScreen(
    navigateBack: () -> Unit,
    onAccountClicked: (String, Long) -> Unit = { _, _ -> },
    refreshSignal: Long? = null,
    onLoadingCompleted: () -> Unit = {},
    accountTypeFilters: List<StringResource> = emptyList(),
    accountStatusFilters: List<StringResource> = emptyList(),
    filtersClicked: () -> Unit = {},
    viewModel: SavingsAccountViewmodel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(refreshSignal) {
        if (state.firstLaunch) {
            viewModel.trySendAction(SavingsAccountAction.OnFirstLaunched)
            return@LaunchedEffect
        }

        viewModel.trySendAction(
            SavingsAccountAction.LoadAccounts(
                filters = accountTypeFilters + accountStatusFilters,
            ),
        )
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is SavingsAccountsEvent.NavigateBack -> navigateBack.invoke()
            is SavingsAccountsEvent.AccountClicked -> {
                onAccountClicked(Constants.SAVINGS_ACCOUNT, event.accountId)
            }
            is SavingsAccountsEvent.LoadingCompleted -> onLoadingCompleted.invoke()
        }
    }

    SavingsAccountDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) { { viewModel.trySendAction(it) } },
    )

    SavingsAccountContent(
        state = state,
        onAction = remember(viewModel) { { viewModel.trySendAction(it) } },
        filtersClicked = filtersClicked,
    )
}

/**
 * A composable responsible for displaying dialogs based on the [SavingsAccountState].
 * Currently, it handles the display of an error dialog with a retry option.
 *
 * @param dialogState The current state of the dialog from [SavingsAccountState].
 * @param onAction A callback to send actions, like retry, to the ViewModel.
 */
@Composable
internal fun SavingsAccountDialog(
    dialogState: SavingsAccountState.DialogState?,
    onAction: (SavingsAccountAction) -> Unit,
) {
    when (dialogState) {
        is SavingsAccountState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(SavingsAccountAction.OnRetry) },
                isRetryEnabled = true,
            )
        }
        null -> Unit
    }
}

/**
 * A stateless composable that renders the main UI for the Savings Account screen.
 *
 * It conditionally displays UI elements based on the [ScreenUiState], such as a loading
 * indicator, error messages, an empty data view, or the success view with the list of accounts.
 *
 * @param state The current [SavingsAccountState] to render.
 * @param onAction A callback to send user actions to the ViewModel.
 * @param filtersClicked A lambda to handle the click event on the filter icon.
 */
@Composable
internal fun SavingsAccountContent(
    state: SavingsAccountState,
    onAction: (SavingsAccountAction) -> Unit,
    filtersClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(KptTheme.spacing.md),
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> {
                MifosProgressIndicator()
            }

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(SavingsAccountAction.OnRetry) },
                )
            }

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(SavingsAccountAction.OnRetry) },
                )
            }

            ScreenUiState.Empty -> {
                EmptyDataView(
                    icon = MifosIcons.Info,
                    error = Res.string.feature_account_empty_savings_accounts,
                )
            }

            ScreenUiState.Success -> {
                Spacer(modifier = Modifier.height(KptTheme.spacing.md))

                MifosDashboardCard(
                    isSingleLine = true,
                    savingsAccount = Res.string.feature_savings_account_dashboard,
                    savingsAmount = state.totalSavingAmount,
                    isVisible = state.isAmountVisible,
                    currency = state.currency,
                    onVisibilityToggle = { onAction(SavingsAccountAction.ToggleAmountVisible) },
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.feature_savings_account),
                            style = MifosTypography.titleMediumEmphasized,
                            color = KptTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = stringResource(
                                Res.string.feature_savings_account_items,
                                state.items ?: 0,
                            ),
                            style = MifosTypography.labelMedium,
                            color = KptTheme.colorScheme.secondary,
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased)) {
                        Icon(
                            modifier = Modifier
                                .clickable { filtersClicked() }
                                .size(DesignToken.sizes.iconDp20),
                            imageVector = MifosIcons.Filter,
                            contentDescription = stringResource(Res.string.content_description_filter),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignToken.strokes.thin),
                )

                if (state.isFilteredEmpty) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        EmptyDataView(
                            icon = MifosIcons.Info,
                            error = Res.string.feature_savings_no_accounts_found,
                        )
                    }
                } else {
                    val accounts = state.savingsAccount.orEmpty()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                    ) {
                        item { Spacer(modifier = Modifier.height(KptTheme.spacing.sm)) }

                        items(accounts) { account ->
                            val color = when (account.status?.value) {
                                SavingStatus.ACTIVE.status -> AppColors.customEnable
                                SavingStatus.SUBMIT_AND_PENDING_APPROVAL.status -> AppColors.customYellow
                                SavingStatus.INACTIVE.status -> KptTheme.colorScheme.error
                                else -> KptTheme.colorScheme.onSurface
                            }

                            val accountStatus = if (account.status?.active == true) {
                                CurrencyFormatter.format(
                                    account.accountBalance,
                                    account.currency?.code,
                                    account.currency?.decimalPlaces,
                                )
                            } else {
                                if (account.status?.value == SavingStatus.SUBMIT_AND_PENDING_APPROVAL.status) {
                                    stringResource(Res.string.feature_savings_filter_pending_account)
                                } else {
                                    account.status?.value ?: ""
                                }
                            }

                            MifosAccountCard(
                                accountId = account.id,
                                accountNumber = account.accountNo,
                                accountType = account.productName,
                                accountStatus = accountStatus,
                                accountStatusColor = color,
                                onAccountClick = {
                                    onAction(
                                        SavingsAccountAction.OnAccountClicked(
                                            it,
                                            Constants.SAVINGS_ACCOUNT,
                                        ),
                                    )
                                },
                                icon = MifosIcons.PersonAccounts,
                            )
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

/**
 * A Jetpack Compose preview for the [SavingsAccountContent].
 *
 * This provides a design-time visualization of the savings account screen UI in
 * Android Studio, configured with a default empty state.
 */

@Preview
@Composable
private fun Savings_Account_Preview() {
    MifosMobileTheme {
        SavingsAccountContent(
            state = SavingsAccountState(
                dialogState = null,
                savingsAccount = emptyList(),
                clientId = 1L,
            ),
            onAction = {},
            filtersClicked = {},
        )
    }
}
