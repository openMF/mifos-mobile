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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDashboardCard
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun HomeScreen(
//    navigateToDestinationScreen: (String) -> Unit,
    navigateToSavingsScreen: () -> Unit,
    navigateToNotificationScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is HomeEvent.Navigate -> {
                when {
                    event.route == Constants.SAVINGS_ACCOUNT -> navigateToSavingsScreen.invoke()
                }
//                navigateToDestinationScreen(event.route)
            }

            is HomeEvent.NavigateToNotification -> navigateToNotificationScreen.invoke()
        }
    }

    HomeScreenDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    HomeContent(
        state = state,
        modifier = modifier,
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
                Image(
                    imageVector = MifosIcons.SearchNew,
                    contentDescription = null,
                )
                Image(
                    imageVector = MifosIcons.Alert,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onAction(HomeAction.OnNotificationClick)
                    },
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(DesignToken.padding.large),
        ) {
            Spacer(modifier = Modifier.height(DesignToken.spacing.small))

            Text(
                text = stringResource(Res.string.feature_home_greet, state.username),
                style = MifosTypography.titleLarge,
                color = AppColors.customBlack,
            )

            Spacer(modifier = Modifier.height(DesignToken.spacing.large))

            MifosDashboardCard(
                isLoanApplied = state.isLoanApplied,
                savingsAccount = Res.string.feature_home_total_available_savings,
                loanAccount = Res.string.feature_home_total_available_loan,
                loanAmount = state.loanAmount,
                savingsAmount = state.savingsAmount,
                isVisible = state.isAmountVisible,
                onVisibilityToggle = { onAction(HomeAction.ToggleAmountVisible) },
                currency = state.currency,
            )

            Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

            Text(
                text = stringResource(Res.string.feature_home_services),
                style = MifosTypography.titleMediumEmphasized,
                color = AppColors.customBlack,
            )

            Spacer(modifier = Modifier.height(DesignToken.spacing.large))

            ServiceBox(
                items = state.items,
                onAction = onAction,
            )
        }
    }
}

@Composable
internal fun ServiceBox(
    items: ImmutableList<ServiceItem>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(4),
        verticalItemSpacing = DesignToken.spacing.medium,
        horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        content = {
            items(items) { item ->
                ServiceItemCard(
                    title = item.title,
                    icon = item.icon,
                    onClick = { onAction(HomeAction.OnNavigate(item.route)) },
                )
            }
        },
        modifier = modifier,
    )
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
            color = AppColors.customBlack,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HomeScreenDialog(
    dialogState: HomeState.DialogState?,
    onAction: (HomeAction) -> Unit,
) {
    when (dialogState) {
        is HomeState.DialogState.Error -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = { onAction(HomeAction.OnDismissDialog) },
        )
        is HomeState.DialogState.Loading -> MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )

        null -> Unit
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MifosMobileTheme {
        HomeContent(
            state = HomeState(dialogState = null, items = serviceCards),
            onAction = {},
            modifier = Modifier,
        )
    }
}
