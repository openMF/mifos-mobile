/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.pocketDashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import mifos_mobile.feature.pocket.generated.resources.Res
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_dashboard_loan_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_dashboard_manage
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_dashboard_savings_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_dashboard_share_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_dashboard_title
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_dashboard_total_balance
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.rememberMifosPullToRefreshState
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.ui.component.MifosAccountCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.pocket.components.EmptyPocketContent
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun PocketDashboardScreen(
    navigateBack: () -> Unit,
    navigateToManagePocket: () -> Unit,
    navigateToLoanAccountDetail: (Long) -> Unit,
    navigateToShareAccountDetail: (Long) -> Unit,
    navigateToSavingsAccountDetail: (Long) -> Unit,
    viewModel: PocketDashboardViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            PocketDashboardEvent.NavigateBack -> navigateBack.invoke()
            PocketDashboardEvent.ManagePocket -> navigateToManagePocket.invoke()
            is PocketDashboardEvent.NavigateToLoanDetail -> navigateToLoanAccountDetail(event.accountId)
            is PocketDashboardEvent.NavigateToSavingsDetail -> navigateToSavingsAccountDetail(event.accountId)
            is PocketDashboardEvent.NavigateToShareDetail -> navigateToShareAccountDetail(event.accountId)
        }
    }

    PocketDashboardContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun PocketDashboardContent(
    state: PocketDashboardState,
    onAction: (PocketDashboardAction) -> Unit,
) {
    val pullToRefreshState = rememberMifosPullToRefreshState(
        isEnabled = true,
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(PocketDashboardAction.Refresh) },
    )

    MifosElevatedScaffold(
        onNavigateBack = { onAction(PocketDashboardAction.NavigateBack) },
        topBarTitle = stringResource(Res.string.feature_pocket_dashboard_title),
        pullToRefreshState = pullToRefreshState,
        containerColor = KptTheme.colorScheme.background,
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> {
                MifosProgressIndicator()
            }
            is ScreenUiState.ErrorString -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = state.uiState.message,
                    onRetry = { onAction(PocketDashboardAction.Retry) },
                )
            }
            is ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(PocketDashboardAction.Retry) },
                )
            }
            ScreenUiState.Empty -> {
                EmptyPocketContent(
                    onLinkFirstAccount = { onAction(PocketDashboardAction.LinkFirstAccount) },
                )
            }
            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(DesignToken.padding.large),
                ) {
                    PocketDashboardCard(
                        totalBalance = state.totalBalance,
                        onManageClick = { onAction(PocketDashboardAction.ManagePocket) },
                    )

                    Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

                    if (state.savingsAccounts.isNotEmpty()) {
                        PocketSectionHeader(title = Res.string.feature_pocket_dashboard_savings_accounts)
                        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))
                        Column(verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium)) {
                            state.savingsAccounts.forEach { account ->
                                MifosAccountCard(
                                    accountId = account.accountId,
                                    accountType = account.name,
                                    accountNumber = account.accountNumber,
                                    accountStatus = account.balanceOrStatus,
                                    accountStatusColor = account.status.toColor(),
                                    onAccountClick = {
                                        onAction(PocketDashboardAction.NavigateToSavingsDetail(account.accountId))
                                    },
                                    icon = MifosIcons.PersonAccounts,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))
                    }

                    if (state.loanAccounts.isNotEmpty()) {
                        PocketSectionHeader(title = Res.string.feature_pocket_dashboard_loan_accounts)
                        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))
                        Column(verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium)) {
                            state.loanAccounts.forEach { account ->
                                MifosAccountCard(
                                    accountId = account.accountId,
                                    accountType = account.name,
                                    accountNumber = account.accountNumber,
                                    accountStatus = account.balanceOrStatus,
                                    accountStatusColor = account.status.toColor(),
                                    onAccountClick = {
                                        onAction(PocketDashboardAction.NavigateToLoanDetail(account.accountId))
                                    },
                                    icon = MifosIcons.CoinMultiple,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))
                    }

                    if (state.shareAccounts.isNotEmpty()) {
                        PocketSectionHeader(title = Res.string.feature_pocket_dashboard_share_accounts)
                        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))
                        Column(verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium)) {
                            state.shareAccounts.forEach { account ->
                                MifosAccountCard(
                                    accountId = account.accountId,
                                    accountType = account.name,
                                    accountNumber = account.accountNumber,
                                    accountStatus = account.balanceOrStatus,
                                    accountStatusColor = account.status.toColor(),
                                    onAccountClick = {
                                        onAction(PocketDashboardAction.NavigateToShareDetail(account.accountId))
                                    },
                                    icon = MifosIcons.CoinMultiple,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
internal fun PocketSectionHeader(
    title: StringResource,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(title),
        modifier = modifier.fillMaxWidth(),
        style = MifosTypography.titleSmallEmphasized,
        color = KptTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun PocketDashboardCard(
    totalBalance: String,
    onManageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(KptTheme.shapes.large)
            .height(DesignToken.sizes.cardDp112)
            .fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(mifos_mobile.core.ui.generated.resources.Res.drawable.ic_icon_dashboard),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
            ) {
                Text(
                    text = stringResource(Res.string.feature_pocket_dashboard_total_balance),
                    style = MifosTypography.bodySmall,
                    color = AppColors.customWhite.copy(alpha = 0.85f),
                )

                Text(
                    text = totalBalance,
                    style = MifosTypography.headlineSmallEmphasized,
                    color = AppColors.customWhite,
                )
            }

            MifosButton(
                onClick = onManageClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.customWhite,
                    contentColor = KptTheme.colorScheme.primary,
                ),
                content = {
                    Text(
                        text = stringResource(Res.string.feature_pocket_dashboard_manage),
                        style = MifosTypography.titleSmallEmphasized,
                    )
                },
            )
        }
    }
}

