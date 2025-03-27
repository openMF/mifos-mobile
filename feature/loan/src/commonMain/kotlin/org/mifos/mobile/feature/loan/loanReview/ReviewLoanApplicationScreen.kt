/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanReview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.no_internet_connection
import mifos_mobile.feature.loan.generated.resources.update_loan
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun ReviewLoanApplicationScreen(
    navigateBack: (isSuccess: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewLoanApplicationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is ReviewLoanApplicationEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

            is ReviewLoanApplicationEvent.NavigateBack -> {
                navigateBack(event.isSuccess)
            }
        }
    }

    ReviewLoanApplicationScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun LoanReviewDialogs(
    state: ReviewLoanApplicationState,
    dialogState: ReviewLoanApplicationState.DialogState?,
) {
    when (dialogState) {
        is ReviewLoanApplicationState.DialogState.Error ->
            MifosErrorComponent(isNetworkConnected = state.isOnline)

        ReviewLoanApplicationState.DialogState.Loading -> MifosProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(0.8f)),
        )

        null -> Unit
    }
}

@Composable
private fun ReviewLoanApplicationScreen(
    state: ReviewLoanApplicationState,
    onAction: (ReviewLoanApplicationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        MifosTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backPress = { onAction(ReviewLoanApplicationAction.NavigateBack(false)) },
            topBarTitle = stringResource(Res.string.update_loan),
        )
        Box(modifier = Modifier.weight(1f)) {
            ReviewLoanApplicationContent(
                data = state.reviewLoanApplicationUiData,
                onSubmit = { onAction(ReviewLoanApplicationAction.SubmitLoan) },
                modifier = Modifier.padding(16.dp),
            )
        }

        if (!state.isOnline) {
            NoInternet(
                error = Res.string.no_internet_connection,
                isRetryEnabled = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    LoanReviewDialogs(
        dialogState = state.dialogState,
        state = state,
    )
}

@Preview
@Composable
private fun ReviewLoanApplicationScreenPreview() {
    MifosMobileTheme {
        ReviewLoanApplicationScreen(
            state = ReviewLoanApplicationState(dialogState = null),
            onAction = {},
            modifier = Modifier,

        )
    }
}
