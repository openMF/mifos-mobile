/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanType

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_select_loan_type_choose_loan
import mifos_mobile.feature.loan_application.generated.resources.feature_select_loan_type_empty
import mifos_mobile.feature.loan_application.generated.resources.feature_select_loan_type_top_bar_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosExploreCard
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * Entry point for the Loan Type Selection screen.
 * Fetches available loan products via the ViewModel and handles navigation to the product details.
 *
 * @param navigateBack Callback to return to the previous screen.
 * @param navigateToLoanProductDetailsScreen Callback to navigate to the details of a selected product ID.
 * @param viewModel The state holder responsible for fetching and mapping loan products.
 */
@Composable
internal fun SelectLoanTypeScreen(
    navigateBack: () -> Unit,
    navigateToLoanProductDetailsScreen: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SelectLoanTypeViewModel = koinViewModel(),
) {
    val state = viewModel.stateFlow.collectAsStateWithLifecycle().value

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            SelectLoanTypeEvent.NavigateBack -> navigateBack.invoke()
            is SelectLoanTypeEvent.NavigateTo ->
                navigateToLoanProductDetailsScreen.invoke(event.productId, event.productName)
        }
    }

    SelectLoanTypeScreenContent(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    SelectLoanTypeDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * Renders modal dialogs (specifically error alerts) based on the current screen state.
 *
 * @param state The current state containing potential error messages.
 * @param onAction Callback to dismiss the dialog or navigate back.
 */
@Composable
internal fun SelectLoanTypeDialog(
    state: SelectLoanTypeState,
    onAction: (SelectLoanTypeAction) -> Unit,
) {
    when (state.dialogState) {
        is SelectLoanTypeState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(state.dialogState.error),
                ),
                onDismissRequest = { onAction(SelectLoanTypeAction.NavigateBack) },
            )
        }

        null -> {}
    }
}

/**
 * Displays the visual layout including the header and a staggered grid of available loan products.
 * Handles Loading, Error, Empty, and Success UI states.
 *
 * @param state The current UI state containing the list of loan options.
 * @param onAction Callback to handle card clicks and retry actions.
 */
@Composable
internal fun SelectLoanTypeScreenContent(
    state: SelectLoanTypeState,
    modifier: Modifier = Modifier,
    onAction: (SelectLoanTypeAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        topBarTitle = stringResource(Res.string.feature_select_loan_type_top_bar_title),
        onNavigateBack = remember(state) {
            { onAction(SelectLoanTypeAction.NavigateBack) }
        },
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Empty -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(Res.string.feature_select_loan_type_empty),
                    onRetry = { onAction(SelectLoanTypeAction.Retry) },
                )
            }

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(SelectLoanTypeAction.Retry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(SelectLoanTypeAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(DesignToken.padding.large)
                        .padding(top = DesignToken.padding.large),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                    ) {
                        val productOptions = state.productOptions ?: emptyList()
                        if (productOptions.isNotEmpty()) {
                            Text(
                                text = stringResource(Res.string.feature_select_loan_type_choose_loan),
                                style = MifosTypography.labelLargeEmphasized,
                            )
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                                content = {
                                    items(productOptions) { loanType ->
                                        MifosExploreCard(
                                            icon = MifosIcons.Money,
                                            text = loanType.name ?: "",
                                            onClick = {
                                                onAction(
                                                    SelectLoanTypeAction.NavigateTo(
                                                        loanType.id ?: -1,
                                                        loanType.name ?: "",
                                                    ),
                                                )
                                            },
                                        )
                                    }
                                },
                            )
                        }
                    }
                }
            }

            else -> { }
        }
    }
}
