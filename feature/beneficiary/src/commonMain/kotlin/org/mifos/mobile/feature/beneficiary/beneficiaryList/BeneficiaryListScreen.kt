/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.add
import mifos_mobile.feature.beneficiary.generated.resources.add_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.filter
import mifos_mobile.feature.beneficiary.generated.resources.ic_error_black_24dp
import mifos_mobile.feature.beneficiary.generated.resources.linked_with
import mifos_mobile.feature.beneficiary.generated.resources.manage_beneficiaries
import mifos_mobile.feature.beneficiary.generated.resources.no_beneficiary_found_please_add
import mifos_mobile.feature.beneficiary.generated.resources.no_filtered_beneficiary_found
import mifos_mobile.feature.beneficiary.generated.resources.type_of_account
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.designsystem.utils.onClick
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.FilterTopSection
import org.mifos.mobile.core.ui.component.MifosBeneficiariesCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
/**
 * Composable function to display the beneficiary list screen.
 *
 * @param navigateBack The callback to navigate back to the previous screen.
 * @param addBeneficiaryClicked The callback to navigate to the add beneficiary screen.
 * @param onBeneficiaryItemClick The callback to navigate to a beneficiary detail screen when a
 * beneficiary item is clicked.
 * @param modifier The modifier to apply to the composable.
 * @param viewModel The view model to use for the beneficiary list screen.
 */
@Composable
internal fun BeneficiaryListScreen(
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryListViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.trySendAction(BeneficiaryListAction.LoadBeneficiaries)
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryListEvent.Navigate -> navigateBack.invoke()
            BeneficiaryListEvent.AddBeneficiaryClicked -> addBeneficiaryClicked()
            is BeneficiaryListEvent.BeneficiaryItemClick -> onBeneficiaryItemClick(event.position)
        }
    }

    BeneficiaryListScreen(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
    BeneficiaryListDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * Composable function to display the dialogs for the beneficiary list screen.
 *
 * @param state the state of the beneficiary list screen.
 * @param onAction the callback to handle user actions.
 * @return a Unit or null if no dialog should be shown.
 */
@Composable
private fun BeneficiaryListDialog(
    state: BeneficiaryListState,
    onAction: (BeneficiaryListAction) -> Unit,
) {
    when (state.dialogState) {
        BeneficiaryListState.DialogState.Filters -> {
            BeneficiaryFilters(
                state = state,
                onAction = onAction,
            )
        }

        else -> Unit
    }
}

/**
 * Composable function to display the beneficiary list screen.
 *
 * @param state The state of the beneficiary list screen.
 * @param onAction The callback to handle user actions.
 * @param modifier The modifier to apply to the composable function.
 */
@Composable
private fun BeneficiaryListScreen(
    state: BeneficiaryListState,
    onAction: (BeneficiaryListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(BeneficiaryListAction.OnNavigate) },
        topBarTitle = stringResource(Res.string.manage_beneficiaries),
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
        if (state.dialogState == null) {
            BeneficiaryListContent(
                state = state,
                onAction = onAction,
            )
        }
    }
}

/**
 * Composable function to display the beneficiary list content.
 *
 * @param state The state of the beneficiary list content.
 * @param onAction The callback to handle user actions.
 * @param modifier The modifier to apply to the composable function.
 */
