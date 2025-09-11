/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import mifos_mobile.core.ui.generated.resources.ic_icon_logo_1
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.feature_home_greet
import mifos_mobile.feature.home.generated.resources.feature_home_services
import mifos_mobile.feature.home.generated.resources.feature_home_total_available_loan
import mifos_mobile.feature.home.generated.resources.feature_home_total_available_savings
import mifos_mobile.feature.home.generated.resources.feature_server_error
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosAccountApplyDashboard
import org.mifos.mobile.core.ui.component.MifosDashboardCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.home.components.BottomSheetContent
import org.mifos.mobile.feature.home.navigation.HomeNavigationDestination
import org.mifos.mobile.feature.home.navigation.HomeNavigator

@Composable
internal fun HomeScreen(
    onNavigate: HomeNavigator,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is HomeEvent.Navigate -> {
                when (event.route) {
                    Constants.SAVINGS_ACCOUNT ->
                        onNavigate(HomeNavigationDestination.AccountsWithType(Constants.SAVINGS_ACCOUNT))
                    Constants.LOAN_ACCOUNT ->
                        onNavigate(HomeNavigationDestination.AccountsWithType(Constants.LOAN_ACCOUNT))
                    Constants.SHARE_ACCOUNTS ->
                        onNavigate(HomeNavigationDestination.AccountsWithType(Constants.SHARE_ACCOUNTS))
                    Constants.APPLY_LOAN -> onNavigate(HomeNavigationDestination.ApplyLoan)
                    Constants.APPLY_SAVINGS -> onNavigate(HomeNavigationDestination.ApplySavings)
                    Constants.APPLY_SHARE -> onNavigate(HomeNavigationDestination.ApplyShare)
                    Constants.TRANSACTIONS -> onNavigate(HomeNavigationDestination.Transaction)
                    Constants.CHARGES -> onNavigate(HomeNavigationDestination.Charge)
                    Constants.BENEFICIARY -> onNavigate(HomeNavigationDestination.Beneficiary)
                    Constants.HELP -> onNavigate(HomeNavigationDestination.Faq)
                }
            }
            is HomeEvent.NavigateToNotification -> onNavigate(HomeNavigationDestination.Notification)
        }
    }

    HomeContent(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    HomeScreenDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        brandIcon = mifos_mobile.core.ui.generated.resources.Res.drawable.ic_icon_logo_1,
        topBarTitle = "Home",
        onNavigateBack = {},
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
            ) {
                // TODO : once ui/ux team gives this flow uncomment and implement
//                Image(
//                    imageVector = MifosIcons.SearchNew,
//                    contentDescription = null,
//                )
                Image(
                    imageVector = MifosIcons.Alert,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.clickable {
                        onAction(HomeAction.OnNotificationClick)
                    },
                )
            }
        },
    ) {
        when (state.uiState) {
            is HomeScreenState.Error -> {
                MifosErrorComponent(
                    message = stringResource(Res.string.feature_server_error),
                    isRetryEnabled = true,
                    onRetry = { onAction(HomeAction.Retry) },
                )
            }

            HomeScreenState.Loading -> MifosProgressIndicator()

            HomeScreenState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(HomeAction.Retry) },
                )
            }

            HomeScreenState.Success -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(DesignToken.padding.large),
                ) {
                    Spacer(modifier = Modifier.height(DesignToken.spacing.small))
                    Text(
                        text = stringResource(
                            Res.string.feature_home_greet,
                            state.firstName.toString(),
                        ),
                        style = MifosTypography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(DesignToken.spacing.large))

                    if (state.isAccountsPresent) {
                        MifosDashboardCard(
                            savingsAccount = Res.string.feature_home_total_available_savings,
                            loanAccount = Res.string.feature_home_total_available_loan,
                            loanAmount = state.loanAmount,
                            savingsAmount = state.savingsAmount,
                            isVisible = state.isAmountVisible,
                            onVisibilityToggle = { onAction(HomeAction.ToggleAmountVisible) },
                            currency = state.currency,
                        )
                    } else {
                        MifosAccountApplyDashboard(
                            onOpenAccountClick = { onAction(HomeAction.BottomBarPicker) },
                        )
                    }

                    Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

                    Text(
                        text = stringResource(Res.string.feature_home_services),
                        style = MifosTypography.titleMediumEmphasized,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(DesignToken.spacing.large))

                    ServiceBox(
                        items = state.items,
                        onAction = onAction,
                    )
                }
            }

            null -> {}
        }
    }
}

@Composable
internal fun ServiceBox(
    items: ImmutableList<ServiceItem>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        maxItemsInEachRow = 4,
        horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                ServiceItemCard(
                    title = item.title,
                    icon = item.icon,
                    onClick = { onAction(HomeAction.OnNavigate(item.route)) },
                )
            }
        }
    }
}

@Composable
internal fun ServiceItemCard(
    title: StringResource,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = DesignToken.padding.small),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    onClick()
                },
        ) {
            Image(
                modifier = Modifier
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondaryContainer,
                        DesignToken.shapes.medium,
                    )
                    .padding(DesignToken.padding.medium + 2.dp),
                imageVector = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
            )
        }

        Text(
            text = stringResource(title),
            style = MifosTypography.bodySmallEmphasized,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenDialog(
    dialogState: HomeState.DialogState?,
    onAction: (HomeAction) -> Unit,
) {
    when (dialogState) {
        is HomeState.DialogState.Error -> {
            MifosErrorComponent(
                isRetryEnabled = true,
                message = stringResource(dialogState.message),
                onRetry = { onAction(HomeAction.Retry) },
            )
        }

        is HomeState.DialogState.ShowAccountApplyBottomBar -> {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = dialogState.isVisible,
            )

            LaunchedEffect(dialogState.isVisible) {
                if (dialogState.isVisible) {
                    sheetState.expand()
                }
            }

            ModalBottomSheet(
                onDismissRequest = {
                    onAction(HomeAction.OnDismissDialog)
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                contentWindowInsets = {
                    BottomSheetDefaults.windowInsets
                },
                modifier = Modifier.wrapContentHeight(),
                dragHandle = { BottomSheetDefaults.DragHandle() },
            ) {
                BottomSheetContent(
                    onAction = onAction,
                    isVisible = dialogState.isVisible,
                )
            }
        }
        null -> Unit
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MifosMobileTheme {
        HomeContent(
            state = HomeState(
                dialogState = null,
                items = serviceCards,
                uiState = HomeScreenState.Success,
            ),
            onAction = {},
            modifier = Modifier,
        )
    }
}