@Composable
fun AccountStatus.toColor(): Color =
    when (this) {
        AccountStatus.ACTIVE ->
            AppColors.customEnable

        AccountStatus.PENDING,
        AccountStatus.APPROVED,
        ->
            AppColors.customYellow

        AccountStatus.REJECTED,
        AccountStatus.WITHDRAWN,
        AccountStatus.CLOSED,
        ->
            KptTheme.colorScheme.error

        AccountStatus.OVERPAID,
        AccountStatus.MATURED,
        ->
            AppColors.customEnable

        AccountStatus.UNKNOWN ->
            KptTheme.colorScheme.onSurface
    }

@Preview
@Composable
internal fun PocketDashboardContentPreview() {
    MifosMobileTheme(darkTheme = false) {
        PocketDashboardContent(
            state = PocketDashboardState(
                totalBalance = "$ 18,750.00",
                savingsAccounts = listOf(
                    DetailedPocket(
                        accountId = 1L,
                        name = "Emergency Fund",
                        accountNumber = "1004859238",
                        balanceOrStatus = "$ 5,000.00",
                        status = AccountStatus.ACTIVE,
                    ),
                    DetailedPocket(
                        accountId = 2L,
                        name = "Vacation Savings",
                        accountNumber = "1004859299",
                        balanceOrStatus = "$ 1,250.00",
                        status = AccountStatus.ACTIVE,
                    ),
                ),
                loanAccounts = listOf(
                    DetailedPocket(
                        accountId = 3L,
                        name = "Personal Loan",
                        accountNumber = "3009284756",
                        balanceOrStatus = "$ 10,000.00",
                        status = AccountStatus.ACTIVE,
                    ),
                    DetailedPocket(
                        accountId = 4L,
                        name = "Auto Loan",
                        accountNumber = "3009284812",
                        balanceOrStatus = "PENDING",
                        status = AccountStatus.PENDING,
                    ),
                ),
                shareAccounts = listOf(
                    DetailedPocket(
                        accountId = 5L,
                        name = "Company Shares",
                        accountNumber = "5001129384",
                        balanceOrStatus = "$ 2,500.00",
                        status = AccountStatus.ACTIVE,
                    ),
                ),
                uiState = ScreenUiState.Success,
            ),
            onAction = {},
        )
    }
}
