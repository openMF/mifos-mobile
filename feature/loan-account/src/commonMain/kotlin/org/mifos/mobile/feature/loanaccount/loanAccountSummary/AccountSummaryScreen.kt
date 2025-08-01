/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountSummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_loan_summary
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_details_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_charges_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installment_details_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_outstanding_details_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_paid_off_details_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_payoff_details_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_waivers_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.loanaccount.component.AccountSummaryCard
import kotlin.collections.orEmpty

@Composable
internal fun LoanAccountSummaryScreen(
    navigateBack: () -> Unit,
    viewModel: LoanAccountSummaryViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is LoanAccountSummaryEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    LoanAccountSummaryContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    LoanAccountSummaryDialog(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun LoanAccountSummaryDialog(
    dialogState: LoanAccountSummaryState.DialogState?,
    onAction: (LoanAccountSummaryAction) -> Unit,
) {
    when (dialogState) {
        is LoanAccountSummaryState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(LoanAccountSummaryAction.Retry) },
                isRetryEnabled = true,
            )
        }
        is LoanAccountSummaryState.DialogState.Loading -> MifosProgressIndicator()

        null -> Unit
    }
}

@Composable
internal fun LoanAccountSummaryContent(
    state: LoanAccountSummaryState,
    onAction: (LoanAccountSummaryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(LoanAccountSummaryAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_account_action_loan_summary),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DesignToken.padding.large)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
            ) {
                Spacer(modifier = Modifier.height(DesignToken.spacing.medium))
                AccountSummaryCard(
                    title = stringResource(Res.string.feature_loan_account_details_title),
                    keyValuePairs = state.accountDetails.orEmpty()
                        .associate { it.label to it.value },
                )

                AccountSummaryCard(
                    title = stringResource(Res.string.feature_loan_payoff_details_title),
                    keyValuePairs = state.payOffDetails.orEmpty().associate { it.label to it.value },
                )

                AccountSummaryCard(
                    title = stringResource(Res.string.feature_loan_charges_title),
                    keyValuePairs = state.chargeDetails.orEmpty().associate { it.label to it.value },
                )

                AccountSummaryCard(
                    title = stringResource(Res.string.feature_loan_waivers_title),
                    keyValuePairs = state.waiversDetails.orEmpty()
                        .associate { it.label to it.value },
                )

                AccountSummaryCard(
                    title = stringResource(Res.string.feature_loan_paid_off_details_title),
                    keyValuePairs = state.paidOffDetails.orEmpty()
                        .associate { it.label to it.value },
                )

                AccountSummaryCard(
                    title = stringResource(Res.string.feature_loan_outstanding_details_title),
                    keyValuePairs = state.outStandingDetails.orEmpty()
                        .associate { it.label to it.value },
                )

                AccountSummaryCard(
                    title = stringResource(Res.string.feature_loan_installment_details_title),
                    keyValuePairs = state.installmentDetails.orEmpty()
                        .associate { it.label to it.value },
                )
            }
        }
    }
}

@Preview
@Composable
private fun Loan_Account_Summary_Preview() {
    MifosMobileTheme {
        LoanAccountSummaryContent(
            state = LoanAccountSummaryState(
                dialogState = null,
                accountId = -1L,
            ),
            onAction = {},
        )
    }
}
