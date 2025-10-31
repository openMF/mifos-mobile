/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.shareAccount

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
import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_account_empty_share_accounts
import mifos_mobile.feature.share_account.generated.resources.feature_share_account
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_dashboard
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_items
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
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
import kotlin.collections.orEmpty

/**
 * A Composable function that represents the Share Account screen.
 *
 * @param navigateBack A function to navigate back to the previous screen.
 * @param onAccountClicked A function to handle account click events.
 * @param refreshSignal A signal to trigger a refresh of the account data.
 * @param onLoadingCompleted A function to be called when loading is completed.
 * @param accountTypeFilters A list of account type filters.
 * @param accountStatusFilters A list of account status filters.
 * @param filtersClicked A function to handle filter click events.
 * @param viewModel An instance of [ShareAccountsViewmodel].
 */
@Composable
fun ShareAccountScreen(
    navigateBack: () -> Unit,
    onAccountClicked: (String, Long) -> Unit = { _, _ -> },
    refreshSignal: Long? = null,
    onLoadingCompleted: () -> Unit = {},
    accountTypeFilters: List<StringResource> = emptyList(),
    accountStatusFilters: List<StringResource> = emptyList(),
    filtersClicked: () -> Unit = {},
    viewModel: ShareAccountsViewmodel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(refreshSignal) {
        if (state.firstLaunch) {
            viewModel.trySendAction(ShareAccountsAction.OnFirstLaunched)
            return@LaunchedEffect
        }

        viewModel.trySendAction(
            ShareAccountsAction.LoadAccounts(
                filters = accountTypeFilters + accountStatusFilters,
            ),
        )
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ShareAccountsEvent.NavigateBack -> navigateBack.invoke()

            is ShareAccountsEvent.AccountClicked -> {
                onAccountClicked(Constants.SHARE_ACCOUNTS, event.accountId)
            }

            is ShareAccountsEvent.LoadingCompleted -> {
                onLoadingCompleted.invoke()
            }
        }
    }

    ShareAccountDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ShareAccountContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        filtersClicked = filtersClicked,
    )
}

/**
 * A Composable function that displays a dialog based on the [ShareAccountsState.DialogState].
 *
 * @param dialogState The state of the dialog to be displayed.
 * @param onAction A function to handle actions from the dialog.
 */
@Composable
internal fun ShareAccountDialog(
    dialogState: ShareAccountsState.DialogState?,
    onAction: (ShareAccountsAction) -> Unit,
) {
    when (dialogState) {
        is ShareAccountsState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(ShareAccountsAction.OnRetry) },
                isRetryEnabled = true,
            )
        }

        null -> Unit
    }
}

/**
 * A Composable function that displays the content of the Share Account screen.
 *
 * @param state The current state of the screen.
 * @param onAction A function to handle actions from the screen.
 * @param filtersClicked A function to handle filter click events.
 */
@Composable
internal fun ShareAccountContent(
    state: ShareAccountsState,
    onAction: (ShareAccountsAction) -> Unit,
    filtersClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DesignToken.padding.large),
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> {
                MifosProgressIndicator()
            }

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(ShareAccountsAction.OnRetry) },
                )
            }

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(ShareAccountsAction.OnRetry) },
                )
            }

            ScreenUiState.Empty -> {
                EmptyDataView(
                    icon = MifosIcons.Info,
                    error = Res.string.feature_account_empty_share_accounts,
                )
            }

            ScreenUiState.Success -> {
                Spacer(modifier = Modifier.height(DesignToken.spacing.large))

                MifosDashboardCard(
                    isSingleLine = true,
                    savingsAccount = Res.string.feature_share_account_dashboard,
                    savingsAmount = state.totalLoanAmount,
                    isVisible = state.isAmountVisible,
                    currency = state.currency,
                    onVisibilityToggle = { onAction(ShareAccountsAction.ToggleAmountVisible) },
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.feature_share_account),
                            style = MifosTypography.titleMediumEmphasized,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = stringResource(
                                Res.string.feature_share_account_items,
                                state.items ?: 0,
                            ),
                            style = MifosTypography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
                    ) {
                        Icon(
                            modifier = Modifier
                                .clickable {}
                                .size(20.dp),
                            imageVector = MifosIcons.SearchNew,
                            contentDescription = null,
                        )
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

                if (state.isFilteredEmpty) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        EmptyDataView(
                            icon = MifosIcons.Info,
                            error = Res.string.feature_account_empty_share_accounts,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(DesignToken.spacing.small))
                        }
                        items(state.shareAccounts.orEmpty()) { account ->
                            val color = when (account.status?.value) {
                                LoanStatus.ACTIVE.status -> AppColors.customEnable
                                LoanStatus.SUBMIT_AND_PENDING_APPROVAL.status -> AppColors.customYellow
                                LoanStatus.WITHDRAWN.status, LoanStatus.MATURED.status ->
                                    MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurface
                            }

                            MifosAccountCard(
                                accountId = account.id,
                                accountNumber = account.accountNo,
                                accountType = account.productName,
                                accountStatus = (
                                    account.status?.value ?: ""
                                    ),
//                    TODO Design according to Figma design

//                        if (account.status?.active == true) {
//                            CurrencyFormatter.format(
//                                account.amount,
//                                account.currency?.code,
//                                account.currency?.decimalPlaces?.toInt(),
//                            )
//                        } else {
//                            account.status?.value ?: ""
//                        }),
                                accountStatusColor = color,
                                onAccountClick = {
                                    onAction(
                                        ShareAccountsAction.OnAccountClicked(
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

            else -> {}
        }
    }
}

@Preview
@Composable
private fun Share_Account_Preview() {
    MifosMobileTheme {
        ShareAccountContent(
            state = ShareAccountsState(
                dialogState = null,
                shareAccounts = emptyList(),
                clientId = 1L,
            ),
            onAction = {},
            filtersClicked = {},
        )
    }
}