@Composable
fun BeneficiaryListContent(
    state: BeneficiaryListState,
    onAction: (BeneficiaryListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = DesignToken.padding.small),
        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.small),
    ) {
        when (state.uiState) {
            is ScreenUiState.Loading -> {
                MifosProgressIndicator()
            }

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(BeneficiaryListAction.RefreshBeneficiaries) },
                )
            }

            is ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(BeneficiaryListAction.RefreshBeneficiaries) },
                )
            }

            is ScreenUiState.Empty -> {
                Box(
                    Modifier.fillMaxSize().padding(horizontal = DesignToken.padding.large),
                    contentAlignment = Alignment.Center,
                ) {
                    Column {
                        EmptyDataView(
                            modifier = Modifier.fillMaxWidth(),
                            image = Res.drawable.ic_error_black_24dp,
                            error = Res.string.no_beneficiary_found_please_add,
                        )
                        Spacer(Modifier.padding(DesignToken.padding.large))
                        MifosButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onAction(BeneficiaryListAction.OnAddBeneficiaryClicked)
                            },
                            shape = DesignToken.shapes.medium,
                        ) {
                            Text(stringResource(Res.string.add_beneficiary))
                        }
                    }
                }
            }

            is ScreenUiState.Success -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.small),
                ) {
                    if (state.beneficiaries.isNotEmpty()) {
                        ActionBar(onAction = onAction)
                    }

                    if (state.isFilteredEmpty) {
                        EmptyDataView(
                            modifier = Modifier.fillMaxSize(),
                            image = Res.drawable.ic_error_black_24dp,
                            error = Res.string.no_filtered_beneficiary_found,
                        )
                    } else {
                        LazyColumn {
                            items(state.filteredBeneficiaries) { beneficiary ->
                                MifosBeneficiariesCard(
                                    beneficiary = beneficiary,
                                    onBeneficiaryClick = {
                                        onAction(
                                            BeneficiaryListAction
                                                .OnBeneficiaryItemClick(beneficiary.id ?: -1L),
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }

            else -> { }
        }
    }
}

/**
 * A composable function to display the action bar with add and filter buttons.
 *
 * @param onAction The callback to handle user actions.
 * @param modifier The modifier to apply to the composable function.
 */
@Composable
internal fun ActionBar(
    onAction: (BeneficiaryListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = DesignToken.padding.medium,
                horizontal = DesignToken.padding.large,
            ),
        horizontalArrangement = Arrangement.End,
    ) {
        Row(
            modifier = Modifier.clickable {
                onAction(BeneficiaryListAction.OnAddBeneficiaryClicked)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            Text(
                text = stringResource(Res.string.add),
                color = MaterialTheme.colorScheme.primary,
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.largeIncreased))

        Row(
            modifier = Modifier.clickable {
                onAction(BeneficiaryListAction.ToggleFilter)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            Text(
                text = stringResource(Res.string.filter),
                color = MaterialTheme.colorScheme.primary,
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.Filter,
                contentDescription = stringResource(Res.string.filter),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * A composable function to display the beneficiary filters.
 *
 * @param state The view state to use for the beneficiary filters.
 * @param onAction A callback to handle user actions.
 * @param modifier The modifier to apply to the composable function.
 */
@Composable
internal fun BeneficiaryFilters(
    state: BeneficiaryListState,
    onAction: (BeneficiaryListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isAccountsExpanded by rememberSaveable { mutableStateOf(true) }
    var isOfficesExpanded by rememberSaveable { mutableStateOf(true) }

    MifosElevatedScaffold(
        onNavigateBack = { onAction(BeneficiaryListAction.OnNavigate) },
        topBarTitle = stringResource(Res.string.manage_beneficiaries),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.large),
        ) {
            FilterTopSection(
                isAnyFilterSelected = state.isAnyFilterSelected,
                resetFilters = {
                    onAction(BeneficiaryListAction.ResetFilters)
                },
                onApplyFilter = {
                    onAction(BeneficiaryListAction.GetFilterResults)
                },
                dismissDialog = {
                    onAction(BeneficiaryListAction.DismissDialog)
                },
            )

            Spacer(Modifier.height(DesignToken.spacing.largeIncreased))

            HorizontalDivider(modifier = Modifier.height(1.dp))

            FilterSection(
                title = stringResource(Res.string.linked_with),
                selectedFilters = state.selectedOffices,
                isExpanded = isOfficesExpanded,
                onToggle = { isOfficesExpanded = !isOfficesExpanded },
                filters = state.offices,
                onCheckChanged = {
                    onAction(BeneficiaryListAction.OnOfficeChange(it ?: ""))
                },
            )

            FilterSection(
                title = stringResource(Res.string.type_of_account),
                selectedFilters = state.selectedAccounts,
                isExpanded = isAccountsExpanded,
                onToggle = { isAccountsExpanded = !isAccountsExpanded },
                filters = state.template?.accountTypeOptions?.map { it.value } ?: emptyList(),
                onCheckChanged = {
                    onAction(BeneficiaryListAction.OnAccountChange(it ?: ""))
                },
            )
        }
    }
}

/**
 * A composable function to display a filter section.
 *
 * @param title The title of the filter section.
 * @param selectedFilters The list of selected filters.
 * @param isExpanded Whether the filter section is expanded or not.
 * @param onToggle The callback to toggle the filter section.
 * @param filters The list of filters to display.
 * @param onCheckChanged The callback to handle the check changed event of the filters.
 * @param modifier The modifier to apply to the composable.
 */
@Composable
internal fun FilterSection(
    title: String,
    selectedFilters: Set<String>,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    filters: List<String?>,
    onCheckChanged: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(
                start = DesignToken.spacing.extraLargeIncreased,
                end = DesignToken.spacing.small,
                top = DesignToken.padding.medium,
            ),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MifosTypography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (selectedFilters.isNotEmpty()) {
                        Text(
                            text = "${selectedFilters.size} selected",
                            style = MifosTypography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .width(DesignToken.sizes.iconSmall)
                            .height(DesignToken.sizes.iconSmall),
                        imageVector = if (isExpanded) MifosIcons.ChevronUp else MifosIcons.ChevronDown,
                        contentDescription = null,
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 40 }) + fadeOut(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
                ) {
                    filters.forEach { filter ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onClick {
                                    onCheckChanged(filter)
                                }
                                .padding(horizontal = DesignToken.padding.largeIncreased),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                                checked = selectedFilters.contains(filter),
                                onCheckedChange = {
                                    onCheckChanged(filter)
                                },
                            )

                            Spacer(modifier = Modifier.width(DesignToken.spacing.small))
                            Text(
                                text = filter ?: "",
                                style = MifosTypography.labelMediumEmphasized,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = DesignToken.padding.medium)
                .height(1.dp),
        )
    }
}

@Preview
@Composable
fun PreviewBeneficiaryListScreen() {
    MifosMobileTheme {
        BeneficiaryListScreen(
            state = BeneficiaryListState(dialogState = null),
            onAction = { },
            modifier = Modifier,
        )
    }
}
