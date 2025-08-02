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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import kotlin.collections.orEmpty

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

@Composable
internal fun LoanAccountDialog(
    dialogState: LoanAccountsState.DialogState?,
    onAction: (LoanAccountsAction) -> Unit,
) {
    when (dialogState) {
        is LoanAccountsState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(LoanAccountsAction.OnRetry(emptyList())) },
                isRetryEnabled = true,
            )
        }
        is LoanAccountsState.DialogState.Loading -> MifosProgressIndicator()

        null -> Unit
    }
}

@Composable
internal fun LoanAccountContent(
    state: LoanAccountsState,
    onAction: (LoanAccountsAction) -> Unit,
    filtersClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DesignToken.padding.large),
    ) {
        if (!state.isEmpty && state.dialogState == null) {
            Spacer(modifier = Modifier.height(DesignToken.spacing.large))

            MifosDashboardCard(
                isSingleLine = true,
                savingsAccount = Res.string.feature_loan_account_dashboard,
                savingsAmount = state.totalLoanAmount,
                isVisible = state.isAmountVisible,
                currency = state.currency,
                onVisibilityToggle = { onAction(LoanAccountsAction.ToggleAmountVisible) },
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
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = stringResource(
                            Res.string.feature_loan_account_items,
                            state.items ?: 0,
                        ),
                        style = MifosTypography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
                ) {
                    // TODO : commenting because it has no
                    // functionality now which is not best experience for user
//                    Icon(
//                        modifier = Modifier
//                            .clickable {}
//                            .size(20.dp),
//                        imageVector = MifosIcons.SearchNew,
//                        contentDescription = null,
//                    )
                    Icon(
                        modifier = Modifier
                            .clickable { filtersClicked() }
                            .size(20.dp),
                        imageVector = MifosIcons.Filter,
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.99997.dp),
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                item {
                    Spacer(modifier = Modifier.height(DesignToken.spacing.small))
                }
                items(state.loanAccounts.orEmpty()) { account ->
                    val color = when (account.status?.value) {
                        LoanStatus.ACTIVE.status -> AppColors.customEnable
                        LoanStatus.SUBMIT_AND_PENDING_APPROVAL.status -> AppColors.customYellow
                        LoanStatus.WITHDRAWN.status, LoanStatus.MATURED.status -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
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
        if (state.isEmpty) {
            EmptyDataView(
                icon = MifosIcons.Info,
                error = Res.string.feature_account_empty_loan_accounts,
            )
        }
        if (state.isFilteredEmpty && !state.isEmpty) {
            EmptyDataView(
                icon = MifosIcons.Info,
                error = Res.string.feature_account_empty_filtered_loan_accounts,
            )
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
