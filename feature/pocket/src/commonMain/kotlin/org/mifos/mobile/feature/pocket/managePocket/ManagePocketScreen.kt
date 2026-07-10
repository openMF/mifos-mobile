/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.managePocket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.pocket.generated.resources.Res
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_action_cancel
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_action_link
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_action_ok
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_action_remove
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_delink_account_message
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_dialog_error_title
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_link_accounts_title
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_link_more_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_link_selected
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_linked_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_manage_title
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_no_available_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_no_linked_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_remove_account_detail
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_remove_account_title
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_you_are_removing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.LoadingDialogState
import org.mifos.mobile.core.designsystem.component.MifosBottomSheet
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosLoadingDialog
import org.mifos.mobile.core.designsystem.component.MifosSearchTextField
import org.mifos.mobile.core.designsystem.component.MifosTabPager
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosCheckBox
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun ManagePocketScreen(
    navigateBack: () -> Unit,
    viewModel: ManagePocketViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            ManagePocketEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    ManagePocketContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ManagePocketDialogs(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun ManagePocketContent(
    state: ManagePocketState,
    onAction: (ManagePocketAction) -> Unit,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(ManagePocketAction.NavigateBack) },
        topBarTitle = stringResource(Res.string.feature_pocket_manage_title),
        containerColor = KptTheme.colorScheme.background,
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> MifosProgressIndicator()
            is ScreenUiState.ErrorString -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = state.uiState.message,
                    onRetry = { onAction(ManagePocketAction.Retry) },
                )
            }
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(ManagePocketAction.Retry) },
                )
            }
            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(ManagePocketAction.Retry) },
                )
            }
            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(DesignToken.padding.large),
                ) {
                    LinkMoreAccountsCard(
                        onLinkClick = { onAction(ManagePocketAction.OpenLinkAccounts) },
                    )

                    Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

                    Text(
                        text = stringResource(Res.string.feature_pocket_linked_accounts),
                        style = MifosTypography.titleSmallEmphasized,
                        color = KptTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                    ) {
                        if (state.linkedAccounts.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.feature_pocket_no_linked_accounts),
                                style = MifosTypography.bodyMedium,
                                color = KptTheme.colorScheme.secondary,
                                modifier = Modifier.padding(top = DesignToken.padding.medium),
                            )
                        } else {
                            state.linkedAccounts.forEach { account ->
                                LinkedPocketAccountCard(
                                    account = account,
                                    onRemoveClick = {
                                        onAction(ManagePocketAction.OpenDelinkConfirmation(account))
                                    },
                                )
                            }
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun ManagePocketDialogs(
    state: ManagePocketState,
    onAction: (ManagePocketAction) -> Unit,
) {
    when (val dialogState = state.dialogState) {
        ManagePocketDialogState.LinkAccounts -> {
            MifosBottomSheet(
                onDismiss = { onAction(ManagePocketAction.DismissDialog) },
            ) {
                LinkAccountsSheet(
                    state = state,
                    onAction = onAction,
                )
            }
        }

        is ManagePocketDialogState.DelinkConfirmation -> {
            MifosBottomSheet(
                onDismiss = { onAction(ManagePocketAction.DismissDialog) },
            ) {
                RemoveLinkedAccountSheet(
                    account = dialogState.account,
                    onCancelClick = { onAction(ManagePocketAction.DismissDialog) },
                    onRemoveClick = {
                        onAction(ManagePocketAction.DelinkAccount(dialogState.account))
                    },
                )
            }
        }

        is ManagePocketDialogState.Error -> {
            MifosAlertDialog(
                dialogTitle = stringResource(Res.string.feature_pocket_dialog_error_title),
                dialogText = stringResource(dialogState.message),
                dismissText = stringResource(Res.string.feature_pocket_action_cancel),
                confirmationText = stringResource(Res.string.feature_pocket_action_ok),
                onDismissRequest = { onAction(ManagePocketAction.DismissDialog) },
                onConfirmation = { onAction(ManagePocketAction.DismissDialog) },
                icon = MifosIcons.Error,
            )
        }

        ManagePocketDialogState.Loading -> {
            MifosLoadingDialog(visibilityState = LoadingDialogState.Shown)
        }

        null -> Unit
    }
}

@Composable
private fun LinkMoreAccountsCard(
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosCustomCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onLinkClick,
        variant = CardVariant.FILLED,
        shape = KptTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = KptTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            contentColor = KptTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignToken.elevation.none),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignToken.padding.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
        ) {
            Text(
                text = stringResource(Res.string.feature_pocket_link_more_accounts),
                modifier = Modifier.weight(1f),
                style = MifosTypography.titleMediumEmphasized,
                color = KptTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            MifosButton(
                onClick = onLinkClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = KptTheme.colorScheme.primary,
                    contentColor = KptTheme.colorScheme.onPrimary,
                ),
                content = {
                    Text(
                        text = stringResource(Res.string.feature_pocket_action_link),
                        style = MifosTypography.labelLarge,
                    )
                },
            )
        }
    }
}

