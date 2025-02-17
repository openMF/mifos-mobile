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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.loan_application_submitted_successfully
import mifos_mobile.feature.loan.generated.resources.loan_application_updated_successfully
import mifos_mobile.feature.loan.generated.resources.no_internet_connection
import mifos_mobile.feature.loan.generated.resources.update_loan
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosTopBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.NoInternet

@Composable
internal fun ReviewLoanApplicationScreen(
    navigateBack: (isSuccess: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewLoanApplicationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val data by viewModel.reviewLoanApplicationUiData.collectAsStateWithLifecycle()
    val isOnline by viewModel.onlineStatus.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ReviewLoanApplicationScreen(
        uiState = uiState,
        data = data,
        isOnline = isOnline,
        snackbarHostState = snackbarHostState,
        navigateBack = navigateBack,
        onSubmit = viewModel::submitLoan,
        modifier = modifier,
    )

    HandleUiState(uiState, snackbarHostState, navigateBack)
}

@Composable
private fun ReviewLoanApplicationScreen(
    uiState: ReviewLoanApplicationUiState,
    data: ReviewLoanApplicationUiData,
    isOnline: Boolean,
    snackbarHostState: SnackbarHostState,
    navigateBack: (isSuccess: Boolean) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        MifosTopBar(
            modifier = Modifier.fillMaxWidth(),
            backPress = { navigateBack(false) },
            topBarTitle = stringResource(Res.string.update_loan),
        )

        Box(modifier = Modifier.weight(1f)) {
            ReviewLoanApplicationContent(
                data = data,
                onSubmit = onSubmit,
                modifier = Modifier.padding(16.dp),
            )

            if (uiState is ReviewLoanApplicationUiState.Loading) {
                MifosProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(0.8f)),
                )
            }
        }

        SnackbarHost(hostState = snackbarHostState)

        if (!isOnline) {
            NoInternet(
                error = Res.string.no_internet_connection,
                isRetryEnabled = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun HandleUiState(
    uiState: ReviewLoanApplicationUiState,
    snackbarHostState: SnackbarHostState,
    navigateBack: (isSuccess: Boolean) -> Unit,
) {

    LaunchedEffect(uiState) {
        when (uiState) {
            is ReviewLoanApplicationUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = uiState.throwable.toString(),
                    duration = SnackbarDuration.Short,
                )
            }

            is ReviewLoanApplicationUiState.Success -> {
                val message = when (uiState.loanState) {
                    LoanState.CREATE -> Res.string.loan_application_submitted_successfully
                    LoanState.UPDATE -> Res.string.loan_application_updated_successfully
                }

                snackbarHostState.showSnackbar(
                    message = message.toString(),
                    duration = SnackbarDuration.Short,
                )

                navigateBack(true)
            }

            else -> Unit
        }
    }
}

internal class UiStatesParameterProvider : PreviewParameterProvider<ReviewLoanApplicationUiState> {
    override val values: Sequence<ReviewLoanApplicationUiState>
        get() = sequenceOf(
            ReviewLoanApplicationUiState.ReviewLoanUiReady,
            ReviewLoanApplicationUiState.Error(throwable = null),
            ReviewLoanApplicationUiState.Loading,
            ReviewLoanApplicationUiState.Success(loanState = LoanState.CREATE),
        )
}

@Preview
@Composable
private fun ReviewLoanApplicationScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class)
    reviewLoanApplicationUiState: ReviewLoanApplicationUiState,
) {
    MifosMobileTheme {
        ReviewLoanApplicationScreen(
            uiState = reviewLoanApplicationUiState,
            data = ReviewLoanApplicationUiData(),
            isOnline = true,
            navigateBack = {},
            snackbarHostState = SnackbarHostState(),
            onSubmit = {},
            modifier = Modifier,

        )
    }
}
