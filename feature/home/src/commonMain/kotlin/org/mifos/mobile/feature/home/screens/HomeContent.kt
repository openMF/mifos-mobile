/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.accounts_overview
import mifos_mobile.feature.home.generated.resources.contact_email
import mifos_mobile.feature.home.generated.resources.hello_client
import mifos_mobile.feature.home.generated.resources.help_line_number
import mifos_mobile.feature.home.generated.resources.hidden_amount
import mifos_mobile.feature.home.generated.resources.ic_visibility_24px
import mifos_mobile.feature.home.generated.resources.ic_visibility_off_24px
import mifos_mobile.feature.home.generated.resources.need_help
import mifos_mobile.feature.home.generated.resources.total_loan
import mifos_mobile.feature.home.generated.resources.total_saving
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosHiddenTextRow
import org.mifos.mobile.core.ui.component.MifosLinkText
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.feature.home.components.HomeNavigationDrawer
import org.mifos.mobile.feature.home.components.HomeTopBar
import org.mifos.mobile.feature.home.components.TransferDialog
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.navigation.toDestination
import org.mifos.mobile.feature.home.viewmodel.HomeAction
import org.mifos.mobile.feature.home.viewmodel.HomeCardItem
import org.mifos.mobile.feature.home.viewmodel.HomeNavigationItems
import org.mifos.mobile.feature.home.viewmodel.HomeState

@Composable
internal fun HomeContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    HomeNavigationDrawer(
        username = state.username,
        drawerState = drawerState,
        userBitmap = state.image,
        modifier = modifier,
        navigateItem = {
            coroutineScope.launch { drawerState.close() }
            when (it) {
                HomeNavigationItems.Logout -> onAction(HomeAction.OnLogoutClicked)
                else -> onAction(HomeAction.OnNavigate(it.toDestination()))
            }
        },
        content = {
            MifosScaffold(
                topBar = {
                    HomeTopBar(
                        openNavigationDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        notificationCount = state.notificationCount,
                        openNotifications = { onAction(HomeAction.OnNavigate(HomeDestinations.NOTIFICATIONS)) },
                    )
                },
            ) {
                HomeScreenContent(
                    modifier = Modifier.padding(it),
                    onAction = onAction,
                    state = state,
                )
            }
        },
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        UserDetailsRow(
            username = state.username,
            userBitmap = state.image,
            userProfile = { onAction(HomeAction.OnNavigate(HomeDestinations.PROFILE)) },
        )

        AccountOverviewCard(
            totalLoanAmount = state.loanAmount,
            totalSavingsAmount = state.savingsAmount,
            totalLoan = { onAction(HomeAction.OnNavigate(HomeDestinations.LOAN_ACCOUNT)) },
            totalSavings = { onAction(HomeAction.OnNavigate(HomeDestinations.SAVINGS_ACCOUNT)) },
        )

        state.homeCardItems?.let {
            HomeCards(
                homeCards = it,
                onNavigate = { destination ->
                    onAction(HomeAction.OnNavigate(destination))
                },
            )
        }

        ContactUsRow(
            callHelpline = { onAction(HomeAction.OnCallHelpLine) },
            mailHelpline = { onAction(HomeAction.OnMailHelpLine) },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeCards(
    homeCards: List<HomeCardItem>,
    onNavigate: (HomeDestinations) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showTransferDialog by rememberSaveable { mutableStateOf(false) }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        homeCards.forEach { card ->
            HomeCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp),
                titleId = card.titleId,
                imageVector = card.imageVector,
                onClick = {
                    if (card == HomeCardItem.TransferCard) {
                        showTransferDialog = true
                    } else {
                        onNavigate(card.toDestination())
                    }
                },
            )
        }
    }

    if (showTransferDialog) {
        TransferDialog(
            onDismissRequest = { showTransferDialog = false },
            navigateToTransfer = { onNavigate(HomeDestinations.TRANSFER) },
            navigateToThirdPartyTransfer = { onNavigate(HomeDestinations.THIRD_PARTY_TRANSFER) },
        )
    }
}

@Composable
private fun UserDetailsRow(
    username: String,
    userBitmap: ByteArray?,
    userProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MifosUserImage(
            modifier = Modifier
                .size(84.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                ) { userProfile.invoke() },
            bitmap = userBitmap,
            username = username,
        )
        Text(
            text = stringResource(Res.string.hello_client, username),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(1f),
        )
    }
}

@Composable
private fun HomeCard(
    titleId: StringResource,
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
            )
            Text(
                text = stringResource(titleId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun AccountOverviewCard(
    totalLoanAmount: Double,
    totalSavingsAmount: Double,
    totalSavings: () -> Unit,
    totalLoan: () -> Unit,
) {
    val isInPreview = LocalInspectionMode.current

    Row {
        MifosCard(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(Res.string.accounts_overview),
                    style = MaterialTheme.typography.bodyMedium,
                )

                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))

                MifosHiddenTextRow(
                    title = stringResource(Res.string.total_saving),
                    hiddenText = if (isInPreview) {
                        ""
                    } else {
                        CurrencyFormatter.format(
                            totalSavingsAmount,
                            "USD",
                            5,
                        )
                    },
                    hiddenColor = MaterialTheme.colorScheme.primary,
                    hidingText = stringResource(Res.string.hidden_amount),
                    visibilityIconId = Res.drawable.ic_visibility_24px,
                    visibilityOffIconId = Res.drawable.ic_visibility_off_24px,
                    onClick = totalSavings,
                )

                MifosHiddenTextRow(
                    title = stringResource(Res.string.total_loan),
                    hiddenText = if (isInPreview) {
                        ""
                    } else {
                        CurrencyFormatter.format(
                            totalLoanAmount,
                            "USD",
                            5,
                        )
                    },
                    hiddenColor = MaterialTheme.colorScheme.primary,
                    hidingText = stringResource(Res.string.hidden_amount),
                    visibilityIconId = Res.drawable.ic_visibility_24px,
                    visibilityOffIconId = Res.drawable.ic_visibility_off_24px,
                    onClick = totalLoan,
                )
            }
        }
    }
}

@Composable
private fun ContactUsRow(
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.need_help),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
        )

        Column {
            MifosLinkText(
                text = stringResource(Res.string.help_line_number),
                modifier = Modifier.align(Alignment.End),
                onClick = callHelpline,
                isUnderlined = false,
            )

            MifosLinkText(
                text = stringResource(Res.string.contact_email),
                modifier = Modifier.align(Alignment.End),
                onClick = mailHelpline,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHomeContent() {
    MifosMobileTheme {
        HomeContent(
            state = HomeState(dialogState = null),
            onAction = { },
            modifier = Modifier,
        )
    }
}