@Composable
private fun LinkedPocketAccountCard(
    account: ManagePocketAccount,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PocketAccountRow(
        account = account,
        modifier = modifier,
        trailingContent = {
            Surface(
                modifier = Modifier.size(DesignToken.sizes.avatarMedium),
                shape = CircleShape,
                color = KptTheme.colorScheme.errorContainer.copy(alpha = 0.62f),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onRemoveClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = MifosIcons.Delete,
                        contentDescription = stringResource(Res.string.feature_pocket_action_remove),
                        tint = KptTheme.colorScheme.error,
                        modifier = Modifier.size(DesignToken.sizes.iconMedium),
                    )
                }
            }
        },
    )
}

@Composable
private fun PocketAccountRow(
    account: ManagePocketAccount,
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = DesignToken.padding.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        PocketAccountIcon(icon = account.accountType.toIcon())

        Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = account.accountNumber,
                style = MifosTypography.titleSmallEmphasized,
                color = KptTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = account.name,
                style = MifosTypography.bodySmall,
                color = KptTheme.colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

        trailingContent()
    }
}

@Composable
private fun LinkAccountsSheet(
    state: ManagePocketState,
    onAction: (ManagePocketAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(AccountType.SAVINGS, AccountType.LOAN, AccountType.SHARE)
    val selectedTabIndex = tabs.indexOf(state.selectedTab).coerceAtLeast(0)
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { tabs.size },
    )
    var searchValue by remember {
        mutableStateOf(TextFieldValue(state.searchQuery))
    }

    LaunchedEffect(state.searchQuery) {
        if (state.searchQuery != searchValue.text) {
            searchValue = searchValue.copy(text = state.searchQuery)
        }
    }

    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 520.dp, max = 720.dp)
            .padding(horizontal = DesignToken.padding.large),
    ) {
        Text(
            text = stringResource(Res.string.feature_pocket_link_accounts_title),
            style = MifosTypography.headlineSmallEmphasized,
            color = KptTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = DesignToken.padding.large),
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.large))

        MifosSearchTextField(
            value = searchValue,
            onValueChange = {
                searchValue = it
                onAction(ManagePocketAction.SearchQueryChanged(it.text))
            },
            onSearchDismiss = {
                searchValue = TextFieldValue("")
                onAction(ManagePocketAction.SearchQueryChanged(""))
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            MifosTabPager(
                modifier = Modifier.fillMaxSize(),
                pagerState = pagerState,
                currentPage = selectedTabIndex,
                tabs = tabs.map { it.name },
                setCurrentPage = { page ->
                    onAction(ManagePocketAction.TabSelected(tabs[page]))
                },
            ) { page ->
                val accounts = state.availableAccounts.filter { it.accountType == tabs[page] }

                when {
                    state.isAvailableAccountsLoading -> MifosProgressIndicator()
                    accounts.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.feature_pocket_no_available_accounts),
                                style = MifosTypography.bodyMedium,
                                color = KptTheme.colorScheme.secondary,
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                        ) {
                            item { Spacer(modifier = Modifier.height(DesignToken.spacing.small)) }
                            items(accounts, key = { it.accountId }) { account ->
                                SelectablePocketAccountCard(
                                    account = account,
                                    selected = account.accountId in state.selectedAccountIds,
                                    onSelectedChange = { selected ->
                                        onAction(
                                            ManagePocketAction.AccountSelectionChanged(
                                                accountId = account.accountId,
                                                selected = selected,
                                            ),
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }

            if (state.searchQuery.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KptTheme.colorScheme.surface.copy(alpha = 0.95f)),
                ) {
                    val searchResults = state.availableAccounts.filter {
                        it.name.contains(state.searchQuery, ignoreCase = true) ||
                            it.accountNumber.contains(state.searchQuery, ignoreCase = true)
                    }

                    if (searchResults.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.feature_pocket_no_available_accounts),
                                style = MifosTypography.bodyMedium,
                                color = KptTheme.colorScheme.secondary,
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                        ) {
                            item { Spacer(modifier = Modifier.height(DesignToken.spacing.small)) }
                            items(searchResults, key = { it.accountId }) { account ->
                                SelectablePocketAccountCard(
                                    account = account,
                                    selected = account.accountId in state.selectedAccountIds,
                                    onSelectedChange = { selected ->
                                        onAction(
                                            ManagePocketAction.AccountSelectionChanged(
                                                accountId = account.accountId,
                                                selected = selected,
                                            ),
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = KptTheme.colorScheme.outlineVariant)

        MifosButton(
            onClick = { onAction(ManagePocketAction.LinkSelectedAccounts) },
            enabled = state.selectedAccountIds.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DesignToken.padding.large),
            content = {
                Text(
                    text = stringResource(
                        Res.string.feature_pocket_link_selected,
                        state.selectedAccountIds.size,
                    ),
                    style = MifosTypography.labelLarge,
                )
            },
        )
    }
}

@Composable
private fun SelectablePocketAccountCard(
    account: AvailablePocketAccount,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectedChange(!selected) }
            .padding(vertical = DesignToken.padding.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MifosCheckBox(
            text = "",
            checked = selected,
            onCheckChanged = onSelectedChange,
        )

        Spacer(modifier = Modifier.width(DesignToken.spacing.small))

        PocketAccountIcon(icon = account.accountType.toIcon())

        Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = account.accountNumber,
                style = MifosTypography.titleSmallEmphasized,
                color = KptTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = account.name,
                style = MifosTypography.bodySmall,
                color = KptTheme.colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PocketAccountIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = KptTheme.colorScheme.onBackground.copy(alpha = 0.3f),
        modifier = modifier
            .background(
                color = KptTheme.colorScheme.background.copy(alpha = 0.5f),
                shape = CircleShape,
            )
            .padding(KptTheme.spacing.sm),
    )
}

private fun AccountType.toIcon(): ImageVector =
    when (this) {
        AccountType.SAVINGS -> MifosIcons.PersonAccounts
        AccountType.LOAN -> MifosIcons.CoinMultiple
        AccountType.SHARE -> MifosIcons.CoinMultiple
    }

@Composable
private fun RemoveLinkedAccountSheet(
    account: ManagePocketAccount,
    onCancelClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(DesignToken.padding.large),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            Icon(
                imageVector = MifosIcons.Warning,
                contentDescription = null,
                tint = KptTheme.colorScheme.error,
            )
            Text(
                text = stringResource(Res.string.feature_pocket_remove_account_title),
                style = MifosTypography.headlineSmallEmphasized,
                color = KptTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

        MifosCustomCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            enabled = false,
            variant = CardVariant.FILLED,
            shape = KptTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = KptTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                contentColor = KptTheme.colorScheme.onSurface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(DesignToken.padding.large),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
            ) {
                Text(
                    text = stringResource(Res.string.feature_pocket_you_are_removing),
                    style = MifosTypography.bodyLarge,
                    color = KptTheme.colorScheme.secondary,
                )
                Text(
                    text = account.name,
                    style = MifosTypography.titleMediumEmphasized,
                    color = KptTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(
                        Res.string.feature_pocket_remove_account_detail,
                        account.accountNumber,
                    ),
                    style = MifosTypography.bodyLarge,
                    color = KptTheme.colorScheme.secondary,
                )
            }
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

        Text(
            text = stringResource(Res.string.feature_pocket_delink_account_message),
            style = MifosTypography.bodyLarge,
            color = KptTheme.colorScheme.secondary,
        )

        Spacer(modifier = Modifier.height(DesignToken.spacing.extraLarge))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MifosButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = KptTheme.colorScheme.surface,
                    contentColor = KptTheme.colorScheme.primary,
                ),
                content = {
                    Text(
                        text = stringResource(Res.string.feature_pocket_action_cancel),
                        style = MifosTypography.labelLarge,
                    )
                },
            )

            MifosButton(
                onClick = onRemoveClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = KptTheme.colorScheme.error,
                    contentColor = KptTheme.colorScheme.onError,
                ),
                content = {
                    Text(
                        text = stringResource(Res.string.feature_pocket_action_remove),
                        style = MifosTypography.labelLarge,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun ManagePocketContentPreview() {
    MifosMobileTheme {
        ManagePocketContent(
            state = ManagePocketState(
                linkedAccounts = listOf(
                    previewPocketAccount(1, AccountType.SAVINGS, "1004859238", "Emergency Fund"),
                    previewPocketAccount(2, AccountType.LOAN, "3009284756", "Personal Loan"),
                    previewPocketAccount(3, AccountType.SHARE, "5001129384", "Company Shares"),
                ),
                uiState = ScreenUiState.Success,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun LinkAccountsSheetContentPreview() {
    MifosMobileTheme {
        LinkAccountsSheet(
            state = ManagePocketState(
                availableAccounts = listOf(
                    previewAvailablePocketAccount(1, AccountType.SAVINGS, "1004859238", "Emergency Fund"),
                    previewAvailablePocketAccount(2, AccountType.SAVINGS, "1004859239", "Vacation Savings"),
                ),
                selectedAccountIds = setOf(1),
                uiState = ScreenUiState.Success,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun RemoveLinkedAccountSheetContentPreview() {
    MifosMobileTheme {
        RemoveLinkedAccountSheet(
            account = previewPocketAccount(1, AccountType.SAVINGS, "1004859238", "Emergency Fund"),
            onCancelClick = {},
            onRemoveClick = {},
        )
    }
}

private fun previewPocketAccount(
    id: Long,
    type: AccountType,
    number: String,
    name: String,
) = ManagePocketAccount(
    accountId = id,
    mappingId = id,
    name = name,
    accountNumber = number,
    accountType = type,
)

private fun previewAvailablePocketAccount(
    id: Long,
    type: AccountType,
    number: String,
    name: String,
) = AvailablePocketAccount(
    accountId = id,
    name = name,
    accountNumber = number,
    accountType = type,
)
