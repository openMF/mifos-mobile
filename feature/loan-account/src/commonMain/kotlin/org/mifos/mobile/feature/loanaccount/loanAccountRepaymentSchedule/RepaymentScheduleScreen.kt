/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.repayment_schedule
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.loanaccount.component.RepaymentScheduleItem
import template.core.base.designsystem.theme.KptTheme

/**
 * The main composable for the repayment schedule screen.
 * It displays the repayment schedule for a loan account and allows the user to pay installments.
 *
 * @param navigateBack A callback to navigate back to the previous screen.
 * @param navigateToMakePaymentScreen A callback to navigate to the make payment screen.
 * @param viewModel The [RepaymentScheduleViewModel] for this screen.
 */
@Composable
internal fun RepaymentScheduleScreen(
    navigateBack: () -> Unit,
    navigateToMakePaymentScreen: (AccountDetails) -> Unit,
    viewModel: RepaymentScheduleViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            RepaymentScheduleEvent.NavigateBack -> navigateBack.invoke()

            is RepaymentScheduleEvent.PayInstallment -> {
                navigateToMakePaymentScreen(
                    AccountDetails(
                        accountId = event.accountId,
                        outstandingBalance = event.outStandingBalance,
                        transferType = event.transferTyp,
                        transferTarget = event.transferTarget,
                        transferSuccessDestination = event.transferSuccessDestination,
                    ),
                )
            }
        }
    }

    RepaymentScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    RepaymentDialogs(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * The content of the repayment schedule screen.
 * It displays the basic details of the loan and a list of repayment periods.
 *
 * @param state The [RepaymentScheduleState] for this screen.
 * @param onAction A callback to handle actions from the screen.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
internal fun RepaymentScreenContent(
    state: RepaymentScheduleState,
    onAction: (RepaymentScheduleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.repayment_schedule),
        onNavigateBack = {
            onAction(RepaymentScheduleAction.OnNavigateBack)
        },
        modifier = modifier,
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
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(RepaymentScheduleAction.Retry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(RepaymentScheduleAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            vertical = DesignToken.padding.extraLarge,
                            horizontal = KptTheme.spacing.md,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MifosDetailsCard(keyValuePairs = state.basicDetails)

                    Spacer(Modifier.height(KptTheme.spacing.md))

                    RepaymentScheduleList(
                        periods = state.getPeriods,
                        currencyCode = state.loanWithAssociations?.currency?.code ?: "",
                        maxDigits = state.loanWithAssociations?.currency?.decimalPlaces?.toInt(),
                        onPayClick = { period ->

                            onAction(
                                RepaymentScheduleAction.OnPayInstallment(
                                    accountId = state.accountId ?: 0L,
                                    outStandingBalance = period.totalDueForPeriod ?: 0.0,
                                    transferTyp = Constants.TRANSFER_PAY_TO,
                                    transferTarget = TransferType.SELF,
                                    transferSuccessDestination = TransferSuccessDestination.LOAN_ACCOUNT,
                                ),
                            )
                        },
                    )
                }
            }

            else -> { }
        }
    }
}

/**
 * A composable that displays a list of repayment schedule items.
 *
 * @param periods The list of [Periods] to display.
 * @param currencyCode The currency code to use for formatting the amount.
 * @param maxDigits The maximum number of digits to display for the fractional part of the amount.
 * @param modifier The modifier to be applied to the component.
 * @param onPayClick A callback that is invoked when the pay button is clicked for a period.
 */
@Composable
fun RepaymentScheduleList(
    periods: List<Periods>,
    currencyCode: String?,
    maxDigits: Int?,
    modifier: Modifier = Modifier,
    onPayClick: (Periods) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
    ) {
        periods.forEach { period ->
            RepaymentScheduleItem(
                period = period,
                currencyCode = currencyCode,
                maxDigits = maxDigits,
                onPayClick = { onPayClick(period) },
            )
        }
    }
}

/**
 * A composable that displays a dialog for the repayment schedule screen.
 * It can show an error dialog.
 *
 * @param dialogState The state of the dialog to display.
 * @param onAction A callback to handle actions from the dialog.
 */
@Composable
internal fun RepaymentDialogs(
    dialogState: RepaymentScheduleState.DialogState?,
    onAction: (RepaymentScheduleAction) -> Unit,
) {
    when (dialogState) {
        is RepaymentScheduleState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(RepaymentScheduleAction.Retry) },
                isRetryEnabled = true,
            )
        }

        null -> Unit
    }
}

@Preview
@Composable
private fun Repayment_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.padding(KptTheme.spacing.md),
        ) {
            RepaymentScreenContent(
                state = RepaymentScheduleState(dialogState = null),
                onAction = {},
            )
        }
    }
}
