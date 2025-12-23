/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccount

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
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_empty_filtered_loan_accounts
import mifos_mobile.feature.loan_account.generated.resources.feature_account_empty_loan_accounts
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_dashboard
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_items
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
import org.mifos.mobile.core.model.LoanStatus
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosAccountCard
import org.mifos.mobile.core.ui.component.MifosDashboardCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import template.core.base.designsystem.theme.KptTheme
import kotlin.collections.orEmpty

/**
 * The main composable for the loan account screen.
 * It displays a list of loan accounts and allows the user to filter them.
 *
 * @param navigateBack A callback to navigate back to the previous screen.
 * @param onAccountClicked A callback that is invoked when a loan account is clicked.
 * @param refreshSignal A signal to trigger a refresh of the account list.
 * @param onLoadingCompleted A callback that is invoked when the initial loading is completed.
 * @param accountTypeFilters A list of account type filters to apply.
 * @param accountStatusFilters A list of account status filters to apply.
 * @param filtersClicked A callback that is invoked when the filter button is clicked.
 * @param viewModel The [LoanAccountsViewmodel] for this screen.
 */
@Composable
fun LoanAccountScreen(
    navigateBack: () -> Unit,
    onAccountClicked: (String, Long) -> Unit = { _, _ -> },
    refreshSignal: Long? = null,
    onLoadingCompleted: () -> Unit = {},
    accountTypeFilters: List<StringResource> = emptyList(),
    accountStatusFilters: List<StringResource> = emptyList(),
    filtersClicked: () -> Unit = {},
    viewModel: LoanAccountsViewmodel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(refreshSignal) {
        if (state.firstLaunch) {
            viewModel.trySendAction(LoanAccountsAction.OnFirstLaunched)
            return@LaunchedEffect
        }

        viewModel.trySendAction(
            LoanAccountsAction.LoadAccounts(
                filters = accountTypeFilters + accountStatusFilters,
            ),
        )
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is LoanAccountsEvent.NavigateBack -> navigateBack.invoke()

            is LoanAccountsEvent.AccountClicked -> {
                onAccountClicked(Constants.LOAN_ACCOUNT, event.accountId)
            }

            is LoanAccountsEvent.LoadingCompleted -> {
                onLoadingCompleted.invoke()
            }
        }
    }

    LoanAccountDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    LoanAccountContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        filtersClicked = filtersClicked,
    )
}

/**
 * A composable that displays a dialog for the loan account screen.
 * It can show an error dialog.
 *
 * @param dialogState The state of the dialog to display.
 * @param onAction A callback to handle actions from the dialog.
 */
@Composable
internal fun LoanAccountDialog(
    dialogState: LoanAccountsState.DialogState?,
    onAction: (LoanAccountsAction) -> Unit,
) {
    when (dialogState) {
        is LoanAccountsState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(LoanAccountsAction.OnRetry) },
                isRetryEnabled = true,
            )
        }

        null -> Unit
    }
}

/**
 * The content of the loan account screen.
 * It displays a dashboard card with the total loan amount and a list of loan accounts.
 * It also handles different UI states such as loading, error, network, and empty.
 *
 * @param state The [LoanAccountsState] for this screen.
 * @param onAction A callback to handle actions from the screen.
 * @param filtersClicked A callback that is invoked when the filter button is clicked.
 */
@Composable
internal fun LoanAccountContent(
    state: LoanAccountsState,
    onAction: (LoanAccountsAction) -> Unit,
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
                    onRetry = { onAction(LoanAccountsAction.OnRetry) },
                )
            }

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(LoanAccountsAction.OnRetry) },
                )
            }

            ScreenUiState.Empty -> {
                EmptyDataView(
                    icon = MifosIcons.Info,
                    error = Res.string.feature_account_empty_loan_accounts,
                )
            }

            ScreenUiState.Success -> {
                Spacer(modifier = Modifier.height(KptTheme.spacing.md))

                MifosDashboardCard(
                    isVisible = state.isAmountVisible,
                    isSingleLine = true,
                    savingsAccount = Res.string.feature_loan_account_dashboard,
                    savingsAmount = state.totalLoanAmount,
                    currency = state.currency,
                    onVisibilityToggle = {
                        onAction(LoanAccountsAction.ToggleAmountVisible)
                    },
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.feature_loan_account),
                            style = MifosTypography.titleMediumEmphasized,
                            color = KptTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = stringResource(
                                Res.string.feature_loan_account_items,
                                state.items ?: 0,
                            ),
                            style = MifosTypography.labelMedium,
                            color = KptTheme.colorScheme.secondary,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            DesignToken.spacing.largeIncreased,
                        ),
                    ) {
                        Icon(
                            modifier = Modifier
                                .clickable { filtersClicked() }
                                .size(DesignToken.sizes.iconDp20),
                            imageVector = MifosIcons.Filter,
                            contentDescription = null,
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
                            error = Res.string.feature_account_empty_filtered_loan_accounts,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                    ) {
                        item { Spacer(modifier = Modifier.height(KptTheme.spacing.sm)) }
                        items(state.loanAccounts.orEmpty()) { account ->
                            val color = when (account.status?.value) {
                                LoanStatus.ACTIVE.status -> AppColors.customEnable
                                LoanStatus.SUBMIT_AND_PENDING_APPROVAL.status -> AppColors.customYellow
                                LoanStatus.WITHDRAWN.status,
                                LoanStatus.MATURED.status,
                                -> KptTheme.colorScheme.error

                                else -> KptTheme.colorScheme.onSurface
                            }

                            MifosAccountCard(
                                accountId = account.id,
                                accountNumber = account.accountNo,
                                accountType = account.productName,
                                accountStatus = (
                                    if (account.status?.active == true) {
                                        CurrencyFormatter.format(
                                            account.loanBalance,
                                            account.currency?.code,
                                            account.currency?.decimalPlaces?.toInt(),
                                        )
                                    } else {
                                        account.status?.value ?: ""
                                    }
                                    ),
                                accountStatusColor = color,
                                onAccountClick = {
                                    onAction(
                                        LoanAccountsAction.OnAccountClicked(
                                            it,
                                            Constants.LOAN_ACCOUNT,
                                        ),
                                    )
                                },
                                icon = MifosIcons.CoinMultiple,
                            )
                        }
                    }
                }
            }

            else -> { }
        }
    }
}

@Preview
@Composable
private fun Savings_Account_Preview() {
    MifosMobileTheme {
        LoanAccountContent(
            state = LoanAccountsState(
                dialogState = null,
                loanAccounts = emptyList(),
                clientId = 1L,
            ),
            onAction = {},
            filtersClicked = {},
        )
    }
}
