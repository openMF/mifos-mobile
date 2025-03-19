/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountSummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.account_short
import mifos_mobile.feature.loan.generated.resources.account_status
import mifos_mobile.feature.loan.generated.resources.active_uc
import mifos_mobile.feature.loan.generated.resources.fees
import mifos_mobile.feature.loan.generated.resources.fees_waived
import mifos_mobile.feature.loan.generated.resources.ic_check_circle_green_24px
import mifos_mobile.feature.loan.generated.resources.ic_report_problem_red_24px
import mifos_mobile.feature.loan.generated.resources.inactive_uc
import mifos_mobile.feature.loan.generated.resources.interest
import mifos_mobile.feature.loan.generated.resources.interest_waived
import mifos_mobile.feature.loan.generated.resources.loan_product
import mifos_mobile.feature.loan.generated.resources.loan_summary
import mifos_mobile.feature.loan.generated.resources.outstanding_balance
import mifos_mobile.feature.loan.generated.resources.penalties
import mifos_mobile.feature.loan.generated.resources.penalties_waived
import mifos_mobile.feature.loan.generated.resources.principal
import mifos_mobile.feature.loan.generated.resources.total_paid
import mifos_mobile.feature.loan.generated.resources.total_repayment
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDrawableSingleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun LoanAccountSummaryScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanAccountSummaryViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            LoanAccountSummaryEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    LoanAccountSummaryScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun LoanAccountSummaryDialog(
    dialogState: LoanAccountSummaryState.DialogState?,
    state: LoanAccountSummaryState,
) {
    when (dialogState) {
        is LoanAccountSummaryState.DialogState.Error -> MifosErrorComponent(isNetworkConnected = state.isOnline)
        LoanAccountSummaryState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        null -> Unit
    }
}

@Composable
private fun LoanAccountSummaryScreen(
    state: LoanAccountSummaryState,
    modifier: Modifier = Modifier,
    onAction: (LoanAccountSummaryAction) -> Unit,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.loan_summary),
        backPress = { (onAction(LoanAccountSummaryAction.BackPress)) },
    ) {
        Box(modifier = Modifier.padding(it)) {
            state.loanAccountAssociations?.let {
                LoanAccountSummaryContent(
                    state = state,
                    modifier = modifier,
                )
            }
        }
    }
    LoanAccountSummaryDialog(
        dialogState = state.dialogState,
        state = state,
    )
}

@Composable
private fun LoanAccountSummaryContent(
    state: LoanAccountSummaryState,
    modifier: Modifier = Modifier,
) {
    val currencyCode = state.loanAccountAssociations?.currency?.code
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MifosTextTitleDescSingleLine(
            modifier = Modifier.padding(horizontal = 14.dp),
            title = stringResource(Res.string.account_short),
            description = state.loanAccountAssociations?.accountNo ?: "",
        )

        MifosTextTitleDescSingleLine(
            modifier = Modifier.padding(horizontal = 14.dp),
            title = stringResource(Res.string.loan_product),
            description = state.loanAccountAssociations?.loanProductName ?: "",
        )

        Spacer(modifier = Modifier.height(8.dp))

        MifosCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.principal),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.principal ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.interest),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.interestCharged ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.fees),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.feeChargesCharged ?: 0.0,
                        currencyCode,
                        2,
                    ),

                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.penalties),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.penaltyChargesCharged ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MifosCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.total_repayment),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.totalExpectedRepayment ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.total_paid),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.totalRepayment ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.interest_waived),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.interestWaived ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.penalties_waived),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.penaltyChargesWaived ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.fees_waived),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.feeChargesWaived ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MifosCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(Res.string.outstanding_balance),
                    description = CurrencyFormatter.format(
                        state.loanAccountAssociations?.summary?.totalOutstanding ?: 0.0,
                        currencyCode,
                        2,
                    ),
                )
                MifosTextTitleDescDrawableSingleLine(
                    title = stringResource(Res.string.account_status),
                    description = if (state.loanAccountAssociations?.status?.active == true) {
                        stringResource(
                            Res.string.active_uc,
                        )
                    } else {
                        stringResource(Res.string.inactive_uc)
                    },
                    imageResId = if (state.loanAccountAssociations?.status?.active == true) {
                        Res.drawable.ic_check_circle_green_24px
                    } else {
                        Res.drawable.ic_report_problem_red_24px
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoanAccountSummaryPreview() {
    MifosMobileTheme {
        LoanAccountSummaryScreen(
            state = LoanAccountSummaryState(dialogState = null),
            onAction = {},
            modifier = Modifier,
        )
    }
}
