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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_download_icon_description
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_filter_icon_description
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_statement
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_transaction_history
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.rememberMifosPullToRefreshState
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.TransactionScreenItem
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.accounts.viewmodel.AccountTransactionAction
import org.mifos.mobile.feature.accounts.viewmodel.AccountTransactionEvent
import org.mifos.mobile.feature.accounts.viewmodel.AccountTransactionState
import org.mifos.mobile.feature.accounts.viewmodel.AccountsTransactionViewModel
import org.mifos.mobile.feature.accounts.viewmodel.getTransactionCreditStatus

@Composable
internal fun TransactionScreen(
    navigateBack: () -> Unit,
    viewModel: AccountsTransactionViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            AccountTransactionEvent.OnNavigateBack -> {
                navigateBack.invoke()
            }
        }
    }

    TransactionScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    AccountTransactionsDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun TransactionScreenContent(
    state: AccountTransactionState,
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = state.isRefreshing
    val pullToRefreshState = rememberMifosPullToRefreshState(
        isEnabled = true,
        isRefreshing = isRefreshing,
        onRefresh = {
            onAction(AccountTransactionAction.Refresh)
        },
    )

    MifosElevatedScaffold(
        onNavigateBack = {
            onAction(AccountTransactionAction.OnNavigateBackClick)
        },
        topBarTitle = stringResource(Res.string.feature_transaction_transaction_history),
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
        LazyColumn(
            Modifier.padding(DesignToken.padding.large),
        ) {
            item {
                ActionBar(
                    onAction = {},
                )
            }

            state.data.forEach { (date, transactions) ->
                item {
                    Text(
                        text = date,
                        style = MifosTypography.labelLargeEmphasized,
                        modifier = Modifier.padding(vertical = DesignToken.padding.medium),
                    )
                }

                items(transactions.size) { index ->
                    val transaction = transactions[index]
                    TransactionScreenItem(
                        title = transaction.paymentDetailData?.paymentType?.name ?: "",
                        date = DateHelper.getDateAsString(transaction.date),
                        time = "",
                        transactionAmount = CurrencyFormatter.format(
                            balance = transaction.amount,
                            currencyCode = transaction.currency?.code ?: "USD",
                            maximumFractionDigits = 3,
                        ),
                        isCredited = getTransactionCreditStatus(transaction.transactionType),
                    )
                }
            }
        }
    }
}

@Composable
internal fun AccountTransactionsDialog(
    state: AccountTransactionState,
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.dialogState) {
        is AccountTransactionState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = state.dialogState.message,
                ),
                onDismissRequest = { onAction(AccountTransactionAction.DismissDialog) },
            )
        }
        AccountTransactionState.DialogState.Filters -> {}
        AccountTransactionState.DialogState.Loading -> MifosLoadingDialog(
            modifier = modifier,
            visibilityState = LoadingDialogState.Shown,
        )
        null -> {}
    }
}

@Composable
internal fun ActionBar(
    onAction: (AccountTransactionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = DesignToken.padding.medium),
        horizontalArrangement = Arrangement.End,
    ) {
        Row(
            modifier = Modifier.clickable {
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            Text(
                text = stringResource(Res.string.feature_transaction_statement),
                color = MaterialTheme.colorScheme.primary,
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.Download,
                contentDescription = stringResource(Res.string.feature_transaction_download_icon_description),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.largeIncreased))

        Row(
            modifier = Modifier.clickable {
                onAction(AccountTransactionAction.FilterClicked)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            Text(
                text = stringResource(Res.string.feature_transaction_filter),
                color = MaterialTheme.colorScheme.primary,
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.Filter,
                contentDescription = stringResource(Res.string.feature_transaction_filter_icon_description),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
